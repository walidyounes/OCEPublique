/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
/**
 * This is an abstract class representing a decision made by the OCE engine
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class AbstractDecision {

    protected OCEAgent emitter; // The transmitter of the message
    protected ArrayList<OCEAgent> receivers; // The list of the recipients of the message, if == null -> message is in broadcast

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */

    public final OCEAgent getEmitter() {
        return this.emitter;
    }

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    public final void setEmitter(OCEAgent emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the receivers of the message
     * @return the receivers of the message
     */
    public final ArrayList<OCEAgent> getReceivers() {
        return this.receivers;
    }

    /**
     * set the list of receivers for this message
     * @param receivers : the list of receivers
     */
    public final void setReceivers(ArrayList<OCEAgent> receivers) {
        this.receivers = receivers;
    }

    /**
     * Execute the decision that has been made by the engine
     * @param communicationAdapter : the communication component of the engine
     */
    public abstract void toSelfTreat(ICommunicationAdapter communicationAdapter);

}
