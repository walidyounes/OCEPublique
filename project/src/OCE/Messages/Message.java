/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.IMessage;
import OCE.Decisions.AbstractDecision;

import java.util.ArrayList;

/**
 * This is an abstract class representing a message that can be exchanged between agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class Message implements IMessage {

    protected InfraAgentReference emitter; // The transmitter of the message
    protected ArrayList<InfraAgentReference> recievers; // The list of the recipients of the message, if == null -> message is in broadcast

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
     * get the list of the recievers of the message
     * @return the recievers of the message
     */
    @Override
    public ArrayList<InfraAgentReference> getRecievers() {
        return this.recievers;
    }

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    @Override
    public void setRecievers(ArrayList<InfraAgentReference> recievers) {
        this.recievers = recievers;
    }

    /**
     * treat the message according to it's type
     */
    public abstract AbstractDecision toSelfTreat();
}
