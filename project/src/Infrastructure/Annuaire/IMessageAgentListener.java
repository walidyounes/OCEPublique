/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Communication.IMessage;

public interface IMessageAgentListener {

    void messageEnvoye(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessage IMessage);

    void messageRecu(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessage IMessage);
}
