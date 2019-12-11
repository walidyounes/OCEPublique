/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.FeedbackInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;

public class RejectedConnectionState implements IConnectionState {

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection :  the connection to be treated
     * @param communicationManager : the medium used to send messages to the concerned agent which are part of this connection
     * @param oceRecord : the reference to the component responsible for reference resolving
     * @param binderAgentFactory   : the reference to the component which allows creating binder agents
     */
    @Override
    public void treatConnection(Connection connection, ICommunicationAdapter communicationManager, IRecord oceRecord, IOCEBinderAgentFactory binderAgentFactory) {
        //Get the first Service agent
        ServiceAgent firstServiceAgent = connection.getFirstServiceAgent();
        //Get the second Service agent
        ServiceAgent secondServiceAgent = connection.getSecondServiceAgent();

        //Send the feedbackMessage for the first agent
            //Create the feedback message with an Accepted Value
            //create the message with a "REJECTED" response
            FeedbackInfraMessage firstAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.REJECTED);
            //Add the two agents as receivers for the message
            ArrayList<OCEAgent> firstAgentReceivers = new ArrayList<>();
            firstAgentReceivers.add(firstServiceAgent);
            //Set to "empty" the agent to whom the second agent is connected to
            firstAgentFeedbackMessage.deleteAgentChosenUser();
            //send the message using the communication manager
            communicationManager.sendMessage(firstAgentFeedbackMessage,connection.getBinderAgent(),firstAgentReceivers);

        //Send the feedbackMessage for the second agent
            //Create the feedback message with an Accepted Value
            //create the message with a "REJECTED" response
            FeedbackInfraMessage secondAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.REJECTED);
            //Set to "empty" the agent to whom the second agent is connected to
            secondAgentFeedbackMessage.deleteAgentChosenUser();
            //Add the two agents as receivers for the message
            ArrayList<OCEAgent> secondAgentReceivers = new ArrayList<>();
            secondAgentReceivers.add(secondServiceAgent);
            //send the message using the communication manager
            communicationManager.sendMessage(secondAgentFeedbackMessage,connection.getBinderAgent(),secondAgentReceivers);

//        //Create the feedback message with an Accepted Value
//        //create the message with a "REJECTED" response
//        FeedbackInfraMessage feedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.REJECTED);
//        //Add the two agents as receivers for the message
//        ArrayList<OCEAgent> receivers = new ArrayList<>();
//        receivers.add(firstServiceAgent);
//        receivers.add(secondServiceAgent);
//        //send the message using the communication manager
//        communicationManager.sendMessage(feedbackMessage,connection.getBinderAgent(),receivers);
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
