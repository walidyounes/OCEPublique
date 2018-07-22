/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.AdvertiseDecision;
import OCE.Decisions.EmptyDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.logging.Level;

public class EmptyPerception extends AbstractPerception {

    /**
     * Create an empty message
     */
    public EmptyPerception(ServiceAgent myInfraAgentRef) {
        this.emitter = myInfraAgentRef; // Here the emitter is the agent who recieved this empty message --> it's usuful to have the reference Of the Agent
        this.recievers = null;
    }

    /**
     * treat the empty message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef, OCService localService) {
        MyLogger.log(Level.INFO, "Treating an empty message ! ");
        if(stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){ // if the agent was created ->
            //send a advertisement message todo "this could be enhaced later on
            return new AdvertiseDecision(this.emitter, null);
        }else return new EmptyDecision();

    }

}
