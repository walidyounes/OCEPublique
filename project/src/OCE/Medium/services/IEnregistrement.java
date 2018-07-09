/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.services;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;


/**
 * Interface Enregistrement. Service fourni par le Medium.
 * Permet à la Sonde de signaler au Medium l'arriver
 * ou la disparition d'un agent.
 */
public interface IEnregistrement {

    /**
     * Signale l'arrivé d'un agent.
     *
     * @param RefAgent nouvel agent
     * @param service  service de l'agent
     */
    void addAgent(ReferenceAgent RefAgent, OCService service);

    /**
     * Signale la disparition d'un agent.
     *
     * @param RefAgent agent disparu
     */
    void removeAgent(ReferenceAgent RefAgent);

}
