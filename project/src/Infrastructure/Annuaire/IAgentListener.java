/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import Infrastructure.Agent.Agent;

public interface IAgentListener {

    void agentAjoute(Agent agent);

    void agentRetire(Agent agent);
}
