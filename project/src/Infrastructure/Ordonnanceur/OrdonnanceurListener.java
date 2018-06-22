/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import sma.infrastructure.agent.ReferenceAgent;
import sma.infrastructure.etat.IEtat;

public interface OrdonnanceurListener {

    void changementEtat(ReferenceAgent referenceAgent, IEtat etatAbstract);
}
