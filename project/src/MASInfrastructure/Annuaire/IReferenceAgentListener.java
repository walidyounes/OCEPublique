/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.AgentReference;

public interface IReferenceAgentListener {

    void agentAjoute(AgentReference agentReference);

    void agentRetire(AgentReference agentReference);
}
