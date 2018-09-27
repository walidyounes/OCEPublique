/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Communication;

import MASInfrastructure.Agent.InfraAgentReference;

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
    InfraAgentReference getEmitter();

    /**
     *  set the refernece of the transmitter of the message
     * @param emitter : the reference of the transmitter
     */
    void setEmitter(InfraAgentReference emitter);

    /**
     * get the list of the receivers of the message
     * @return the receivers of the message
     */
    ArrayList<InfraAgentReference> getReceivers();

    /**
     * set the list of receivers for this message
     * @param receivers : the list of receivers
     */
    void setReceivers(ArrayList<InfraAgentReference> receivers);

    /**
     * treat the message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @return the deicision made by the engine
     */
   // AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef, OCService localService);


}
