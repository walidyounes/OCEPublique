/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import sma.infrastructure.EnumVitesse;
import sma.infrastructure.agent.Agent;

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
    public List<Agent> arreterOrdonnancement() {
        return stratOrdonnanceur.arreterOrdonnancement();
    }

    @Override
    public void ordagentAjoute(Agent agent) {
        stratOrdonnanceur.agentAjoute(agent);
    }

    public IStratOrdonnanceur getStratOrdonnanceur() {
        return stratOrdonnanceur;
    }

    @Override
    public void OrdagentRetire(Agent agent) {
        stratOrdonnanceur.agentRetire(agent);
    }
}
