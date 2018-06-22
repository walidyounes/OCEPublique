/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Ordonnanceur;

import sma.infrastructure.EnumVitesse;
import sma.infrastructure.agent.Agent;

import java.util.List;

public interface IOrdonnanceur {

    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    void changerOrdonnancement(IStratOrdonnanceur stratOrdonnanceur);

    List<Agent> arreterOrdonnancement();

    void ordagentAjoute(Agent agent);

    void OrdagentRetire(Agent agent);


}
