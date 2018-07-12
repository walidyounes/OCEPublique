/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Communication;

import MASInfrastructure.Agent.AgentReference;
import java.util.ArrayList;

/**
 * Interfaceto insure the transmition of messages between the agent
 * @author Walid YOUNES
 * @version 1.0
 */
public interface IMessage {

    /**
     * get the transmitter of the message
     * @return the reference of the transmitter of the message
     */
    AgentReference getEmitter();

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    void setEmitter(AgentReference emitter);

    /**
     * get the list of the recievers of the message
     * @return the recievers of the message
     */
    ArrayList<AgentReference> getRecievers();

    /**
     * set the list of recievers for this message
     * @param recievers : the list of recievers
     */
    void setRecievers(ArrayList<AgentReference> recievers);



    // AbstractPerception toPerception();
}
