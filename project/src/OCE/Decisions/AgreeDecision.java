/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import MASInfrastructure.Agent.InfraAgentReference;

import java.util.ArrayList;

/**
 * This class represent an agreement decision send by the agent when he accept the decision of binding
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeDecision extends AbstractDecision {

    /**
     * Create an agreement decision
     * @param emitter    reference of the agent that agrees
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AgreeDecision(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }
}


