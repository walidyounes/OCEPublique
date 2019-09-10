/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Annuaire.IAgentListener;

import java.util.List;

public interface IStratOrdonnanceur extends IAgentListener {
    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    List<InfraAgent> arreterOrdonnancement();

    void pauseOrdonnancement();

    void repriseOrdonnancement();

    void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener);

    void agentAjoute(InfraAgent infraAgent);

    void agentRetire(InfraAgent infraAgent);
}
