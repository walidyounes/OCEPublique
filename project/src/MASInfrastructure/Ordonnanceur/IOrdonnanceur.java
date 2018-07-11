/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.Agent;


import java.util.List;

public interface IOrdonnanceur {

    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    void changerOrdonnancement(IStratOrdonnanceur stratOrdonnanceur);

    List<Agent> arreterOrdonnancement();

    void ordagentAjoute(Agent agent);

    void OrdagentRetire(Agent agent);


}
