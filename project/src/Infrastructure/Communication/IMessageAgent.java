/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Communication;

import Agent.Beans.Perceptions.AbstractPerception;
import sma.infrastructure.agent.ReferenceAgent;

import java.util.ArrayList;

public interface IMessageAgent {

    ReferenceAgent getExpediteur();

    AbstractPerception toPerception();

    ArrayList<ReferenceAgent> getDestinataires();
}
