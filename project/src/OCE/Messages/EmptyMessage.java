/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;

import java.util.logging.Level;

public class EmptyMessage extends Message {


    /**
     * Create an empty message
     */
    public EmptyMessage() {
        this.emitter = null;
        this.recievers = null;
    }

    @Override
    public AbstractDecision toSelfTreat() {
        MyLogger.log(Level.INFO, "Treating an empty message ! ");
        return null;
    }
}
