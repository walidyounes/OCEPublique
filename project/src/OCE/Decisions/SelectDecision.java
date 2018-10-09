/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
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
        this.myBinderAgent = instanciateBinderAgent(((ServiceAgent)emitter).getMyBinderAgentFactory());
    }

    private BinderAgent instanciateBinderAgent(IOCEBinderAgentFactory binderAgentFactory){
        return binderAgentFactory.createBinderAgent().getKey();
    }
    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an agree decision ! ");
        SelectMessage selectMessage = new SelectMessage(null, null, this.myBinderAgent.getMyInfraAgent().getInfraAgentReference());
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
