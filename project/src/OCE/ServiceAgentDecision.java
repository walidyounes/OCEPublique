/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import MASInfrastructure.Communication.IMessage;
import Midlleware.ThreeState.IDecisionState;
import OCE.Selection.IMessageSelection;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentDecision implements IDecisionState {

    IMessageSelection selectionMessageStrategy;

    public ServiceAgentDecision(IMessageSelection selectionMessageStrategy) {
        this.selectionMessageStrategy = selectionMessageStrategy;
    }

    /**
     *  Impelment the decision mechanisme of the binder agent, and produce a list of decisions
     */
    @Override
    public void decide(ArrayList<IMessage> perceptions) {
        MyLogger.log(Level.INFO, "The service agent is making decisions !");
        //Call the selection method to select the messages to treat
        
    }
}
