/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.InfraAgent;

public interface IAgentListener {

    void agentAjoute(InfraAgent infraAgent);

    void agentRetire(InfraAgent infraAgent);
}
