/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages.InfraARSAMessages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.ARSAMessages.SelectMessage;

import java.util.ArrayList;

/**
 * This class represents the select message (Infrastructure level) sent in the third step of the ARSA protocol
 * @author Walid YOUNES
 * @version 1.0
 */
public class SelectInfraMessage extends ARSAInfraMessage {
    private InfraAgentReference binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param recievers the references of the receivers of the selection message
     * @param bindingAgent the agent responsible of executing the physical binding
     */
    public SelectInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers, InfraAgentReference bindingAgent) {
        this.emitter = emitter;
        this.receivers = recievers;
        this.binderAgent = bindingAgent;
        this.myType = MessageTypes.SELECT;
    }

    /**
     * create a Selection message (empty)
     */
    public SelectInfraMessage() {
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
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a selection message ! ");
        return null;
    }*/

    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        try {
            return new SelectMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), (BinderAgent) referenceResolver.retrieveOCEAgentByInfraAgentReference(this.binderAgent));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    /**
     * Get the type of the message
     * @return : the type of this message
     */
    @Override
    public MessageTypes getMyType() {
        return this.myType;
    }

    @Override
    public String toString() {
        return "SelectInfraMessage{" +
                "binderAgent=" + binderAgent +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
