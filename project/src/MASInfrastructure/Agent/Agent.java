/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Agent;


import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;

/**
 *
 */
public class Agent {

    private final AgentReference agentReference;
    private LifeCycle lifeCycle;
    private OCService handledService;

    public Agent(OCService handledService, LifeCycle lifeCycle) {
        // Create a unique ID for the Agent
        this.agentReference = new AgentReference();
        //Attach the service to handle by this agent
        this.handledService = handledService;
        //Create the life cyrcle of the agent
        this.lifeCycle = lifeCycle;
    }

    /**
     * run the agent's behavior charecterised by his life circle
     */
    public void run() {
        this.lifeCycle.run();
    }

    /**
     * get the references id of the agent
     * @return the unique reference of the agent
     */
    public AgentReference getAgentReference() {
        return agentReference;
    }

    /**
     * get the current state of the agent
     * @return the current state
     */
    public IEtat getState() {
        return lifeCycle.getCurrentState();
    }

    /**
     * get the service handled by the agent
     * @return the handled service
     */
    public OCService getHandledService() {
        return handledService;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentReference=" + agentReference +
                '}';
    }
}
