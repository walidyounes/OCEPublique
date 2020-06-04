/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Unifieur;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;

/**
 * Le rôle de l'Unifieur est de proposer un service permettant de savoir si
 * deux OCService sont compatibles.
 */
public class Matching implements IMatching {
    /**
     * Match retourne si oui ou non deux OCService sont compatibles.
     *
     * @param serviceA OCService A
     * @param serviceB OCService B
     * @return boolean
     */
    @Override
    public boolean match(OCService serviceA, OCService serviceB) {
        /*if (serviceA == null || serviceB == null) {
			return false;
		}
		else {	//return true;
			return serviceA.getLinkedServices().equals(serviceB.getLinkedServices());
			*/
        if (serviceA instanceof MockupService && serviceB instanceof MockupService)

            return ( (((MockupService) serviceA).getWay() != ((MockupService) serviceB).getWay()) && (!((MockupService) serviceA).getOwner().equalsIgnoreCase(((MockupService) serviceB).getOwner()) ) &&
                    (((MockupService) serviceA).getMatchingID().equals(((MockupService) serviceB).getMatchingID())) );
        else
            return false;

    }


}
