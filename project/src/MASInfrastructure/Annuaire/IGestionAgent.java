/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;


public interface IGestionAgent {

    void addAgent(InfraAgent infraAgent);

    void removeAgent(InfraAgentReference infraAgentReference);
}
