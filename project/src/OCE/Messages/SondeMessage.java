/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.AgentReference;

import java.util.ArrayList;

public class SondeMessage extends Message {

    private Boolean exist; // Boolean attribute used to inform the agent if it's service still exsit or not

    /**
     * Create A Sonde Message
     * @param exist :  boolean value informing if the service attached to the agent is still existing in the environement or not
     * @param recievers : the references of the recievers of the ad, if null == Broadcast
     */
    public SondeMessage(Boolean exist, ArrayList<AgentReference> recievers) {
        this.exist = exist;
        this.emitter = null; // The sonde has no reference
        this.recievers = recievers;
    }

    /**
     * Create a Sonde message (empty message)
     */
    public SondeMessage() {
        this.exist = false;
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

    /**
     * get the value of the boolean indiating if the sevice still exist or not
     * @return the value of the boolean
     */
    public Boolean getExist() {
        return exist;
    }

    /**
     * Set the value of the boolean indicating if the service exist or not
     * @param exist
     */
    public void setExist(Boolean exist) {
        this.exist = exist;
    }
}
