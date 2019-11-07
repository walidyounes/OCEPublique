/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

public interface IConnectionState {

    /**
     * Treat the connexion according to the it's state
     * @param connection :  the connection to be treated
     */
    void treatConnection(Connection connection);
}
