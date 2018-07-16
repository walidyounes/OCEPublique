/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Fabrique;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Etat.LifeCycle;

public interface IInfraAgentFactory {

    InfraAgent creer(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager);

}
