/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.AgentReference;

import java.util.ArrayList;

public class EmptyMessage extends Message {


    /**
     * Create an empty message
     */
    public EmptyMessage() {
        this.emitter = null;
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
