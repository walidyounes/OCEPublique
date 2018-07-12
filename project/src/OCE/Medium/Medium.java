/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import OCE.Medium.Communication.CommunicationAdapter;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.Recorder.Record;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.Optional;

public class Medium implements IRecord, ICommunicationAdapter {

    private IRecord myRecorder;
    private ICommunicationAdapter mycomunnicationAdapter;

    public Medium(ICommunication communicationInfrastructure) {
        //Intanciate the recorder
        this.myRecorder = new Record();
        //Instanciate the communication adapter with the communication module from the infrastructure
        this.mycomunnicationAdapter = new CommunicationAdapter(communicationInfrastructure, myRecorder);
    }

    /**
     * sends a message from one agent to all the other agents in broadcast
     *
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param recievers  : the recievers of the message
     */
    @Override
    public void sendMessageBroadcast(IMessage message,  ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.mycomunnicationAdapter.sendMessageBroadcast(message, emitter,  recievers);
    }

    /**
     * sends a message from one agent to another (point to point communication)
     *
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param recievers  : the recievers of the message
     */
    @Override
    public void sendMessage(IMessage message,  ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.mycomunnicationAdapter.sendMessage(message, emitter,  recievers);
    }

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The message received
     */
    @Override
    public Optional<IMessage> receiveMessage(ServiceAgent receiver) {
        return this.mycomunnicationAdapter.receiveMessage(receiver);
    }

    /**
     * allows an agent to retreive all the messages sented to it
     *
     * @param receiver the recipient of the messages
     * @return list of messages received
     */
    @Override
    public ArrayList<IMessage> receiveMessages(ServiceAgent receiver) {
        return this.mycomunnicationAdapter.receiveMessages(receiver);
    }

    /**
     * Register in the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     * @param agentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    @Override
    public void registerServiceAgent(ServiceAgent serviceAgent, AgentReference agentReference) {
        this.myRecorder.registerServiceAgent(serviceAgent, agentReference);
    }

    /**
     * Unregister from the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     *
     */
    @Override
    public void unregisterServiceAgent(ServiceAgent serviceAgent) {
        this.myRecorder.unregisterServiceAgent(serviceAgent);
    }

    /**
     * Resolve the physical adresse (AgentReference) of ONE ServiceAgent
     * @param serviceAgent : the service Agent in question
     * @return his physical reference
     * @throws ReferenceResolutionFailure when the serviceAgent doesn't exist
     */
    @Override
    public AgentReference resolveAgentReference(ServiceAgent serviceAgent) throws ReferenceResolutionFailure {
        return this.myRecorder.resolveAgentReference(serviceAgent);
    }

    /**
     * Resolve the physical adresse (AgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
     * @param serviceAgents : the list of the serviceAgents
     * @return the list of corresponding physical references
     * @throws ReferenceResolutionFailure when a serviceAgent doesn't exist
     */
    @Override
    public ArrayList<AgentReference> resolveAgentsReferences(ArrayList<ServiceAgent> serviceAgents) throws ReferenceResolutionFailure {
        return this.myRecorder.resolveAgentsReferences(serviceAgents);
    }

    /**
     * Retrieve and return the ServiceAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    @Override
    public ServiceAgent retrieveSAgentByPService(OCService attachedService) {
        return this.myRecorder.retrieveSAgentByPService(attachedService);
    }
}
