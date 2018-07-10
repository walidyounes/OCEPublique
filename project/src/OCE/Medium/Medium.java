/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium;

import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Communication.ICommunication;
import Infrastructure.Communication.IMessage;
import OCE.Medium.Communication.CommunicationAdapterAdapter;
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
        this.mycomunnicationAdapter = new CommunicationAdapterAdapter(communicationInfrastructure);
    }

    /**
     * sends a message from one agent to all the other agents in broadcast
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessageBroadcast(IMessage message) {
        this.mycomunnicationAdapter.sendMessageBroadcast(message);
    }

    /**
     * sends a message from one agent to another
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(IMessage message) {
        this.mycomunnicationAdapter.sendMessage(message);
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
    public void registerServiceAgent(ServiceAgent serviceAgent, ReferenceAgent agentReference) {
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
}
