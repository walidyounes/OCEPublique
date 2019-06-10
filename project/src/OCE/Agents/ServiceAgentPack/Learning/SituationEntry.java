/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Messages.MessageTypes;

/**
 * This class represent a 3-uplet representing a service agent in a situation
 */
public class SituationEntry {
    private ServiceAgent agent; // The agent emitter of the message
    private MessageTypes messageType; // The type of the message send by the agent "agent"
    private float score; // The score of the agent "agent" in the current situation (initially equal to à or None, we affect the value in the scoring step)

    /**
     * Construct a new situation entry
     * @param agent : the agent emitter of the message
     * @param messageType : the type of the message send
     */
    public SituationEntry(ServiceAgent agent, MessageTypes messageType) {
        this.agent = agent;
        this.messageType = messageType;
    }

    public ServiceAgent getAgent() {
        return agent;
    }

    public void setAgent(ServiceAgent agent) {
        this.agent = agent;
    }

    public MessageTypes getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypes messageType) {
        this.messageType = messageType;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
