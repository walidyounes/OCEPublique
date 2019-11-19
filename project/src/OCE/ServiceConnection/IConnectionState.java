/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import OCE.Medium.Communication.ICommunicationAdapter;

public interface IConnectionState {

    /**
     * Treat the connexion according to the it's state
     * @param connection :  the connection to be treated
     * @param communicationManager : the medium used to send messages to the concerned agent which are part of this connection
     */
    void treatConnection(Connection connection, ICommunicationAdapter communicationManager);
}
