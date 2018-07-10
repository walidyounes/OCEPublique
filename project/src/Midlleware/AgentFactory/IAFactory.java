/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;
import OCE.BinderAgent;
import OCE.ServiceAgent;

import java.util.Map;

/**
 * Agent Factory Interface : offer a set of functions allowing to create different type of agents
 * @author Walid YOUNES
 * @version 1.0
 */
public interface IAFactory {

    /**
     * Create an agent attached to a service
     * @return the service agent created
     */
    Map.Entry<ServiceAgent, ReferenceAgent> createServiceAgent(OCService attachedService);

    /**
     * Create a binding agent
     * @return the binding agent created
     */
    BinderAgent createBinderAgent();

}
