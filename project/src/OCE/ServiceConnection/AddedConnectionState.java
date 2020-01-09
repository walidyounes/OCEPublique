/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import Logger.OCELogger;
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
import java.util.logging.Level;

public class AddedConnectionState implements IConnectionState {

    /**
     * Treat the connexion according to the it's state
     *
     * @param connection           :  the connection to be treated
     * @param communicationManager : the medium used to send messages to the concerned agent which are part of this connection
     * @param oceRecord            : the reference to the component responsible for reference resolving
     * @param binderAgentFactory   : the reference to the component which allows creating binder agents
     * @param infrastructureAgentList   : the list of agents to wake up to inform them of the arrival of user feedback
     */
    @Override
    public void treatConnection(Connection connection, ICommunicationAdapter communicationManager, IRecord oceRecord, IOCEBinderAgentFactory binderAgentFactory, List<InfrastructureAgent> infrastructureAgentList) {
        OCELogger.log(Level.WARNING, "Treating a connection ADDED BY THE USER ");

        //Retrieve the reference to the service agent who handel the service 'firstService' of this new connection
        Optional<ServiceAgent> firstServiceAgent = oceRecord.retrieveSAgentByPService(connection.getFirstService());
        //Retrieve the reference to the service agent who handel the service 'secondService' of this new connection
        Optional<ServiceAgent> secondServiceAgent = oceRecord.retrieveSAgentByPService(connection.getSecondService());
        //Order the creations of the binder Agent to be responsible of this connection
        BinderAgent createdBinderAgent = binderAgentFactory.createBinderAgent().getKey();
        //Add the services to the binderAgent
        createdBinderAgent.setFirstService(connection.getFirstService());
        createdBinderAgent.setSecondService(connection.getSecondService());
        //Add the created binder agent to the connection
        connection.setBinderAgent(createdBinderAgent);

        //If the two service agents are found
        if (firstServiceAgent.isPresent() && secondServiceAgent.isPresent()){
            //Add both agents in the list to get wake up later to treat the user feedback
            infrastructureAgentList.add(firstServiceAgent.get().getMyInfrastructureAgent());
            infrastructureAgentList.add(secondServiceAgent.get().getMyInfrastructureAgent());

            //Create the feedback message for the first agent with a ADDED Value
            FeedbackInfraMessage firstAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ADDED);
            //Add the first agent as receivers for the first message
            ArrayList<OCEAgent> firstAgentReceivers = new ArrayList<>();
            firstAgentReceivers.add(firstServiceAgent.get());
            //Set the reference of the service agent to whom the first agent is connected to
            firstAgentFeedbackMessage.setAgentChosenUser(secondServiceAgent.get());
            //send the feedback message to the first agent
            communicationManager.sendMessage(firstAgentFeedbackMessage, createdBinderAgent, firstAgentReceivers);


            //Create the feedback message for the second agent with a ADDED Value
            FeedbackInfraMessage secondAgentFeedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ADDED);
            //Add the second agent as receivers for the feedback message
            ArrayList<OCEAgent> secondAgentReceivers = new ArrayList<>();
            secondAgentReceivers.add(secondServiceAgent.get());
            //Set the reference of the service agent to whom the first agent is connected to
            secondAgentFeedbackMessage.setAgentChosenUser(firstServiceAgent.get());
            //send the feedback message to the chosen agent
            communicationManager.sendMessage(secondAgentFeedbackMessage, createdBinderAgent, secondAgentReceivers);
        }else{
            OCELogger.log(Level.WARNING, "Error ! Can't retrieve the agent in the record ");
        }
    }
}
