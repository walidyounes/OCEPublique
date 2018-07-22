/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Communication.IMessage;
import OCE.Decisions.AbstractDecision;
import OCE.Messages.Message;

import java.util.ArrayList;

public interface IDecisionState {

    ArrayList<AbstractDecision> decide(ArrayList<Message> perceptions);
}
