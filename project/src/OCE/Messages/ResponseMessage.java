/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import Agent.Beans.Perceptions.AbstractPerception;
import Agent.Beans.Perceptions.ResponsePerception;
import OCPlateforme.OCService;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.ArrayList;


public class ResponseMessage extends MessageAgent {

    /**
     * Creer une réponse
     *
     * @param service       service de l'agent qui répond
     * @param expediteur    reference de l'agent qui répond
     * @param destinataires les references des agents destinataires. Si == Null, alors diffusion en Broadcast
     */
    public ResponseMessage(OCService service, ReferenceAgent expediteur, ArrayList<ReferenceAgent> destinataires) {
        this.service = service;
        this.expediteur = expediteur;
        this.destinataires = destinataires;
    }

    @Override
    public OCService getService() {
        return this.service;
    }

    @Override
    public ReferenceAgent getExpediteur() {
        return this.expediteur;
    }

    @Override
    public AbstractPerception toPerception() {
        return new ResponsePerception(this);
    }

    @Override
    public ArrayList<ReferenceAgent> getDestinataires() {
        return this.destinataires;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "service=" + service +
                ", expediteur=" + expediteur +
                ", destinataires=" + destinataires +
                '}';
    }
}
