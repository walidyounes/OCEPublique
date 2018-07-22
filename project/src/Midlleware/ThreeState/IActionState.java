/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import OCE.Decisions.AbstractDecision;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;

public interface IActionState {

    void act(ArrayList<AbstractDecision> decisionsList);
    void setCommunicationManager(ICommunicationAdapter communicationManager);
}
