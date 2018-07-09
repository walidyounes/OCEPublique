/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */
package OCE.Medium.composants;

import OCE.Medium.services.IEnvAnnonce;
import OCE.Medium.services.IListeAgents;

public class Mediator {

    private IListeAgents record; // requis listeAgent de Record
    private IEnvAnnonce messageSender; // requis envAnnonce de MessageSender

    public Mediator(IListeAgents record, IEnvAnnonce messageSender) {
        super();
        this.record = record;
        this.messageSender = messageSender;
    }

}
