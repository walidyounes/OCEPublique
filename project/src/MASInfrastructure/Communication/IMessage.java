/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Communication;

import MASInfrastructure.Agent.AgentReference;

import java.util.ArrayList;

public interface IMessage {

    AgentReference getExpediteur();

    AbstractPerception toPerception();

    ArrayList<AgentReference> getDestinataires();
}
