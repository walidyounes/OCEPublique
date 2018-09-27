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
import OCE.Messages.Message;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.ArrayList;

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
     * @param receivers  : the receivers of the message
     */
    @Override
    public void sendMessageBroadcast(Message message, OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.mycomunnicationAdapter.sendMessageBroadcast(message, emitter, receivers);
    }

    /**
     * sends a message from one agent to another (point to point communication)
     *
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param receivers  : the receivers of the message
     */
    @Override
    public void sendMessage(Message message, OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.mycomunnicationAdapter.sendMessage(message, emitter,  receivers);
    }

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The message received
     */
    /*@Override
    public Optional<IMessage> receiveMessage(ServiceAgent receiver) {
        return this.mycomunnicationAdapter.receiveMessage(receiver);
    }*/

    /**
     * allows an agent to retreive all the messages sented to it
     *
     * @param receiver the recipient of the messages
     * @return list of messages received
     */
    /*@Override
    public ArrayList<IMessage> receiveMessages(ServiceAgent receiver) {
        return this.mycomunnicationAdapter.receiveMessages(receiver);
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
     * Resolve the physical adresse (InfraAgentReference) of ONE ServiceAgent
     * @param oceAgent : the service InfraAgent in question
     * @return his physical reference
     * @throws ReferenceResolutionFailure when the oceAgent doesn't exist
     */
    @Override
    public InfraAgentReference resolveAgentReference(OCEAgent oceAgent) throws ReferenceResolutionFailure {
        return this.myRecorder.resolveAgentReference(oceAgent);
    }

    /**
     * Resolve the physical adresse (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
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
    public ServiceAgent retrieveSAgentByPService(OCService attachedService) {
        return this.myRecorder.retrieveSAgentByPService(attachedService);
    }

    /**
     * Resolve the logical adresse (OCEAgent) of a list of InfraAgentReference
     * @param infraAgents the liste of the refrence of the infrastructure agent
     * @return the coresponding list of OCEAgents
     * @throws ReferenceResolutionFailure if one of the agents doesn't exist
     */
    @Override
    public ArrayList<OCEAgent> retrieveOCEAgentsByInfraAgentReferences(ArrayList<InfraAgentReference> infraAgents) throws ReferenceResolutionFailure {
        return this.myRecorder.retrieveOCEAgentsByInfraAgentReferences(infraAgents);
    }

    /**
     * Resolve the logical adresse (OCEAgent) of the InfraAgentReference
     * @param infraAgent the refrence of the infrastructure agent
     * @return the coresponding OCEAgent
     * @throws ReferenceResolutionFailure if the agent doesn't exist
     */
    @Override
    public OCEAgent retrieveOCEAgentByInfraAgentReference(InfraAgentReference infraAgent) throws ReferenceResolutionFailure {
        return this.myRecorder.retrieveOCEAgentByInfraAgentReference(infraAgent);
    }
}
