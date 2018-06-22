/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Communication;

import sma.infrastructure.agent.ReferenceAgent;

import java.util.List;
import java.util.Optional;

public interface ICommunication {

    // void envoyerMessage(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessageAgent IMessageAgent); // pourquoi on passe de l'expediteur alors qu'il est ds le msg

    // void diffuserMessage(ReferenceAgent expediteur, IMessageAgent IMessageAgent);// pourquoi on passe de l'expediteur alors qu'il est ds le msg

    /**
     * sends a message from one agent to other agents
     *
     * @param message the message to be sent
     */
    public void diffuserMessage(IMessageAgent message);

    /**
     * sends a message from one agent to another
     *
     * @param message the message to be sent
     */

    public void envoyerMessage(IMessageAgent message);

    /**
     * allows an agent to receive One message
     *
     * @param destinataire the recipient of the messages
     * @return The message received
     */
    Optional<IMessageAgent> recevoirMessage(ReferenceAgent destinataire);

    /**
     * allows an agent to receive a list of messages
     *
     * @param destinataire the recipient of the messages
     * @return list of messages received
     */
    public List<IMessageAgent> recevoirMessages(ReferenceAgent destinataire);

}
