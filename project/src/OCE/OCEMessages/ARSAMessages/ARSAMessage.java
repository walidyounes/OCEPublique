/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages.ARSAMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEMessages.OCEMessage;

/**
 * This is an abstract class representing an ARSA message for a service agent
 * @author Walid YOUNES
 * @version 1.0
 */
public abstract class ARSAMessage extends OCEMessage {


    /**
     * treat the message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "CREATED, CONNECTED, NOT_CONNECTED, WAITING"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    public abstract OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService);



    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    public abstract  Boolean toSelfFilterAdvertise();

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    public abstract SituationEntry toEntrySituation();
}
