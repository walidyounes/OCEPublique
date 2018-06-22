/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import Infrastructure.Agent.Agent;
import Infrastructure.Agent.ReferenceAgent;


public interface IGestionAgent {

    void addAgent(Agent agent);

    void removeAgent(ReferenceAgent referenceAgent);
}
