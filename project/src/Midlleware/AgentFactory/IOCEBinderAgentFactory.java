/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.Map;

public interface IOCEBinderAgentFactory {

    /**
     * Create a binder agent
     * @return the binder agent created and its InfrastructureReference
     */
    Map.Entry<BinderAgent, InfraAgentReference> createBinderAgent();

}
