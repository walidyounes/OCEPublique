/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import OCE.Medium.Communication.CommunicationAdapter;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.Recorder.Record;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Medium implements IRecord, ICommunicationAdapter {

    private IRecord myRecorder;
    private ICommunicationAdapter myCommunicationAdapter;

    public Medium(ICommunication communicationInfrastructure) {
        //Instantiate the recorder
        this.myRecorder = new Record();
        //Instantiate the communication adapter with the communication module from the infrastructure
        this.myCommunicationAdapter = new CommunicationAdapter(communicationInfrastructure, myRecorder);
    }

    /**
     * sends a infraMessage from one agent to all the other agents in broadcast
     *
     * @param infraMessage the infraMessage to be sent
     * @param emitter the sender of the infraMessage
     * @param receivers  : the receivers of the infraMessage
     */
    @Override
    public void sendMessageBroadcast(InfraMessage infraMessage, OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.myCommunicationAdapter.sendMessageBroadcast(infraMessage, emitter, receivers);
    }

    /**
     * sends a infraMessage from one agent to another (point to point communication)
     *
     * @param infraMessage the infraMessage to be sent
     * @param emitter the sender of the infraMessage
     * @param receivers  : the receivers of the infraMessage
     */
    @Override
    public void sendMessage(InfraMessage infraMessage, OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.myCommunicationAdapter.sendMessage(infraMessage, emitter,  receivers);
    }

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The message received
     */
    /*@Override
    public Optional<IMessage> receiveMessage(ServiceAgent receiver) {
        return this.myCommunicationAdapter.receiveMessage(receiver);
    }*/

    /**
     * allows an agent to retrieve all the messages sent to it
     *
     * @param receiver the recipient of the messages
     * @return list of messages received
     */
    /*@Override
    public ArrayList<IMessage> receiveMessages(ServiceAgent receiver) {
        return this.myCommunicationAdapter.receiveMessages(receiver);
    }*/

    /**
     * Register in the recording list the mapping between a oceAgent and it's associated referenceAgent
     * @param oceAgent : the oceAgent
     * @param infraAgentReference : the agent's Reference in the infrastructure which is associated to the oceAgent
     */
    @Override
    public void registerOCEAgent(OCEAgent oceAgent, InfraAgentReference infraAgentReference) {
        this.myRecorder.registerOCEAgent(oceAgent, infraAgentReference);
    }

    /**
     * Unregister from the recording list the mapping between a oceAgent and it's associated referenceAgent
     * @param oceAgent : the oceAgent
     *
     */
    @Override
    public void unregisterOCEAgent(OCEAgent oceAgent) {
        this.myRecorder.unregisterOCEAgent(oceAgent);
    }

    /**
     * Resolve the physical address (InfraAgentReference) of ONE ServiceAgent
     * @param oceAgent : the service InfrastructureAgent in question
     * @return his physical reference
     * @throws ReferenceResolutionFailure when the oceAgent doesn't exist
     */
    @Override
    public InfraAgentReference resolveAgentReference(OCEAgent oceAgent) throws ReferenceResolutionFailure {
        return this.myRecorder.resolveAgentReference(oceAgent);
    }

    /**
     * Resolve the physical address (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
     * @param oceAgents : the list of the oceAgents
     * @return the list of corresponding physical references
     * @throws ReferenceResolutionFailure when a serviceAgent doesn't exist
     */
    @Override
    public ArrayList<InfraAgentReference> resolveAgentsReferences(ArrayList<OCEAgent> oceAgents) throws ReferenceResolutionFailure {
        return this.myRecorder.resolveAgentsReferences(oceAgents);
    }

    /**
     * Retrieve and return the ServiceAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    @Override
    public Optional<ServiceAgent> retrieveSAgentByPService(OCService attachedService) {
        return this.myRecorder.retrieveSAgentByPService(attachedService);
    }

    /**
     * Resolve the logical address (OCEAgent) of a list of InfraAgentReference
     * @param infraAgents the list of the reference of the infrastructure agent
     * @return the corresponding list of OCEAgents
     * @throws ReferenceResolutionFailure if one of the agents doesn't exist
     */
    @Override
    public ArrayList<OCEAgent> retrieveOCEAgentsByInfraAgentReferences(ArrayList<InfraAgentReference> infraAgents) throws ReferenceResolutionFailure {
        return this.myRecorder.retrieveOCEAgentsByInfraAgentReferences(infraAgents);
    }

    /**
     * Resolve the logical address (OCEAgent) of the InfraAgentReference
     * @param infraAgent the reference of the infrastructure agent
     * @return the corresponding OCEAgent
     * @throws ReferenceResolutionFailure if the agent doesn't exist
     */
    @Override
    public OCEAgent retrieveOCEAgentByInfraAgentReference(InfraAgentReference infraAgent) throws ReferenceResolutionFailure {
        return this.myRecorder.retrieveOCEAgentByInfraAgentReference(infraAgent);
    }

    /**
     * Get the set of all agents existing in the system
     * @return the list of agent present in the environment
     */
    @Override
    public ObservableList<OCEAgent> getAllAgents() {
        return this.myRecorder.getAllAgents();
    }
}
