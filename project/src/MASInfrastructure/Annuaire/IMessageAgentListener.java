/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.IMessage;

public interface IMessageAgentListener {

    void messageEnvoye(AgentReference expediteur, AgentReference destinataire, IMessage IMessage);

    void messageRecu(AgentReference expediteur, AgentReference destinataire, IMessage IMessage);
}
