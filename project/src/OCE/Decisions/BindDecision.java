/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Agents.OCEAgent;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;

public class BindDecision extends OCEDecision {

    /**
     * Create a Bind decision
     * @param emitter    reference of the agent that agrees
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public BindDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }

    @Override
    public String toString() {
        return "BindDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
