/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import OCE.Decisions.OCEDecision;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;

public interface IActionState {

    void act(ArrayList<OCEDecision> decisionsList);
    void setCommunicationManager(ICommunicationAdapter communicationManager);
}
