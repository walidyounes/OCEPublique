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
     * set the agent (in the infrastructure) which is associated to this service agent
     * @param myInfrastructureAgent : the associated agent
     */
    public void setMyInfrastructureAgent(InfrastructureAgent myInfrastructureAgent) {
        this.myInfrastructureAgent = myInfrastructureAgent;
    }

    /**
     * get the associated agent (un the infrastructure) to this serviceAgent
     * @return the associated agent
     */
    public InfrastructureAgent getMyInfrastructureAgent() {
        return myInfrastructureAgent;
    }

    /**
     * set the perception mechanism of this agent
     * @param myWayOfPerception the perception mechanism
     */
    public void setMyWayOfPerception(IPerceptionState myWayOfPerception) {
        this.myWayOfPerception = myWayOfPerception;
    }

    /**
     * set the decision mechanism of the agent
     * @param myWayOfDecision  the decision mechanism
     */
    public void setMyWayOfDecision(IDecisionState myWayOfDecision) {
        this.myWayOfDecision = myWayOfDecision;
    }

    /**
     * set the action mechanism of the agent
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

}
