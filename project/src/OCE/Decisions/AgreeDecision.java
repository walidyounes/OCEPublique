/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.AgreeMessage;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent an agreement decision send by the agent when he accept the decision of binding
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeDecision extends AbstractDecision {

    /**
     * Create an agreement decision
     * @param emitter    reference of the agent that agrees
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public AgreeDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an agree decision ! ");
        AgreeMessage agreeMessage = new AgreeMessage(null, null);
        communicationAdapter.sendMessage(agreeMessage, this.emitter, this.receivers);
    }

    @Override
    public String toString() {
        return "AgreeDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}


