/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import Midlleware.ThreeState.IActionState;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Strategies.Advertise.IAdvertiseStrategy;
import OCE.Strategies.Agree.IAgreeStrategy;
import OCE.Strategies.Reply.IReplyStrategy;
import OCE.Strategies.Select.ISelectStrategy;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class implements the actions mechanism of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentAction implements IActionState {

    private ICommunicationAdapter communicationManager;
    //The differentStrategies
    private IAdvertiseStrategy myAdvertiseStrategy;
    private IReplyStrategy myReplyStrategy;
    private ISelectStrategy mySelectStrategy;
    private IAgreeStrategy myAgreeStrategy;

    /**
     * Update the communication componennt
     * @param communicationManager : the componenent whic is in charge of the communication between the agent
     */
    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }

    /**
     * Execute the taken decisions and affect the environement acordingly
     */
    @Override
    public void act(ArrayList<AbstractDecision> decisionsList) {
        MyLogger.log(Level.INFO, "The service agent is acting upon the environment !");
        for (AbstractDecision decision : decisionsList) {
            decision.toSelfTreat(communicationManager);
        }
    }
}
