/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Agent;


import Environment.OCPlateforme.OCService;
import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

/**
 *
 */
public class Agent {

    private final ReferenceAgent referenceAgent;
    private LifeCyrcle lifeCyrcle;
    private OCService handledService;

    public Agent(IEtat initialState, OCService handledService) {
        // Create a unique ID for the Agent
        this.referenceAgent = new ReferenceAgent();
        //Attach the service to handle by this agent
        this.handledService = handledService;
        //Create the life cyrcle of the agent
        this.lifeCyrcle = new LifeCyrcle(initialState, this.referenceAgent);
    }

    /**
     * run the agent's behavior charecterised by his life circle
     */
    public void run() {
        this.lifeCyrcle.run();
    }

    /**
     * get the references id of the agent
     * @return the unique reference of the agent
     */
    public ReferenceAgent getReferenceAgent() {
        return referenceAgent;
    }

    /**
     * get the current state of the agent
     * @return the current state
     */
    public IEtat getState() {
        return lifeCyrcle.getCurrentState();
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
                "referenceAgent=" + referenceAgent +
                '}';
    }
}
