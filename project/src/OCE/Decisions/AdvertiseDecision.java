/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import MASInfrastructure.Agent.InfraAgentReference;

import java.util.ArrayList;

/**
 * This class represent an advertisement decision (i.e. send an advertisement message -usualy in boradcast-)
 * @author Walid YOUNES
 * @version 1.0
 */
public class AdvertiseDecision extends AbstractDecision {

    /**
     * Create an advertise decision
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AdvertiseDecision(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }
}
