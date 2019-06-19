/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;

public class ScoredCurrentSituation extends CurrentSituationEntry {

    private float score; // The score of the agent "agent" in the current situation (initially equal to à or None, we affect the value in the scoring step)
    /**
     * Construct a new scored current situation entry
     *
     * @param agent       : the agent emitter of the message
     * @param messageType : the type of the message send
     * @param score       : the score of the agent
     */
    public ScoredCurrentSituation(ServiceAgent agent, MessageTypes messageType, float score) {
        super(agent, messageType);
        this.score = score;
    }

    /**
     * Get the value of the score of the agent in the situation entry
     * @return the current value of the score
     */
    public float getScore() {
        return score;
    }

    /**
     * Set the value of the score of the agent in the current situation
     * @param score : the new value of the score of the agent
     */
    public void setScore(float score) {
        this.score = score;
    }
}
