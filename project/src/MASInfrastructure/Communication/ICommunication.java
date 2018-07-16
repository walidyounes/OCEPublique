/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Communication;

import MASInfrastructure.Agent.InfraAgentReference;

import java.util.ArrayList;
import java.util.Optional;

public interface ICommunication {

    // void sendMessage(InfraAgentReference expediteur, InfraAgentReference destinataire, IMessage IMessage); // pourquoi on passe de l'expediteur alors qu'il est ds le msg

    // void sendMessageBroadcast(InfraAgentReference expediteur, IMessage IMessage);// pourquoi on passe de l'expediteur alors qu'il est ds le msg

    /**
     * sends a message from one agent to other agents
     *
     * @param message the message to be sent
     */
    void sendMessageBroadcast(IMessage message);

    /**
     * sends a message from one agent to another
     *
     * @param message the message to be sent
     */

    void sendMessage(IMessage message);

    /**
     * allows an agent to receive One message
     *
     * @param reciever the recipient of the messages
     * @return The received message
     */
    Optional<IMessage> receiveMessage(InfraAgentReference reciever);

    /**
     * allows an agent to receive a list of messages
     *
     * @param reciever the recipient of the messages
     * @return list of received messages
     */
    ArrayList<IMessage> receiveMessages(InfraAgentReference reciever);

}
