/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

public class ReferenceSituationEntry extends SituationEntry {

    private double score; // The score of the agent "agent" in the reference situation stored in the agent knowledge base

    /**
     * Construct a new scored current situation entry
     * @param agent : the reference of the agent
     * @param score : the value of the score of the agent
     */
    public ReferenceSituationEntry(IDAgent agent, double score) {
        this.agent = agent;
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
     * Test if two reference situation entries are equals
     * @param obj : the other reference situation entry to compare to this
     * @return : true if the two object are equals (the same agent and the same score value), else false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReferenceSituationEntry)) return false;

        ReferenceSituationEntry that = (ReferenceSituationEntry) obj;

        return this.agent.equals(that.agent) && this.score == that.score;
    }

    @Override
    public String toString() {
        return "( " + this.agent.toString() + " , " + this.score + ")";
    }
}
