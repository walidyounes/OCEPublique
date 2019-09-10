/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.InfraAgent;


import java.util.List;

public class Ordonnanceur implements IOrdonnanceur {

    private IStratOrdonnanceur stratOrdonnanceur;

    public Ordonnanceur(IStratOrdonnanceur stratOrdonnanceur) {
        this.stratOrdonnanceur = stratOrdonnanceur;
    }

    @Override
    public void ordonnancer() {
        stratOrdonnanceur.ordonnancer();
    }

    @Override
    public void changerVitesse(EnumVitesse vitesse) {
        stratOrdonnanceur.changerVitesse(vitesse);
    }

    @Override
    public void changerOrdonnancement(IStratOrdonnanceur stratOrdonnanceur) {
        this.stratOrdonnanceur = stratOrdonnanceur;
    }

    @Override
    public List<InfraAgent> arreterOrdonnancement() {
        return stratOrdonnanceur.arreterOrdonnancement();
    }

    @Override
    public void ordagentAjoute(InfraAgent infraAgent) {
        stratOrdonnanceur.agentAjoute(infraAgent);
    }

    public IStratOrdonnanceur getStratOrdonnanceur() {
        return stratOrdonnanceur;
    }

    @Override
    public void OrdagentRetire(InfraAgent infraAgent) {
        stratOrdonnanceur.agentRetire(infraAgent);
    }


    /**
     * Put pause to the scheduling process of the agents
     */
    @Override
    public void pauseOrdonnancement() {
        this.stratOrdonnanceur.pauseOrdonnancement();
    }

    /**
     * Resume the execution of the scheduling process of the agents
     */
    @Override
    public void repriseOrdonnancement() {
        this.stratOrdonnanceur.repriseOrdonnancement();
    }
}
