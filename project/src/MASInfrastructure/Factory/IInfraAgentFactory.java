/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Factory;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.State.LifeCycle;

public interface IInfraAgentFactory {

    InfrastructureAgent createInfrastructureAgent(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager);

}
