/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import OCE.InfrastructureMessages.InfraARSAMessages.ReplyInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent a reply decision (i.e. send a reply message to the agent who send the ad)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyDecision extends OCEDecision {

    /**
     * Create a reply decision
     * @param emitter    reference of the replying agent
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public ReplyDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an reply decision ! ");
        ReplyInfraMessage replyMessage = new ReplyInfraMessage(null, null );
        communicationAdapter.sendMessage(replyMessage, this.emitter, this.receivers);
    }

    @Override
    public String toString() {
        return "ReplyDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
