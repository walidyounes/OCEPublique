/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.ResponseMessage;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent a reply decision (i.e. send a reply message to the agent who send the ad)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyDecision extends AbstractDecision {

    /**
     * Create a reply decision
     * @param emitter    reference of the replying agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public ReplyDecision(ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an reply decision ! ");
        ResponseMessage responseMessage = new ResponseMessage(null, null );
        communicationAdapter.sendMessage(responseMessage, this.emitter, this.recievers);
    }

    @Override
    public String toString() {
        return "ReplyDecision{" +
                "emitter=" + emitter +
                ", recievers=" + recievers +
                '}';
    }
}
