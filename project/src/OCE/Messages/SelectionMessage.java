/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.InfraAgentReference;

import java.util.ArrayList;

public class SelectionMessage extends Message {
    private InfraAgentReference binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param recievers the references of the recievers of the selection message
     * @param bindingAgent the agent responsible of executing the physical binding
     */
    public SelectionMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers, InfraAgentReference bindingAgent) {
        this.emitter = emitter;
        this.recievers = recievers;
        this.binderAgent = bindingAgent;
    }

    /**
     * create a Selection message (empty)
     */
    public SelectionMessage() {
        this.emitter = null;
        this.recievers= null;
        this.binderAgent = null;
    }

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */
    @Override
    public InfraAgentReference getEmitter() {
        return this.emitter;
    }

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    @Override
    public void setEmitter(InfraAgentReference emitter) {
        this.emitter = emitter;
    }

    /**
     * get the list of the recievers of the message
     * @return the recievers of the message
     */
    @Override
    public ArrayList<InfraAgentReference> getRecievers() {
        return this.recievers;
    }

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    @Override
    public void setRecievers(ArrayList<InfraAgentReference> recievers) {
        this.recievers = recievers;
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


}
