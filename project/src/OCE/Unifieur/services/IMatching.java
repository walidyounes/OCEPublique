/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Unifieur.services;

import OCPlateforme.OCService;

/**
 * Interface Matching. Service fourni par l'Unifieur.
 */
public interface IMatching {

    /**
     * Match retourne si oui ou non deux OCService sont compatibles.
     *
     * @param serviceA OCService A
     * @param serviceB OCService B
     * @return boolean
     */
    boolean match(OCService serviceA, OCService serviceB);
}
