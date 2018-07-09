/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import Midlleware.ThreeState.IActionState;

import java.util.logging.Level;

/**
 * This class implements the actions mechanism of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ActionAgentService implements IActionState {

    /**
     * Execute the taken decisions and affect the environement acordingly
     */
    @Override
    public void act() {
        MyLogger.log(Level.CONFIG, "The service agent is acting upon the environment !");
    }
}
