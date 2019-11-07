/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import OCE.Agents.OCEAgent;
import OCE.OCEDecisions.OCEDecision;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;

public interface IActionState {

    void act(ArrayList<OCEDecision> decisionsList);
    void setCommunicationManager(ICommunicationAdapter communicationManager);
    void setBinderAgent(OCEAgent myBinderAgent);
}
