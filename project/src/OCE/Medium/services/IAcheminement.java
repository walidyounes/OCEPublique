/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.services;


import Agent.Beans.Messages.AdMessage;

/**
 * Interface Acheminement. Service fourni par le Medium.
 * Permet aux agents de transmettre leurs annonces au Medium.
 */
public interface IAcheminement {

    /**
     * Permet aux agents de transmettre leurs annonces au Medium.
     *
     * @param ann annonce
     */
    void annoncer(AdMessage ann);
}
