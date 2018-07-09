/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Fabrique;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.Agent;
import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

public interface ICreationAgent {

    Agent creer(OCService attachedService, LifeCyrcle lifeCyrcle);

}
