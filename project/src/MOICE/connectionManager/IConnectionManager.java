/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.connectionManager;

import AmbientEnvironment.OCPlateforme.OCComponent;
import OCE.ServiceConnection.Connection;

public interface IConnectionManager {

    /**
     * Register the component in the list
     * @param component : the component to register
     */
    void registerComponent(OCComponent component);

    /**
     * Unregister the component from the list
     * @param component : the component to unregister
     */
    void unRegisterComponent(OCComponent component);

    /**
     * Register the connection proposed by the engine OCE
     * @param connection    : the proposed connection to register
     */
    void registerConnection(Connection connection);

    /**
     * Unregister the connection proposed by the engine OCE
     * @param connection    : the proposed connection to unregister
     */
    void unRegisterConnection(Connection connection);

    /**
     * Collect the connection proposed by the engine OCE. This method is dedicated to ICE
     */
    void collectOCEProposedConfiguration();
}
