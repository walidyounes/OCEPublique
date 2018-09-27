/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

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

public class BinderAgentAction implements IActionState {

    private ICommunicationAdapter communicationManager;

    /**
     * Update the communication componennt
     * @param communicationManager : the componenent whic is in charge of the communication between the agent
     */
    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }
    @Override
    public void act(ArrayList<AbstractDecision> decisionsList) {
        MyLogger.log(Level.INFO, "The Binder agent is acting upon the environment !");
        for (AbstractDecision decision : decisionsList) {
            decision.toSelfTreat(communicationManager);
        }
    }

}
