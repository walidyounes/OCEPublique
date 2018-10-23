/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.ReplyPerception;
import OCE.Perceptions.SelectPerception;

import java.util.ArrayList;

public class SelectMessage extends Message {
    private InfraAgentReference binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param recievers the references of the receivers of the selection message
     * @param bindingAgent the agent responsible of executing the physical binding
     */
    public SelectMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers, InfraAgentReference bindingAgent) {
        this.emitter = emitter;
        this.receivers = recievers;
        this.binderAgent = bindingAgent;
    }

    /**
     * create a Selection message (empty)
     */
    public SelectMessage() {
        this.emitter = null;
        this.receivers = null;
        this.binderAgent = null;
    }

    /**
     * get the reference of the binding agent
     * @return the reference
     */
    public InfraAgentReference getBinderAgent() {
        return binderAgent;
    }

    /**
     * set the reference of the binding agent
     * @param binderAgent : the reference of the binding agent
     */
    public void setBinderAgent(InfraAgentReference binderAgent) {
        this.binderAgent = binderAgent;
    }

/*
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a selection message ! ");
        return null;
    }*/

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new SelectPerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), (BinderAgent) referenceResolver.retrieveOCEAgentByInfraAgentReference(this.binderAgent));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "SelectMessage{" +
                "binderAgent=" + binderAgent +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
