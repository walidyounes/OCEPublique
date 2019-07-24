/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;

public class ScoredCurrentSituationEntry extends CurrentSituationEntry {

    private double score; // The score of the agent "agent" in the current situation (initially equal to à or None, we affect the value in the scoring step)
    /**
     * Construct a new scored current situation entry
     *
     * @param agent       : the agent emitter of the message
     * @param messageType : the type of the message send
     * @param score       : the score of the agent
     */
    public ScoredCurrentSituationEntry(IDAgent agent, MessageTypes messageType, double score) {
        super(agent, messageType);
        this.score = score;
    }

    /**
     * Get the agent represented in this  entry
     * @return the reference of the agent represented by this  entry
     */
    @Override
    public IDAgent getAgent() {
        return super.agent;
    }

    /**
     * Set the agent represented in this entry
     * @param agent : the reference of the agent represented in this  entry
     */
    @Override
    public void setAgent(IDAgent agent) {
        setAgent(agent);
    }

    /**
     * Get the value of the score of the agent in the situation entry
     * @return the current value of the score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set the value of the score of the agent in the current situation
     * @param score : the new value of the score of the agent
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Test if two scored current situation entries are equals
     * @param obj : the other scored current situation entry to compare to this
     * @return : true if the two object are equals (the same agent and the same message type), else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScoredCurrentSituationEntry)) return false;

        ScoredCurrentSituationEntry that = (ScoredCurrentSituationEntry) obj;

        return this.agent.equals(that.agent) && this.messageType.equals(that.messageType) && this.score==that.score;
    }

    @Override
    public String toString() {
        return "( " + this.agent.toString() + " , " + this.messageType.toString() + " , " + this.score + ")";
    }
}
