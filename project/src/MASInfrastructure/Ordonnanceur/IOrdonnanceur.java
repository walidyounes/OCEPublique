/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.InfraAgent;


import java.util.List;

public interface IOrdonnanceur {

    void ordonnancer();

    void changerVitesse(EnumVitesse vitesse);

    void changerOrdonnancement(IStratOrdonnanceur stratOrdonnanceur);

    List<InfraAgent> arreterOrdonnancement();

    void ordagentAjoute(InfraAgent infraAgent);

    void OrdagentRetire(InfraAgent infraAgent);


}
