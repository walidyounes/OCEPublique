/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;


import OCE.Medium.Communication.ICommunicationAdapter;

/**
 * This class represent a suicide decision (i.e. the serivce that's attached to the agent is no longer avaible)
 * @author Walid YOUNES
 * @version 1.0
 */
public class SuicideDecision extends AbstractDecision {

    /**
     * Create a suicide decision
     * the emetteur is the sonde component and no receivers (the agent itself)
     */
    public SuicideDecision() {
        this.emitter= null;
        this.receivers = null;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }

    @Override
    public String toString() {
        return "SuicideDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
