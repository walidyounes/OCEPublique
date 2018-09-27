/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.SelectMessage;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent a selection decision (i.e. send a selection message to an agent to inform hi that it has been choosed as suitable connexion)
 * @author Walid YOUNES
 * @version 1.0
 */
public class SelectDecision extends AbstractDecision {
    private BinderAgent myBinderAgent;

    /**
     * Create a selection decision
     * @param emitter    reference of the selecting agent
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public SelectDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an agree decision ! ");
        SelectMessage selectMessage = new SelectMessage(null, null, null);
        communicationAdapter.sendMessage(selectMessage, this.emitter, this.receivers);
    }

    @Override
    public String toString() {
        return "SelectDecision{" +
                "myBinderAgent=" + myBinderAgent +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
