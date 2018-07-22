/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.EmptyDecision;
import OCE.Decisions.ReplyDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class AdPerception extends AbstractPerception {

    private OCService distantService; // the information about the service of the agent that send this advertisement
    /**
     * Create an advertise message
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AdPerception(ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    /**
     * treat the advertising message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef, OCService localService) {
        MyLogger.log(Level.INFO, "Treating an advertisement message ! ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            //Verify the matching between the services Todo use the matching in the future
            //todo here we are casting the type to MockupService
            MockupService service1 =  (MockupService)localService;
            MockupService service2 = (MockupService)this.distantService;

            //verify the matching
            if(service1.equals(service2)){
                // Send a reply message to the emitter of this message
                ArrayList<ServiceAgent> replyRecievers = new ArrayList<>();
                replyRecievers.add(this.emitter);
                return new ReplyDecision(serviceAgentRef, replyRecievers);
            }
            else return new EmptyDecision();
        }else return new EmptyDecision();
    }
}
