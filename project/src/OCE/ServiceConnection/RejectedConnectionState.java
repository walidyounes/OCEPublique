/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

public class RejectedConnectionState implements IConnectionState {

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection :  the connection to be treated
     */
    @Override
    public void treatConnection(Connection connection) {

    }

    /**
     * Returns a string representation of the object
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "{ ( REJECTED STATE ) }";
    }
}
