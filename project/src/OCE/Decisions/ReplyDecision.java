/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceAgent;

import java.util.ArrayList;

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

    }
}
