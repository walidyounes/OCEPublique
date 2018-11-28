/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.Decisions.AbstractDecision;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;

import java.util.ArrayList;

/**
 * This is an abstract class representing a perception of an agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class AbstractPerception {

    protected OCEAgent emitter; // The transmitter of the message
    protected ArrayList<OCEAgent> receivers; // The list of the recipients of the message, if == null -> message is in broadcast

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */

    public OCEAgent getEmitter() {
        return this.emitter;
    }

    /**
     *  set the reference of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */

    public void setEmitter(OCEAgent emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the receivers of the message
     * @return the receivers of the message
     */

    public ArrayList<OCEAgent> getReceivers() {
        return this.receivers;
    }

    /**
     * set the list of receivers for this message
     * @param receivers : the list of receivers
     */
    public void setReceivers(ArrayList<OCEAgent> receivers) {
        this.receivers = receivers;
    }

    /**
     * treat the message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    public abstract AbstractDecision toSelfTreat( ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService);

    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    public abstract Boolean toSelfFilterAdvertise();

    @Override
    public String toString() {
        return "AbstractPerception{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
