/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;


public interface IGestionAgent {

    void addAgent(Agent agent);

    void removeAgent(AgentReference agentReference);
}
