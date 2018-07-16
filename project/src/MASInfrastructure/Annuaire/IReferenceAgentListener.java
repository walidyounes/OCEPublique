/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.InfraAgentReference;

public interface IReferenceAgentListener {

    void agentAjoute(InfraAgentReference infraAgentReference);

    void agentRetire(InfraAgentReference infraAgentReference);
}
