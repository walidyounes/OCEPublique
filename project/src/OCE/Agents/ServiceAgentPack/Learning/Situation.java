/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class implementing the situation (current or reference) of a service agent
 */
public class Situation {

    private Map<ServiceAgent, SituationEntry> mySituation; // the situation representing the current environment

    /**
     * Create a new situation
     */
    public Situation() {
        this.mySituation = new TreeMap<>();
    }

    public Situation(Map<ServiceAgent, SituationEntry> mySituation) {
        this.mySituation = mySituation;
    }

}
