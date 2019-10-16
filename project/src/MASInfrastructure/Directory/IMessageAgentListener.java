/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.IMessage;

public interface IMessageAgentListener {

    void messageEnvoye(InfraAgentReference expediteur, InfraAgentReference destinataire, IMessage IMessage);

    void messageRecu(InfraAgentReference expediteur, InfraAgentReference destinataire, IMessage IMessage);
}
