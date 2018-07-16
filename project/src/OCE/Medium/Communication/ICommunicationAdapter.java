/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Communication;


import MASInfrastructure.Communication.IMessage;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.Optional;

public interface ICommunicationAdapter {

    /**
     * sends a message from one agent to all the other agents in broadcast
     *
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param recievers  : the recievers of the message
     */
    void sendMessageBroadcast(IMessage message, ServiceAgent emitter, ArrayList<ServiceAgent> recievers);

    /**
     * sends a message from one agent to another (point to point communication)
     *
     * @param message the message to be sent
     * @param emitter the sender of the message
     * @param recievers  : the recievers of the message
     */
    void sendMessage(IMessage message, ServiceAgent emitter, ArrayList<ServiceAgent> recievers);

    /**
     * allows an agent to receive One message (the first in it's mail box)
     *
     * @param receiver the receiver of the messages
     * @return The received message
     */
    // Optional<IMessage> receiveMessage(ServiceAgent receiver);

    /**
     * allows an agent to retreive all the messages sented to it
     *
     * @param receiver the recipient of the messages
     * @return list of received messages
     */
    // ArrayList<IMessage> receiveMessages(ServiceAgent receiver);
}
