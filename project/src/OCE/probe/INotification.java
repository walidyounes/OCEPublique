/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.probe;

import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;


/**
 *
 */
public interface INotification {

    /**
     * Create for each appearing service a Service agent associated
     *
     * @param appearingServicesList : List of appearing services
     */
    void appearingServices(ArrayList<OCService> appearingServicesList);

    /**
     * for each disappearing service delete the service agent attached to
     *
     * @param disappearingServicesList : Lsit of disappearing services
     */
    void disappearingServices(ArrayList<OCService> disappearingServicesList);
}
