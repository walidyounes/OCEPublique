/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;

public class CurrentSituationEntry extends SituationEntry {

    protected MessageTypes messageType; // The type of the message send by the agent "agent"


    /**
     * Construct a new current situation entry
     *
     * @param agent       : the agent emitter of the message
     * @param messageType : the type of the message send
     */
    public CurrentSituationEntry(ServiceAgent agent, MessageTypes messageType) {
       this.agent = agent;
       this.messageType = messageType;
    }

    /**
     * Get the agent represented in this  entry
     * @return the reference of the agent represented by this  entry
     */
    @Override
    public ServiceAgent getAgent() {
        return this.agent;
    }

    /**
     * Set the agent represented in this entry
     * @param agent : the reference of the agent represented in this  entry
     */
    @Override
    public void setAgent(ServiceAgent agent) {
        this.agent = agent;
    }

    /**
     * Get the type of the message sent by the agent represented in the entry
     * @return the type of the message sent by the agent represented in the entry
     */
    public MessageTypes getMessageType() {
        return messageType;
    }

    /**
     * Set the type of the message sent by the agent represented in the entry
     * @param messageType : the type of the message sent by the agent represented in the entry
     */
    public void setMessageType(MessageTypes messageType) {
        this.messageType = messageType;
    }
}
