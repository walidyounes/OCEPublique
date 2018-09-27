/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import MASInfrastructure.Agent.InfraAgent;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;

public abstract class OCEAgent {
    protected InfraAgent myInfraAgent;
    protected IDAgent myID;
    protected IPerceptionState myWayOfPerception;
    protected IDecisionState myWayOfDecision;
    protected IActionState myWayOfAction;

    /**
     * Get the unique identifier of this agent
     * @return the ID of this InfraAgent
     */
    public IDAgent getMyID() {
        return myID;
    }

    /**
     * set the agent (in the infrastructure) which is associated to this service agent
     * @param myInfraAgent : the associated agent
     */
    public void setMyInfraAgent(InfraAgent myInfraAgent) {
        this.myInfraAgent = myInfraAgent;
    }

    /**
     * get the associated agent (un the infrastructure) to this serviceAgent
     * @return the associated agent
     */
    public InfraAgent getMyInfraAgent() {
        return myInfraAgent;
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

}
