/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */
package OCE.Medium.composants;

import Agent.Beans.Messages.AdMessage;
import medium.services.IAcheminement;
import medium.services.IRecAnnonce;

public class MessageReceiver implements IAcheminement {

    private IRecAnnonce recAnnonce; // requis = transmission message annonce au Mediator

    public MessageReceiver(IRecAnnonce recAnnonce) {
        super();
        this.recAnnonce = recAnnonce;
    }

    /**
     * Permet à un agent de s'annoncer. Transmet l'annonce au mediator.
     *
     * @param ann annonce
     */

    @Override
    public void annoncer(AdMessage ann) {
        recAnnonce.addAnnonce(ann);
    }

    public IRecAnnonce getRecAnnonce() {
        return recAnnonce;
    }
}
