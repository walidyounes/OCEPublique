/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Decisions.AbstractDecision;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Decisions.AgreeDecision;
import OCE.Decisions.EmptyDecision;

import java.util.ArrayList;
import java.util.logging.Level;

public class AgreePerception extends AbstractPerception {

    /**
     * create an agreement message
     *
     */
    public AgreePerception(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }

    /**
     * treat the agree message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param oceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent oceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, oceAgentRef + " treats an agreement message ");
        //Verify the connexion state of the agent Todo: walid : il y a un probleme lorsque l'agent se met en attente
        // if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            // change the connexion's state of the agent
            ((ServiceAgent)oceAgentRef).setMyConnexionState(ServiceAgentConnexionState.Waiting);
            MyLogger.log(Level.INFO, oceAgentRef + " is now in waiting state ");
            return new EmptyDecision();
       //  }
        //return null;
    }
    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }
}
