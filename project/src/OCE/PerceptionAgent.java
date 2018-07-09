/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import Midlleware.ThreeState.IPerceptionState;

import java.util.logging.Level;

/**
 * This class represent one way hwo we can perceive the environnement, it's used both by the ServiceAgent and the BindingAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class PerceptionAgent implements IPerceptionState {

    /**
     * Implement the perception process of the agent, which consist in reading the received messages
     */
    @Override
    public void percept() {
        MyLogger.log(Level.CONFIG, "The service agent is percepting the envirnment !");
    }
}
