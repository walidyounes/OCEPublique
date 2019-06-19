/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Communication;


import OCE.InfrastructureMessages.InfraMessage;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;

public interface ICommunicationAdapter {

    /**
     * sends a infraMessage from one agent to all the other agents in broadcast
     *
     * @param infraMessage the infraMessage to be sent
     * @param emitter the sender of the infraMessage
     * @param receivers  : the receivers of the infraMessage
     */
    void sendMessageBroadcast(InfraMessage infraMessage, OCEAgent emitter, ArrayList<OCEAgent> receivers);

    /**
     * sends a infraMessage from one agent to another (point to point communication)
     *
     * @param infraMessage the infraMessage to be sent
     * @param emitter the sender of the infraMessage
     * @param receivers  : the receivers of the infraMessage
     */
    void sendMessage(InfraMessage infraMessage, OCEAgent emitter, ArrayList<OCEAgent> receivers);

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
