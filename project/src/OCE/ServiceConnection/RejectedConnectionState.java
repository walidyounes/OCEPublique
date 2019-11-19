/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.FeedbackInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;

public class RejectedConnectionState implements IConnectionState {

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection :  the connection to be treated
     * @param communicationManager : the medium used to send messages to the concerned agent which are part of this connection
     */
    @Override
    public void treatConnection(Connection connection, ICommunicationAdapter communicationManager) {
        //Get the first Service agent
        ServiceAgent firstServiceAgent = connection.getFirstServiceAgent();
        //Get the second Service agent
        ServiceAgent secondServiceAgent = connection.getSecondServiceAgent();

        //Create the feedback message with an Accepted Value
        //create the message with a "ACCEPTED" response
        FeedbackInfraMessage feedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.REJECTED);
        //Add the two agents as receivers for the message
        ArrayList<OCEAgent> receivers = new ArrayList<>();
        receivers.add(firstServiceAgent);
        receivers.add(secondServiceAgent);
        //send the message using the communication manager
        communicationManager.sendMessage(feedbackMessage,connection.getBinderAgent(),receivers);
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
