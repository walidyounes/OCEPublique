/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import OCE.Decisions.OCEDecision;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public interface IDecisionState {

    ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perceptions);
}
