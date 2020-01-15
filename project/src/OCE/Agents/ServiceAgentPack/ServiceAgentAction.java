/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.OCELogger;
import Midlleware.ThreeState.IActionState;
import OCE.Agents.OCEAgent;
import OCE.OCEDecisions.OCEDecision;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ARSAStrategies.Advertise.IAdvertiseStrategy;
import OCE.ARSAStrategies.Agree.IAgreeStrategy;
import OCE.ARSAStrategies.Reply.IReplyStrategy;
import OCE.ARSAStrategies.Select.ISelectStrategy;

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
     * Update the communication component
     * @param communicationManager : the component which is in charge of the communication between the agent
     */
    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }

    /**
     * Execute the taken decisions and affect the environment accordingly
     */
    @Override
    public void act(ArrayList<OCEDecision> decisionsList) {
        OCELogger.log(Level.INFO, "Agent : Action");
        for (OCEDecision decision : decisionsList) {
            decision.toSelfTreat(communicationManager);
        }
    }

    @Override
    public void setBinderAgent(OCEAgent myBinderAgent) {
        //Walid : this is only for test, i need this function for the binder Agent and the feedback message
    }
}
