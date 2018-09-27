/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class AgreePerception extends AbstractPerception {

    /**
     * Creer un message d'acceptation
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
        MyLogger.log(Level.INFO, "Treating a agreement message ! ");
        return null;
    }
}
