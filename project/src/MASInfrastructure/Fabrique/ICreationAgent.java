/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Fabrique;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Etat.LifeCycle;

public interface ICreationAgent {

    Agent creer(OCService attachedService, LifeCycle lifeCycle);

}
