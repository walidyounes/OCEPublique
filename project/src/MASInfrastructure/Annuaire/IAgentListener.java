/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.Agent;

public interface IAgentListener {

    void agentAjoute(Agent agent);

    void agentRetire(Agent agent);
}
