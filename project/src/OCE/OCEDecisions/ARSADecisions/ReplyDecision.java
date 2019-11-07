/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEDecisions.ARSADecisions;

import Logger.OCELogger;
import OCE.OCEDecisions.OCEDecision;
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
     * @param receivers the references of the receivers of the response
     */
    public ReplyDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        OCELogger.log(Level.INFO, "Treating a reply decision ! ");
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
