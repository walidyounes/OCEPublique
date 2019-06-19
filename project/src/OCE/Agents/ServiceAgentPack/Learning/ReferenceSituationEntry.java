/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;

public class ReferenceSituationEntry extends SituationEntry {

    private float score; // The score of the agent "agent" in the reference situation stored in the agent knowledge base

    /**
     * Construct a new scored current situation entry
     * @param agent : the reference of the agent
     * @param score : the value of the score of the agent
     */
    public ReferenceSituationEntry(ServiceAgent agent, float score) {
        this.agent = agent;
        this.score = score;
    }

    /**
     * Get the agent represented in this  entry
     * @return the reference of the agent represented by this  entry
     */
    @Override
    public ServiceAgent getAgent() {
        return null;
    }

    /**
     * Set the agent represented in this entry
     * @param agent : the reference of the agent represented in this  entry
     */
    @Override
    public void setAgent(ServiceAgent agent) {

    }
}
