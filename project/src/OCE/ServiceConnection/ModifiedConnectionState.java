/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import AmbientEnvironment.MockupCompo.MockupService;
import MASInfrastructure.Agent.InfrastructureAgent;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.FeedbackInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModifiedConnectionState implements IConnectionState {

    private Optional<MockupService> secondServiceChangedTo; // The new second service for  the old first service (it may not exist if the user doesn't reconnect it)
    private Optional<MockupService> firstServiceChangedTo; // The new first service for the old second service (it may not exist if the user doesn't reconnect it)
    private Optional<Boolean> createOneMoreBinderAgent;           // If true, a new binder agent must be created to handle one of the modified connection (cause there is insufficient number of binder agents in the system)

    /**
     * Constructor
     */
    public ModifiedConnectionState() {
        this.firstServiceChangedTo = Optional.empty();
        this.secondServiceChangedTo = Optional.empty();
        this.createOneMoreBinderAgent = Optional.empty();
    }

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection                :  the connection to be treated
     * @param communicationManager      : the medium used to send messages to the concerned agent which are part of this connection
     * @param oceRecord                 : the reference to the component responsible for reference resolving
     * @param binderAgentFactory        : the reference to the component which allows creating binder agents
     * @param infrastructureAgentList   : the list of agents to wake up to inform them of the arrival of user feedback
     */
    @Override
    public void treatConnection(Connection connection, ICommunicationAdapter communicationManager, IRecord oceRecord, IOCEBinderAgentFactory binderAgentFactory, List<InfrastructureAgent> infrastructureAgentList) {
        //check if both of the original services of the connection got connected to new ones
        if(this.secondServiceChangedTo.isPresent() && this.firstServiceChangedTo.isPresent()){
            //In the binder agent handling this connection, delete both the services
            connection.getBinderAgent().resetHandledServices();
        }

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

        //Add both agents in the list to get wake up later to treat the user feedback
        infrastructureAgentList.add(firstServiceAgent.getMyInfrastructureAgent());
        infrastructureAgentList.add(secondServiceAgent.getMyInfrastructureAgent());

        //Get the binder agents of this connection
        BinderAgent binderAgent = connection.getBinderAgent();

        //Check to with these two service agents are connected to
        if(this.secondServiceChangedTo.isPresent()){
                //Get the reference of the service agent corresponding to the new service connected to the firstService (it return null if it fails)
                ServiceAgent firstSAConnectedTo = oceRecord.retrieveSAgentByPService(this.secondServiceChangedTo.get()).orElse(null);

                //Add the agent in the list to get wake up later to treat the user feedback
                infrastructureAgentList.add(firstSAConnectedTo.getMyInfrastructureAgent());

                //Set the reference of the service agent to whom the first agent is connected to
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
                    communicationManager.sendMessage(chosenAgentFeedbackMessage, binderAgent, chosenAgentReceivers);
                    //communicationManager.sendMessage(chosenAgentFeedbackMessage, connection.getBinderAgent(), chosenAgentReceivers);
        }
        else{
            //Set to "empty" the reference to the agent to whom the first agent is connected to
            firstAgentFeedbackMessage.deleteAgentChosenUser();
        }

        //send the feedback message for the first agent using the communication manager
        communicationManager.sendMessage(firstAgentFeedbackMessage, binderAgent, firstAgentReceivers);

        //Check if we need one more binder Agent
        if(this.createOneMoreBinderAgent.isPresent()){
            if (this.createOneMoreBinderAgent.get()){ // We need one more agent that will be used for the second new connection
                binderAgent = (BinderAgent) binderAgentFactory.createBinderAgent().getKey();
            }
            //Reinitialise the field to empty
            this.createOneMoreBinderAgent = Optional.empty();
        }

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

            //Add the agent in the list to get wake up later to treat the user feedback
            infrastructureAgentList.add(secondSAConnectedTo.getMyInfrastructureAgent());

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
                communicationManager.sendMessage(chosenAgentFeedbackMessage, binderAgent, chosenAgentReceivers);
        }
        else{
            //Set to "empty" the reference to the agent to whom the second agent is connected to
            secondAgentFeedbackMessage.deleteAgentChosenUser();
        }

        //send the message using the communication manager
        communicationManager.sendMessage(secondAgentFeedbackMessage,binderAgent,secondAgentReceivers);

    }

    /**
     * Get the new second service that the user choose to connect to the old first service of the connection proposed by OCE
     * @return : the reference of the new service, or "empty" if the first service was left not connected
      */
    public Optional<MockupService> getSecondServiceChangedTo() {
        return secondServiceChangedTo;
    }

    /**
     * Update the reference new second service that the user choose to connect to the old first service of the connection proposed by OCE
     * @param secondServiceChangedTo : he reference new second service that the old first service of the connection is connected to
     */
    public void setSecondServiceChangedTo(MockupService secondServiceChangedTo) {
        this.secondServiceChangedTo = Optional.ofNullable(secondServiceChangedTo);
    }

    /**
     * Get the new first service that the user choose to connect to the old second service of the connection proposed by OCE
     * @return : the reference of the new service, or "empty" if the second service was left not connected
     */
    public Optional<MockupService> getFirstServiceChangedTo() {
        return firstServiceChangedTo;
    }

    /**
     * Update the reference new first service that the user choose to connect to the old second service of the connection proposed by OCE
     * @param firstServiceChangedTo : the reference new first service that the old second service of the connection is connected to
     */
    public void setFirstServiceChangedTo(MockupService firstServiceChangedTo) {
        this.firstServiceChangedTo = Optional.ofNullable(firstServiceChangedTo);
    }

    /**
     * Get the value of the parameter that indicates whether the connection which this state belongs to need to create one more binder agent
     * @return : true if the connection need one more binder agent, else "empty"
     */
    public Optional<Boolean> getCreateOneMoreBinderAgent() {
        return createOneMoreBinderAgent;
    }

    /**
     * Update the value of the parameter that indicates whether the connection which this state belongs to need to create one more binder agent
     * @param createOneMoreBinderAgent : the value of the parameter that indicates whether the connection which this state belongs to need to create one more binder agent
     */
    public void setCreateOneMoreBinderAgent(Boolean createOneMoreBinderAgent) {
        this.createOneMoreBinderAgent = Optional.ofNullable(createOneMoreBinderAgent);
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
