/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;

public abstract class Message implements IMessage {

    protected InfraAgentReference emitter; // The transmitter of the message
    protected ArrayList<InfraAgentReference> recievers; // The list of the recipients of the message, if == null -> message is in broadcast

    // public abstract AbstractPerception toPerception();
}
