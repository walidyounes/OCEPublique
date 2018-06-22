/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import sma.infrastructure.agent.ReferenceAgent;
import Infrastructure.Communication.IMessageAgent;

public interface IMessageAgentListener {

    void messageEnvoye(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessageAgent IMessageAgent);

    void messageRecu(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessageAgent IMessageAgent);
}
