/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import Infrastructure.Agent.ReferenceAgent;

public interface IReferenceAgentListener {

    void agentAjoute(ReferenceAgent referenceAgent);

    void agentRetire(ReferenceAgent referenceAgent);
}
