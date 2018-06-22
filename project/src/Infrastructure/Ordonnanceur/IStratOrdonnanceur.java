/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import Infrastructure.Agent.Agent;
import Infrastructure.Annuaire.IAgentListener;

import java.util.List;

public interface IStratOrdonnanceur extends IAgentListener {
    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    List<Agent> arreterOrdonnancement();

    void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener);

    void agentAjoute(Agent agent);

    void agentRetire(Agent agent);
}
