/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import MASInfrastructure.Agent.InfrastructureAgent;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;

public abstract class OCEAgent {
    protected InfrastructureAgent myInfrastructureAgent;
    protected IDAgent myID;
    protected IPerceptionState myWayOfPerception;
    protected IDecisionState myWayOfDecision;
    protected IActionState myWayOfAction;
    protected OCEAgent mySelectedAgent; // The agent that is selected to connect with, it's used to prevent agents that chooses each other but they are blocked waiting for the answer

    /**
     * Get the unique identifier of this agent
     * @return the ID of this InfrastructureAgent
     */
    public IDAgent getMyID() {
        return myID;
    }

    /**
     * Set the agent (in the infrastructure) which is associated to this service agent
     * @param myInfrastructureAgent : the associated agent
     */
    public void setMyInfrastructureAgent(InfrastructureAgent myInfrastructureAgent) {
        this.myInfrastructureAgent = myInfrastructureAgent;
    }

    /**
     * Get the associated agent (un the infrastructure) to this serviceAgent
     * @return the associated agent
     */
    public InfrastructureAgent getMyInfrastructureAgent() {
        return myInfrastructureAgent;
    }

    /**
     * Set the reference of the perception component of the service agent
     * @param myWayOfPerception the perception mechanism
     */
    public void setMyWayOfPerception(IPerceptionState myWayOfPerception) {
        this.myWayOfPerception = myWayOfPerception;
    }

    /**
     * Set the reference of the decision component of the service agent
     * @param myWayOfDecision  the decision mechanism
     */
    public void setMyWayOfDecision(IDecisionState myWayOfDecision) {
        this.myWayOfDecision = myWayOfDecision;
    }

    /**
     * Get the reference of the decision component of the service agent
     * @return
     */
    public IDecisionState getMyWayOfDecision() {
        return myWayOfDecision;
    }

    /**
     * Set the reference of the action component of the service agent
     * @param myWayOfAction the action mechanism
     */
    public void setMyWayOfAction(IActionState myWayOfAction) {
        this.myWayOfAction = myWayOfAction;
    }

    public OCEAgent getMySelectedAgent() {
        return mySelectedAgent;
    }

    public void setMySelectedAgent(OCEAgent mySelectedAgent) {
        this.mySelectedAgent = mySelectedAgent;
    }

    /**
     * Reset the set of attributes of this agent to factory settings
     */
    public abstract void resetToFactoryDefaultSettings();
}
