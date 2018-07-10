/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Communication;

import Infrastructure.Communication.ICommunication;
import Infrastructure.Communication.IMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.Optional;

/**
 * this class acts as a communication intermediary between the engine's serviceAgents and the infrastructure communication component
 * @author Walid YOUNES
 * @version 1.0
 */
public class CommunicationAdapterAdapter implements ICommunicationAdapter {

    private IRecord mediumRecorder;
    private ICommunication comunicationInfrastructure;

    public CommunicationAdapterAdapter(ICommunication comunicationInfrastructure, IRecord mediumRecorder) {
        this.comunicationInfrastructure = comunicationInfrastructure;
        //set the reference of the recorder so that we can use it to lookup for agent physical reference
        this.mediumRecorder = mediumRecorder;
    }

    /**
     * sends a message from one agent to all the other agents in broadcast
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessageBroadcast(IMessage message) {

    }

    /**
     * sends a message from one agent to another
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(IMessage message) {

    }

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The message received
     */
    @Override
    public Optional<IMessage> receiveMessage(ServiceAgent receiver) {
        return null;
    }

    /**
     * allows an agent to retreive all the messages sented to it
     *
     * @param receiver the recipient of the messages
     * @return list of messages received
     */
    @Override
    public ArrayList<IMessage> receiveMessages(ServiceAgent receiver) {
        return null;
    }
}
