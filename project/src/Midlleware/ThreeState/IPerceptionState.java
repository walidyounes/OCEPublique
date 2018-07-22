/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Communication.IMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.Message;
import OCE.ServiceAgent;

import java.util.ArrayList;

public interface IPerceptionState {

    ArrayList<Message> percept();
    void setInfraAgent(InfraAgent infraAgent);
}
