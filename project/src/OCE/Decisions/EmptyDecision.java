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
public class EmptyDecision extends AbstractDecision {

    /**
     * Create an empty Decision
     */
    public EmptyDecision() {
        this.emitter = null;
        this.recievers = null;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }
}
