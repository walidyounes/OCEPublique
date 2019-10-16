/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;


public interface IAgentDirectoryManager {

    void addAgent(InfraAgent infraAgent);

    void removeAgent(InfraAgentReference infraAgentReference);
}
