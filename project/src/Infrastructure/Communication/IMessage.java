/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Communication;

import Infrastructure.Agent.ReferenceAgent;

import java.util.ArrayList;

public interface IMessage {

    ReferenceAgent getExpediteur();

    AbstractPerception toPerception();

    ArrayList<ReferenceAgent> getDestinataires();
}
