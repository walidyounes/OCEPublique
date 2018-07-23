/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceAgent;

import java.util.ArrayList;
/**
 * This is an abstract class representing a decision made by the OCE engine
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class AbstractDecision {

    protected ServiceAgent emitter; // The transmitter of the message
    protected ArrayList<ServiceAgent> recievers; // The list of the recipients of the message, if == null -> message is in broadcast

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */

    public final ServiceAgent getEmitter() {
        return this.emitter;
    }

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    public final void setEmitter(ServiceAgent emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the recievers of the message
     * @return the recievers of the message
     */
    public final ArrayList<ServiceAgent> getRecievers() {
        return this.recievers;
    }

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    public final void setRecievers(ArrayList<ServiceAgent> recievers) {
        this.recievers = recievers;
    }

    /**
     * Execute the decision that has been made by the engine
     * @param communicationAdapter : the communication component of the engine
     */
    public abstract void toSelfTreat(ICommunicationAdapter communicationAdapter);

}
