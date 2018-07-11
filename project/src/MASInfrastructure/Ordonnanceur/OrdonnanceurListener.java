/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Etat.IEtat;


public interface OrdonnanceurListener {

    void changementEtat(AgentReference agentReference, IEtat etatAbstract);
}
