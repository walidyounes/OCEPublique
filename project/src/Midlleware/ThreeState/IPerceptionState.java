/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Communication.IMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceAgent;

import java.util.ArrayList;

public interface IPerceptionState {

    ArrayList<IMessage> percept();
    void setServiceAgent(ServiceAgent serviceAgent);
    void setCommunicationManager(ICommunicationAdapter communicationManager);
}
