/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Medium.Communication.ICommunicationAdapter;

public class BindingDecision extends AbstractDecision {

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }

    @Override
    public String toString() {
        return "BindingDecision{" +
                "emitter=" + emitter +
                ", recievers=" + recievers +
                '}';
    }
}
