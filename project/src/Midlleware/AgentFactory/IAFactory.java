/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import OCE.BinderAgent;
import OCE.ServiceAgent;

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
    ServiceAgent createServiceAgent();

    /**
     * Create a binding agent
     * @return the binding agent created
     */
    BinderAgent createBinderAgent();

}
