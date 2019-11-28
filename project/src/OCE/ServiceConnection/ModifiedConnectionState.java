/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import AmbientEnvironment.MockupCompo.MockupService;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.FeedbackInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;
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
        //Create the feedback message with a MODIFIED Value
        FeedbackInfraMessage firstAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.MODIFIED);
        //Add the two agents as receivers for the message
        ArrayList<OCEAgent> firstAgentReceivers = new ArrayList<>();
        firstAgentReceivers.add(firstServiceAgent);

        //Check to with these two service agents are connected to
        if(this.secondServiceChangedTo.isPresent()){
                //Get the reference of the service agent corresponding to the new service connected to the firstService (it return null if it fails)
                ServiceAgent firstSAConnectedTo = oceRecord.retrieveSAgentByPService(this.secondServiceChangedTo.get()).orElse(null);

                //Set the reference of the service agent to the agent to whom the first agent is connected to
                firstAgentFeedbackMessage.setAgentChosenUser(firstSAConnectedTo);

                //Send a FeedbackMessage to the agent chosen by the user
                    //Create the feedback message with a MODIFIED Value
                    FeedbackInfraMessage chosenAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.MODIFIED);
                    //Add the chosen agent as receivers for the message
                    ArrayList<OCEAgent> chosenAgentReceivers = new ArrayList<>();
                    chosenAgentReceivers.add(firstSAConnectedTo);

                    //Set the reference of the first service agent to the chosen agent as connected to
                    chosenAgentFeedbackMessage.setAgentChosenUser(firstServiceAgent);

                    //send the feedback message to the chosen agent
                    communicationManager.sendMessage(chosenAgentFeedbackMessage, connection.getBinderAgent(), chosenAgentReceivers);
        }
        else{
            //Set to "empty" the reference to the agent to whom the first agent is connected to
            firstAgentFeedbackMessage.deleteAgentChosenUser();
        }

        //send the feedback message for the first agent using the communication manager
        communicationManager.sendMessage(firstAgentFeedbackMessage, connection.getBinderAgent(), firstAgentReceivers);

        //Send the feedbackMessage for the second agent
        //Create the feedback message with a MODIFIED Value
        FeedbackInfraMessage secondAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.MODIFIED);
        //Add the two agents as receivers for the message
        ArrayList<OCEAgent> secondAgentReceivers = new ArrayList<>();
        secondAgentReceivers.add(secondServiceAgent);
        //Check to with these two service agents are connected to
        if(this.firstServiceChangedTo.isPresent()){
            //Get the reference of the service agent corresponding to the new service connected to the secondService (it return null if it fails)
            ServiceAgent secondSAConnectedTo = oceRecord.retrieveSAgentByPService(this.firstServiceChangedTo.get()).orElse(null);

            //Set the reference of the service agent to the agent to whom the second agent is connected to
            secondAgentFeedbackMessage.setAgentChosenUser(secondSAConnectedTo);

            //Send a FeedbackMessage to the agent chosen by the user
                //Create the feedback message with a MODIFIED Value
                FeedbackInfraMessage chosenAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.MODIFIED);
                //Add the chosen agent as receivers for the message
                ArrayList<OCEAgent> chosenAgentReceivers = new ArrayList<>();
                chosenAgentReceivers.add(secondSAConnectedTo);

                //Set the reference of the second service agent to the chosen agent as connected to
                chosenAgentFeedbackMessage.setAgentChosenUser(secondServiceAgent);

                //send the feedback message to the chosen agent
                communicationManager.sendMessage(chosenAgentFeedbackMessage, connection.getBinderAgent(), chosenAgentReceivers);
        }
        else{
            //Set to "empty" the reference to the agent to whom the second agent is connected to
            secondAgentFeedbackMessage.deleteAgentChosenUser();
        }

        //send the message using the communication manager
        communicationManager.sendMessage(secondAgentFeedbackMessage,connection.getBinderAgent(),secondAgentReceivers);

    }

    /**
     * Get the new second service that the old first service of the connection is connected to
     * @return : the reference of the new service, or "empty" if the first service was left not connected
      */
    public Optional<MockupService> getSecondServiceChangedTo() {
        return secondServiceChangedTo;
    }

    /**
     * Update the reference new second service that the old first service of the connection is connected to
     * @return : the reference of the new service, or "empty" if the first service was left not connected
     */
    public void setSecondServiceChangedTo(MockupService secondServiceChangedTo) {
        this.secondServiceChangedTo = Optional.ofNullable(secondServiceChangedTo);
    }

    /**
     * Get the new first service that the old second service of the connection is connected to
     * @return : the reference of the new service, or "empty" if the second service was left not connected
     */
    public Optional<MockupService> getFirstServiceChangedTo() {
        return firstServiceChangedTo;
    }

    /**
     * Update the reference new first service that the old second service of the connection is connected to
     * @return : the reference of the new service, or "empty" if the second service was left not connected
     */
    public void setFirstServiceChangedTo(MockupService firstServiceChangedTo) {
        this.firstServiceChangedTo = Optional.ofNullable(firstServiceChangedTo);
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
