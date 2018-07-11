/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;

import java.util.logging.Level;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class DecisionServiceAgent implements IDecisionState {

    /**
     *  Impelment the decision mechanisme of the binder agent, and produce a list of decisions
     */
    @Override
    public void decide() {
        MyLogger.log(Level.CONFIG, "The service agent is making decisions !");
    }
}
