/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import AmbientEnvironment.MockupCompo.MockupService;

import java.util.Optional;

public class ModifiedConnectionState implements IConnectionState {

    private Optional<MockupService> secondServiceChangedTo; // The new second service for  the old first service (it may not exist if the user doesn't reconnect it)
    private Optional<MockupService> firstServiceChangedTo; // The new first service for the old second service (it may not exist if the user doesn't reconnect it)

    /**
     * Constructor
     */
    public ModifiedConnectionState() {
        this.firstServiceChangedTo = Optional.empty();
        this.secondServiceChangedTo = Optional.empty();
    }

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
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return "{ ( MODIFIED STATE ), ( "+ this.firstServiceChangedTo + " ), ( " + this.secondServiceChangedTo + " ) }";
    }
}
