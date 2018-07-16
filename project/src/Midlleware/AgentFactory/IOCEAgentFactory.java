/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.BinderAgent;
import OCE.ServiceAgent;

import java.util.Map;

/**
 * InfraAgent Factory Interface : offer a set of functions allowing to create different type of agents
 * @author Walid YOUNES
 * @version 1.0
 */
public interface IOCEAgentFactory {

    /**
     * Create an agent attached to a service
     * @return the service agent created
     */
    Map.Entry<ServiceAgent, InfraAgentReference> createServiceAgent(OCService attachedService);

    /**
     * Create a binding agent
     * @return the binding agent created
     */
    BinderAgent createBinderAgent();

}
