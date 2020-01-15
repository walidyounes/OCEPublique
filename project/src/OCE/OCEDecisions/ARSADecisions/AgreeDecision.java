/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEDecisions.ARSADecisions;

import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.OCEDecision;
import OCE.InfrastructureMessages.InfraARSAMessages.AgreeInfraMessage;
import OCE.InfrastructureMessages.BindInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent an agreement decision send by the agent when he accept the decision of binding
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeDecision extends OCEDecision {

    private BinderAgent binderAgent;

    /**
     * Create an agreement decision
     * @param emitter    reference of the agent that agrees
     * @param receivers the references of the receivers of the agreement
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
        OCELogger.log(Level.INFO, "Treating an agree decision ! ");
        //send the agree message
        AgreeInfraMessage agreeMessage = new AgreeInfraMessage(null, null);
        communicationAdapter.sendMessage(agreeMessage, this.emitter, this.receivers);
        //Send a message to the Binder Agent
        BindInfraMessage bindMessage = new BindInfraMessage(null, null);
        ArrayList<OCEAgent> bindReceiver = new ArrayList<>();
        bindReceiver.add(this.binderAgent);
        communicationAdapter.sendMessage(bindMessage, this.emitter, bindReceiver);
        // Change the state of the agent to "WAITING for feedback state"
        ((ServiceAgent)this.emitter).setMyConnexionState(ServiceAgentConnexionState.EXPECTING_FEEDBACK); // todo walid 13/12/2019 -> put the agent directly in WAITTING-Feedback state (we don't need that the binder agent sends a message)
    }

    @Override
    public String toString() {
        return "AgreeDecision{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}


