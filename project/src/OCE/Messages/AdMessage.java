/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import Agent.Beans.Perceptions.AbstractPerception;
import Agent.Beans.Perceptions.AdPerception;
import OCPlateforme.OCService;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.ArrayList;

;


public class AdMessage extends MessageAgent {

    /**
     * Creer une annonce
     *
     * @param service       service de l'agent annonceur
     * @param expediteur    reference de l'agent annonceur
     * @param destinataires les références des agents destinataires. Si == Null, alors diffusion en Broadcast
     */
    public AdMessage(OCService service, ReferenceAgent expediteur, ArrayList<ReferenceAgent> destinataires) {
        this.service = service;
        this.expediteur = expediteur;
        this.destinataires = destinataires;
    }

    @Override
    public ReferenceAgent getExpediteur() {
        return this.expediteur;
    }

    @Override
    public OCService getService() {
        return this.service;
    }

    @Override
    public ArrayList<ReferenceAgent> getDestinataires() {
        return this.destinataires;
    }

    @Override
    public AbstractPerception toPerception() {
        return new AdPerception(this);
    }

    @Override
    public String toString() {
        return "AdMessage{" +
                "service=" + service +
                ", expediteur=" + expediteur +
                ", destinataires=" + destinataires +
                '}';
    }
}
