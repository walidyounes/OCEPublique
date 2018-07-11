/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.FacadeAdapter;


import AmbientEnvironment.OCPlateforme.OCService;

/**
 * Binding Interface : connection of two component's services in a component framework
 *
 * @version 0.1.0
 */
public interface IBinding {

    /**
     * Bind 2 services of components
     *
     * @param service1 the first service
     * @param service2 the second service
     */
    void bind(OCService service1, OCService service2) throws BindingFailure;

    /**
     * Unbind 2 connected services
     *
     * @param service1 the first service
     * @param service2 the second service
     */
    void unbind(OCService service1, OCService service2) throws UnbindingFailure;
}
