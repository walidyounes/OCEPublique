/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.IMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;

import java.util.ArrayList;

/**
 * This is an abstract class representing a message that can be exchanged between agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class InfraMessage implements IMessage {

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
     *  set the reference of the transmitter of the message
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
     * get the type of message,  which can be : Advertise, reply, select, agree...
     * @return the type of the message
     */
    public abstract MessageTypes getMyType();

    /**
     * Transform the message into perception (this method is useful because in the OCE engine we deal with ServiceAgent reference not InfraAgent reference)
     * @param referenceResolver : the component used to resolv the adress ServiceAgent <> InfraAgentReference
     * @return the perception equivalent to the message
     */
    public abstract OCEMessage toOCEMessage(IRecord referenceResolver);


}
