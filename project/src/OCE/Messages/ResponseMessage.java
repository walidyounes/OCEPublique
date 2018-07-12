/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.AbstractPerception;

import java.util.ArrayList;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ResponseMessage extends Message {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param recievers the references of the recievers of the response, if null == Broadcast
     */
    public ResponseMessage(AgentReference emitter, ArrayList<AgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    /**
     * create a Response message (empty)
     */
    public ResponseMessage() {
        this.emitter= null;
        this.recievers = null;
    }

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */
    @Override
    public AgentReference getEmitter() {
        return this.emitter;
    }

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    @Override
    public void setEmitter(AgentReference emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the recievers of the message
     * @return the recievers of the message
     */
    @Override
    public ArrayList<AgentReference> getRecievers() {
        return this.recievers;
    }

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    @Override
    public void setRecievers(ArrayList<AgentReference> recievers) {
        this.recievers = recievers;
    }


}
