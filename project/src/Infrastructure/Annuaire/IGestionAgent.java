/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import sma.infrastructure.agent.Agent;
import sma.infrastructure.agent.ReferenceAgent;

public interface IGestionAgent {

    void addAgent(Agent agent);

    void removeAgent(ReferenceAgent referenceAgent);
}
