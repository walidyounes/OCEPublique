/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Etat.IEtat;


public interface OrdonnanceurListener {

    void changementEtat(ReferenceAgent referenceAgent, IEtat etatAbstract);
}
