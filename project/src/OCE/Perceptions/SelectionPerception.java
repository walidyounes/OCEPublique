/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.BinderAgent;
import OCE.Decisions.AbstractDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class SelectionPerception extends AbstractPerception {
    private BinderAgent binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param recievers the references of the recievers of the selection message
     * @param bindingAgent the agent responsible of executing the physical binding
     */
    public SelectionPerception(ServiceAgent emitter, ArrayList<ServiceAgent> recievers, BinderAgent bindingAgent) {
        this.emitter = emitter;
        this.recievers = recievers;
        this.binderAgent = bindingAgent;
    }

    /**
     * create a Selection message (empty)
     */
    public SelectionPerception() {
        this.emitter = null;
        this.recievers= null;
        this.binderAgent = null;
    }

    /**
     * get the reference of the binding agent
     * @return the reference
     */
    public BinderAgent getBinderAgent() {
        return binderAgent;
    }

    /**
     * set the reference of the binding agent
     * @param binderAgent : the reference of the binding agent
     */
    public void setBinderAgent(BinderAgent binderAgent) {
        this.binderAgent = binderAgent;
    }

    /**
     * treat the selection message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a selection message ! ");
        return null;
    }
}
