/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.IMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.Perceptions.AbstractPerception;

import java.util.ArrayList;

/**
 * This is an abstract class representing a message that can be exchanged between agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class Message implements IMessage {

    protected InfraAgentReference emitter; // The transmitter of the message
    protected ArrayList<InfraAgentReference> receivers; // The list of the recipients of the message, if == null -> message is in broadcast
    protected MessageTypes myType; // The type of the message (Advertise, reply ..etc.)
    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */
    @Override
    public InfraAgentReference getEmitter() {
        return this.emitter;
    }

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    @Override
    public void setEmitter(InfraAgentReference emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the receivers of the message
     * @return the receivers of the message
     */
    @Override
    public ArrayList<InfraAgentReference> getReceivers() {
        return this.receivers;
    }

    /**
     * set the list of receivers for this message
     * @param receivers : the list of receivers
     */
    @Override
    public void setReceivers(ArrayList<InfraAgentReference> receivers) {
        this.receivers = receivers;
    }

    /**
     * treat the message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
   //  public abstract AbstractDecision toSelfTreat( ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef, OCService localService);

    /**
     * Transfome the message into perception (this method is useful because in the OCE engine we deal with ServiceAgent reference not InfraAgent reference)
     * @param referenceResolver : the component used to resolv the adress ServiceAgent <> InfraAgentReference
     * @return the perception equivalent to the message
     */
    public abstract AbstractPerception toPerception(IRecord referenceResolver);

    public abstract MessageTypes getMyType();
}
