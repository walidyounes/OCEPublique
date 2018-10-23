/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.AgreeMessage;
import OCE.Messages.BindMessage;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent an agreement decision send by the agent when he accept the decision of binding
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeDecision extends AbstractDecision {

    private BinderAgent binderAgent;
    /**
     * Create an agreement decision
     * @param emitter    reference of the agent that agrees
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public AgreeDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    public void setBinderAgent(BinderAgent binderAgent) {
        this.binderAgent = binderAgent;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an agree decision ! ");
        //send the agree message
        AgreeMessage agreeMessage = new AgreeMessage(null, null);
        communicationAdapter.sendMessage(agreeMessage, this.emitter, this.receivers);
        //Send a message to the Binder Agent
        BindMessage bindMessage = new BindMessage(null, null);
        ArrayList<OCEAgent> bindReceiver = new ArrayList<>();
        bindReceiver.add(this.binderAgent);
        communicationAdapter.sendMessage(bindMessage, this.emitter, bindReceiver);

    }

    @Override
    public String toString() {
        return "AgreeDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}


