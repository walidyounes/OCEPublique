/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import OCE.BinderAgent;
import OCE.ServiceAgent;

/**
 * Agent Factory implementation : implements the functions in the IAFactory Interface to create different type of agent
 * @author Walid YOUNES
 * @version 1.0
 */
public class AFactory implements IAFactory {


    /**
     * create a service agent
     * @return the agent created
     */
    @Override
    public ServiceAgent createServiceAgent() {
        return null;
    }

    /**
     * create a binding agent
     * @return the binding agent
     */
    @Override
    public BinderAgent createBinderAgent() {
        return null;
    }
}
