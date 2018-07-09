/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Infrastructure.Agent.Agent;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;

/**
 * This class implement the agent responsable of a physical service
 * @author Walid YOUNES
 * @version 1.0
 */

public class ServiceAgent  {
    IPerceptionState myWayOfPerception;
    IDecisionState myWayOfDecision;
    IActionState myWayOfAction;
    Agent myAssociatedAgent;

    public ServiceAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.myAssociatedAgent = null;
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

    /**
     * set the agent (in the infrastructure) which is associated to this service agent
     * @param myAssociatedAgent : the associated agent
     */
    public void setMyAssociatedAgent(Agent myAssociatedAgent) {
        this.myAssociatedAgent = myAssociatedAgent;
    }

    /**
     * get the associated agent (un the infrastructure) to this serviceAgent
     * @return the associated agent
     */
    public Agent getMyAssociatedAgent() {
        return myAssociatedAgent;
    }
}
