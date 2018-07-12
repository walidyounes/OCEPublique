/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.IMessage;
import OCE.ServiceAgent;

import java.util.ArrayList;

public abstract class Message implements IMessage {

    protected AgentReference emitter; // The transmitter of the message
    protected ArrayList<AgentReference> recievers; // The list of the recipients of the message, if == null -> message is in broadcast

    // public abstract AbstractPerception toPerception();
}
