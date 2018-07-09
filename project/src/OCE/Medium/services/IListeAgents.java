/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.services;

import OCPlateforme.OCService;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.HashMap;

/**
 * Interface interne du Medium.
 */
public interface IListeAgents {

    /**
     * Retourne une map d'agents associé à leurs services.
     *
     * @return liste d'agents et services
     */
    HashMap<ReferenceAgent, OCService> getListAgents();
}
