/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;

/**
 * This is an abstract class representing a perception of an agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class AbstractPerception {

    protected ServiceAgent emitter; // The transmitter of the message
    protected ArrayList<ServiceAgent> recievers; // The list of the recipients of the message, if == null -> message is in broadcast

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */

    public ServiceAgent getEmitter() {
        return this.emitter;
    }

    /**
     *  set the reference of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */

    public void setEmitter(ServiceAgent emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the recievers of the message
     * @return the recievers of the message
     */

    public ArrayList<ServiceAgent> getRecievers() {
        return this.recievers;
    }

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    public void setRecievers(ArrayList<ServiceAgent> recievers) {
        this.recievers = recievers;
    }

    /**
     * treat the message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    public abstract AbstractDecision toSelfTreat( ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef, OCService localService);
}
