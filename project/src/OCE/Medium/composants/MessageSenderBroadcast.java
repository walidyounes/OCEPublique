/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */
package OCE.Medium.composants;

import Agent.Beans.Messages.AdMessage;
import OCPlateforme.OCService;
import medium.services.IEnvAnnonce;
import sma.infrastructure.agent.ReferenceAgent;
import sma.infrastructure.communication.ICommunication;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderBroadcast implements IEnvAnnonce {

    // private IMatching matching;
    private ICommunication communication;

    /**
     * Envoi l'annonce "ann" à tous les agents de la map.
     *
     * @param agents map d'agents possible destinataire ainsi que leurs services
     * @param ann    annonce
     */
    @Override
    public void sendAnnonce(HashMap<ReferenceAgent, OCService> agents, AdMessage ann) {
        for (Map.Entry<ReferenceAgent, OCService> agent : agents.entrySet()) {
            communication.diffuserMessage(ann);

        }
    }
}
