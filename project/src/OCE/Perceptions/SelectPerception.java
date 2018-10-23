/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.AgreeDecision;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Decisions.BindDecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class SelectPerception extends AbstractPerception {
    private BinderAgent binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param receivers the references of the receivers of the selection message
     * @param binderAgent the agent responsible of executing the physical binding
     */
    public SelectPerception(OCEAgent emitter, ArrayList<OCEAgent> receivers, BinderAgent binderAgent) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.binderAgent = binderAgent;
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
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, OCEAgentRef + " treats a selection message ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            // Send a agree message to the emitter of this message
            ArrayList<OCEAgent> agreeReceivers = new ArrayList<>();
            agreeReceivers.add(this.emitter);
            AgreeDecision agreeDecision=  new AgreeDecision(OCEAgentRef, agreeReceivers);
            // Add the reference contact of the Binder Agent
            agreeDecision.setBinderAgent(binderAgent);

            return agreeDecision;
        }

        return null;
    }
}
