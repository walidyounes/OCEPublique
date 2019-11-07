/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Agent.InfraAgentReference;


public interface IAgentDirectoryManager {

    void addAgent(InfrastructureAgent infrastructureAgent);

    void removeAgent(InfraAgentReference infraAgentReference);
}
