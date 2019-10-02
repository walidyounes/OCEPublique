/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.connectionManager;

import AmbientEnvironment.OCPlateforme.OCComponent;
import OCE.ServiceConnection.Connection;

public interface IConnectionManager {

    void registerComponent(OCComponent component);

    void registerConnection(Connection connection);

    void collectConnection();
}
