/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Agent.InfraAgent;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public interface IPerceptionState {

    ArrayList<InfraMessage> percept();
    void setInfraAgent(InfraAgent infraAgent);
}
