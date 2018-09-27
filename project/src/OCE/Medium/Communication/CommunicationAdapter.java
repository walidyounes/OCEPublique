/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Communication;

import MASInfrastructure.Communication.ICommunication;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Messages.Message;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;

/**
 * this class acts as a communication intermediary between the engine's serviceAgents and the infrastructure communication component
 * @author Walid YOUNES
 * @version 1.0
 */
public class CommunicationAdapter implements ICommunicationAdapter {

    private IRecord mediumRecorder;
    private ICommunication comunicationInfrastructure;

    public CommunicationAdapter(ICommunication comunicationInfrastructure, IRecord mediumRecorder) {
        this.comunicationInfrastructure = comunicationInfrastructure;
        //set the reference of the recorder so that we can use it to lookup for agent physical reference
        this.mediumRecorder = mediumRecorder;
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
        try {
            // Resolving the reference for the transmitter
            message.setEmitter(this.mediumRecorder.resolveAgentReference(emitter));
            // When sending a message in broadcast the list of receivers is "empty" since every agent is receiving the message so no need to resolve recipients address
            message.setReceivers(new ArrayList<>());
            // Sending the message through the MAS-Infrastructure
            this.comunicationInfrastructure.sendMessageBroadcast(message);
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
        }
    }

    /**
     * sends a message from one agent to another (point to point communication)
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param receivers  : the receivers of the message
     */
    @Override
    public void sendMessage(Message message, OCEAgent emitter, ArrayList<OCEAgent> receivers) {

        try {
            // Resolving the reference of the emitter of the message
            message.setEmitter(this.mediumRecorder.resolveAgentReference(emitter));
            // Resolving the references of the receivers of the message
            message.setReceivers(this.mediumRecorder.resolveAgentsReferences(receivers));
            // Sending the message through the MAS-Infrastructure
            this.comunicationInfrastructure.sendMessage(message);
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
        }
    }

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The message received
     */
  /*  @Override
    public Optional<IMessage> receiveMessage(ServiceAgent receiver) {
        try {
            // Resolving the reference of the reciever
            InfraAgentReference recieverReference = this.mediumRecorder.resolveAgentReference(receiver);
            // Read the first message from the mail box of the agent through the MAS-Infrastructure
            return this.comunicationInfrastructure.receiveMessage(recieverReference);
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }*/

    /**
     * allows an agent to retreive all the messages sented to it
     *
     * @param receiver the recipient of the messages
     * @return list of messages received
     */
    /*@Override
    public ArrayList<IMessage> receiveMessages(ServiceAgent receiver) {
        try {
            // Resolving the reference of the reciever
            InfraAgentReference recieverReference = this.mediumRecorder.resolveAgentReference(receiver);
            // Read all the message from the mail box of the agent through the MAS-Infrastructure
            return this.comunicationInfrastructure.receiveMessages(recieverReference);
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }*/
}
