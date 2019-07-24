/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

/**
 * This class represent an entry of the service agent situation
 */
public abstract class SituationEntry {

    protected IDAgent agent; // The agent emitter of the message

    /**
     * Get the agent represented in this  entry
     * @return the reference of the agent represented by this  entry
     */
    public abstract IDAgent getAgent();


    /**
     * Set the agent represented in this entry
     * @param agent : the reference of the agent represented in this  entry
     */
    public abstract void setAgent(IDAgent agent);


}
