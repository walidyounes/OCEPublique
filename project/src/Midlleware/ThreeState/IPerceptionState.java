/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Agent.InfraAgent;
import OCE.Messages.Message;

import java.util.ArrayList;

public interface IPerceptionState {

    ArrayList<Message> percept();
    void setInfraAgent(InfraAgent infraAgent);
}
