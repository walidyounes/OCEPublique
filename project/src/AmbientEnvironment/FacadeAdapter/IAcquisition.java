/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.FacadeAdapter;

import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;


import java.util.Set;

/**
 * Acquisition Interface : detection of change in a component framework
 *
 * @version 0.1.0
 */
public interface IAcquisition {

    /**
     * get components that have appeared
     *
     * @return a set of new components
     * @throws AcquisitionFailure
     */
     Set<OCComponent> getNewComponents() throws AcquisitionFailure;

    /**
     * get components that have disappeared
     *
     * @return a set of disappeared components
     * @throws AcquisitionFailure
     */
     Set<OCComponent> getDisappearedComponents() throws AcquisitionFailure;

    /**
     * get components that are yet in the environment
     *
     * @return a set of current components
     * @throws AcquisitionFailure
     */
     Set<OCComponent> getCurrentComponents() throws AcquisitionFailure;

    /**
     * get services of components that have appeared
     *
     * @return a set of services
     * @throws AcquisitionFailure
     */
     Set<OCService> getNewServices() throws AcquisitionFailure;

    /**
     * get services of components that have disappeared
     *
     * @return a set of services
     * @throws AcquisitionFailure
     */
    Set<OCService> getDisappearedServices() throws AcquisitionFailure;

    /**
     * get services of components that are in the environment
     *
     * @return a set of services
     * @throws AcquisitionFailure
     */
     Set<OCService> getCurrentServices() throws AcquisitionFailure;


}
