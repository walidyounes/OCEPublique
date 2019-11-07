/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Agent.InfrastructureAgent;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public interface IPerceptionState {

    ArrayList<InfraMessage> percept();
    void setInfraAgent(InfrastructureAgent infrastructureAgent);
}
