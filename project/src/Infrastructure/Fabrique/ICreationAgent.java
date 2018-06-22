/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Fabrique;

import sma.infrastructure.agent.ReferenceAgent;
import sma.infrastructure.etat.IEtat;

public interface ICreationAgent {

    ReferenceAgent creer(IEtat etatInit);

}
