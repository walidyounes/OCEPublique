/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Annuaire.IAgentListener;

import java.util.List;

public interface IStratOrdonnanceur extends IAgentListener {
    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    List<Agent> arreterOrdonnancement();

    void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener);

    void agentAjoute(Agent agent);

    void agentRetire(Agent agent);
}
