/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.Map;

/**
 * InfrastructureAgent Factory Interface : offer a set of functions allowing to create different matchingID of agents
 * @author Walid YOUNES
 * @version 1.0
 */
public interface IOCEServiceAgentFactory {

    /**
     * Create an agent attached to a service
     * @return the service agent created and its InfrastructureReference
     */
    Map.Entry<ServiceAgent, InfraAgentReference> createServiceAgent(OCService attachedService);


}
