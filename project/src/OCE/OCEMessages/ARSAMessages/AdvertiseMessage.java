/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages.ARSAMessages;


import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Decisions.DoNothingDecision;
import OCE.Decisions.OCEDecision;
import OCE.Decisions.ReplyDecision;
import OCE.OCEMessages.MessageTypes;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the advertise message sent in the first step of the ARSA protocol, it allows the agent to announce its availability to others service agents
 * @author Walid YOUNES
 * @version 1.0
 */
public class AdvertiseMessage extends ARSAMessage {

    private OCService distantService; // the information about the service of the agent that send this advertisement
    /**
     * Create an advertise message
     * @param emitter    reference of the advertising agent
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public AdvertiseMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers, OCService distantService) {
        this.emitter= emitter;
        this.receivers = receivers;
        this.distantService = distantService;
    }

    /**
     * treat the advertising message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param oceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent oceAgentRef, OCService localService) {
        MyLogger.log(Level.INFO, oceAgentRef + " treats an advertisement message ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            MyLogger.log(Level.INFO, oceAgentRef +" is not connected  ");
            //Verify the matching between the services Todo use the matching in the future
            //todo here we are casting the type to MockupService
            MockupService service1 =  (MockupService)localService;
            MockupService service2 = (MockupService)this.distantService;
           // MyLogger.log(Level.INFO, "service local = " + service1.toString());
           // MyLogger.log(Level.INFO, "service distant = " + service2.toString());
            //verify the matching // TODO à améliorer le matching
            IMatching matching = new Matching();

            if(matching.match(service1, service2)){
                MyLogger.log(Level.INFO, "The two services matchs ! ");
                // Send a reply message to the emitter of this message
                ArrayList<OCEAgent> replyReceivers = new ArrayList<>();
                replyReceivers.add(this.emitter);
                return new ReplyDecision(oceAgentRef, replyReceivers);
            }
            else return new DoNothingDecision();
        }else return new DoNothingDecision();
    }

    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return true;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry((ServiceAgent) this.emitter, MessageTypes.ADVERTISE);
    }
}
