/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfrastructureAgent;

public interface IAgentListener {

    void agentAjoute(InfrastructureAgent infrastructureAgent);

    void agentRetire(InfrastructureAgent infrastructureAgent);
}
