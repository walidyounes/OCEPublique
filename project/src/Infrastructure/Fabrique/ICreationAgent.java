/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Fabrique;

import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Etat.IEtat;

public interface ICreationAgent {

    ReferenceAgent creer(IEtat etatInit);

}
