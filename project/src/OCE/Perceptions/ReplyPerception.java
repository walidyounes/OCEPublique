/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.EmptyDecision;
import OCE.Decisions.SelectDecision;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyPerception extends AbstractPerception {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param receivers the references of the receivers of the response, if null == Broadcast
     */
    public ReplyPerception(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }


    /**
     * treat the response message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, OCEAgentRef + " treats a reply message");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            // Send a selection message to the emitter of this message
            ArrayList<OCEAgent> SelectionReceivers = new ArrayList<>();
            SelectionReceivers.add(this.emitter);
            return new SelectDecision(OCEAgentRef, SelectionReceivers);
        }
        else{
            return new EmptyDecision();
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
}
