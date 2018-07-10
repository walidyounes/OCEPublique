/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import Agent.Beans.Perceptions.AbstractPerception;
import Agent.Beans.Perceptions.EmptyPerception;
import OCPlateforme.OCService;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.ArrayList;

public class EmptyMessage extends MessageAgent {


    /**
     * Creer un message vide
     */
    public EmptyMessage() {
        this.expediteur = null; // Aucun expéditeur car pas de messages
        this.service = null; // Aucun service géré
        this.destinataires = null;
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
        return new EmptyPerception();
    }

    @Override
    public ArrayList<ReferenceAgent> getDestinataires() {
        return this.destinataires;
    }

    @Override
    public String toString() {
        return "EmptyMessage{" +
                "service=" + service +
                ", expediteur=" + expediteur +
                ", destinataires=" + destinataires +
                '}';
    }
}
