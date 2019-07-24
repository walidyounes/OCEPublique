/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
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
    public CurrentSituationEntry(IDAgent agent, MessageTypes messageType) {
        this.agent = agent;
        this.messageType = messageType;
    }

    /**
     * Get the agent represented in this  entry
     * @return the reference of the agent represented by this  entry
     */
    @Override
    public IDAgent getAgent() {
        return this.agent;
    }


    /**
     * Set the agent represented in this entry
     * @param agent : the reference of the agent represented in this  entry
     */
    @Override
    public void setAgent(IDAgent agent) {
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

    /**
     * Test if two current situation entries are equals
     * @param obj : the other current situation entry to compare to this
     * @return : true if the two object are equals (the same agent and the same message type), else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CurrentSituationEntry)) return false;

        CurrentSituationEntry that = (CurrentSituationEntry) obj;

        return this.agent.equals(that.agent) && this.messageType.equals(that.messageType);
    }

    @Override
    public String toString() {
        return "( " + this.agent.toString() + " , " + this.messageType.toString() + ")";
    }
}
