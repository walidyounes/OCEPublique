/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.services;

import Agent.Beans.Messages.AdMessage;
import OCPlateforme.OCService;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.HashMap;

/**
 * Interface interne du Medium.
 */
public interface IEnvAnnonce {

    /**
     * Envoi une annonce.
     *
     * @param refAgents map d'agents possible destinataire ainsi que leurs services
     * @param ann       annonce
     */
    void sendAnnonce(HashMap<ReferenceAgent, OCService> refAgents, AdMessage ann);
}
