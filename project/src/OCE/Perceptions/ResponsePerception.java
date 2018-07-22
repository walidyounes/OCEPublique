/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.SelectDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ResponsePerception extends AbstractPerception {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param recievers the references of the recievers of the response, if null == Broadcast
     */
    public ResponsePerception(ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }


    /**
     * treat the response message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating an advertisement message ! ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            // Send a selection message to the emitter of this message
            ArrayList<ServiceAgent> SelectionRecievers = new ArrayList<>();
            SelectionRecievers.add(this.emitter);
            return new SelectDecision(serviceAgentRef, SelectionRecievers);
        }
        return null;
    }
}
