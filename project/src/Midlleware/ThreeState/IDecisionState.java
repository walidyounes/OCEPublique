/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;

public interface IDecisionState {

    void decide(ArrayList<IMessage> perceptions);
}
