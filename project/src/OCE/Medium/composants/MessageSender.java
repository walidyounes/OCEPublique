/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */
package OCE.Medium.composants;

import Agent.Beans.Messages.AdMessage;
import OCPlateforme.OCService;
import medium.services.IEnvAnnonce;
import sma.infrastructure.agent.ReferenceAgent;
import sma.infrastructure.communication.ICommunication;
import unifieur.services.IMatching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MessageSender implements IEnvAnnonce {

    private IMatching matching;
    private ICommunication communication;

    // binding des services requis
    public void setMatching(IMatching matching) {
        this.matching = matching;
    }

    public void setCommunication(ICommunication communication) {
        this.communication = communication;
    }

    /**
     * Envoi l'annonce ann aux agents compatibles.
     *
     * @param agents liste d'agents possible destinataire ainsi que leurs services
     * @param ann    annonce
     */
    @Override
    public void sendAnnonce(HashMap<ReferenceAgent, OCService> agents, AdMessage ann) {

        Set cles = agents.keySet();
        Iterator it = cles.iterator();
        while (it.hasNext()) {
            ReferenceAgent agentDestinataire = (ReferenceAgent) it.next();
            OCService valeur = agents.get(agentDestinataire);

            if (match(ann, valeur)) {
                // communication.envoyerMessage(ann.getExpediteur(),agentDestinataire, ann);
                communication.envoyerMessage(ann);
                /** Trace
                 * 	System.out.println ("*******" +((MockupService)(agents.get(agentDestinataire))).getName());
                 */
            }
        }
    }

    /**
     * @param ann     annonces
     * @param service Service
     * @return boolean
     */
    private boolean match(AdMessage ann, OCService service) {

        return (matching.match(ann.getService(), service));
    }
}
