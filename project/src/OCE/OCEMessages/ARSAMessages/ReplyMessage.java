/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages.ARSAMessages;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEDecisions.ARSADecisions.SelectDecision;
import OCE.OCEMessages.MessageTypes;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyMessage extends ARSAMessage {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param receivers the references of the receivers of the response, if null == Broadcast
     */
    public ReplyMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }


    /**
     * treat the response message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "CREATED, CONNECTED, NOT_CONNECTED, WAITING"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a reply message");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NOT_CONNECTED) || stateConnexionAgent.equals(ServiceAgentConnexionState.CREATED)){
            // Send a selection message to the emitter of this message
            ArrayList<OCEAgent> SelectionReceivers = new ArrayList<>();
            SelectionReceivers.add(this.emitter);
            return new SelectDecision(OCEAgentRef, SelectionReceivers);
        }
        else{
            return new DoNothingDecision();
        }
    }

    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.REPLY);
    }
}
