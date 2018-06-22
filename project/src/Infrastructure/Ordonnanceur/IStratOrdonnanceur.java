/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import sma.infrastructure.EnumVitesse;
import sma.infrastructure.agent.Agent;
import sma.infrastructure.annuaire.IAgentListener;

import java.util.List;

public interface IStratOrdonnanceur extends IAgentListener {
    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    List<Agent> arreterOrdonnancement();

    void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener);

    public void agentAjoute(Agent agent);

    public void agentRetire(Agent agent);
}
