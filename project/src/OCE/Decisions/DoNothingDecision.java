/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Medium.Communication.ICommunicationAdapter;

/**
 * This class represent an empty decision which mean the engine wasn't abale for some reason to make a decision (and it can be also do not nothing)
 * @author Walid YOUNES
 * @version 1.0
 */
public class DoNothingDecision extends OCEDecision {

    /**
     * Create an empty Decision
     */
    public DoNothingDecision() {
        this.emitter = null;
        this.receivers = null;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }

    @Override
    public String toString() {
        return "DoNothingDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
