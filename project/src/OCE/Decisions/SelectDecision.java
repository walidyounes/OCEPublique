/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import Logger.MyLogger;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.InfrastructureMessages.BindInfraMessage;
import OCE.InfrastructureMessages.InfraARSAMessages.SelectInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent a selection decision (i.e. send a selection message to an agent to inform hi that it has been choosed as suitable connexion)
 * @author Walid YOUNES
 * @version 1.0
 */
public class SelectDecision extends OCEDecision {
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
        SelectInfraMessage selectMessage = new SelectInfraMessage(null, null, this.myBinderAgent.getMyInfraAgent().getInfraAgentReference());
        communicationAdapter.sendMessage(selectMessage, this.emitter, this.receivers);
        // Send a message to the binder agent
        BindInfraMessage bindMessage = new BindInfraMessage(null, null);
        ArrayList<OCEAgent> bindReceivers = new ArrayList<>();
        bindReceivers.add(this.myBinderAgent);
        communicationAdapter.sendMessage(bindMessage, this.emitter,bindReceivers);
        // Change the state of the agent to "Waiting state" //Todo: I put connected, change it to wainting when implementing the replyMessage from the binder
        ((ServiceAgent)this.emitter).setMyConnexionState(ServiceAgentConnexionState.Waiting);
        //Set the selectedAgent ot be verified later
        this.emitter.setMySelectedAgent(this.receivers.get(0));
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
