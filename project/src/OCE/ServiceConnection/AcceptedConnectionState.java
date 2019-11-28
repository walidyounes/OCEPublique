/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.FeedbackInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;

public class AcceptedConnectionState implements IConnectionState {

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection :  the connection to be treated
     * @param communicationManager : the medium used to send messages to the concerned agent which are part of this connection
     * @param oceRecord : the reference to the component responsible for reference resolving
     */
    @Override
    public void treatConnection(Connection connection, ICommunicationAdapter communicationManager, IRecord oceRecord) {
        //Get the first Service agent
        ServiceAgent firstServiceAgent = connection.getFirstServiceAgent();
        //Get the second Service agent
        ServiceAgent secondServiceAgent = connection.getSecondServiceAgent();

        //Send the feedbackMessage for the first agent
            //Create the feedback message with an Accepted Value
            //create the message with a "ACCEPTED" response
            FeedbackInfraMessage firstAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ACCEPTED);
            //Add the two agents as receivers for the message
            ArrayList<OCEAgent> firstAgentReceivers = new ArrayList<>();
            firstAgentReceivers.add(firstServiceAgent);
            //Set the agent to whom the first agent is connected to
            firstAgentFeedbackMessage.setAgentChosenUser(secondServiceAgent);
            //send the message using the communication manager
            communicationManager.sendMessage(firstAgentFeedbackMessage,connection.getBinderAgent(),firstAgentReceivers);

        //Send the feedbackMessage for the second agent
            //Create the feedback message with an Accepted Value
            //create the message with a "ACCEPTED" response
            FeedbackInfraMessage secondAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ACCEPTED);
            //Set the agent to whom the second agent is connected to
            secondAgentFeedbackMessage.setAgentChosenUser(firstServiceAgent);
            //Add the two agents as receivers for the message
            ArrayList<OCEAgent> secondAgentReceivers = new ArrayList<>();
            secondAgentReceivers.add(secondServiceAgent);
            //send the message using the communication manager
            communicationManager.sendMessage(secondAgentFeedbackMessage,connection.getBinderAgent(),secondAgentReceivers);
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "{ ( ACCEPTED STATE ) }";
    }
}
