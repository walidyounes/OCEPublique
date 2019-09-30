/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;

/**
 * This class implement the agent responsable of binding the services associated to two ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class BinderAgent extends OCEAgent {


    public BinderAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
    }

    /**
     * Change the Id of the service agent, it's used so that the OCE agent have the same ID as the Infrastructure Agent's ID
     * @param newIDAgent : the new ID
     */
    public void setMyIDAgent(IDAgent newIDAgent){
        this.myID = newIDAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinderAgent)) return false;

        BinderAgent that = (BinderAgent) o;

        return myID.equals(that.myID);
    }

    @Override
    public String toString() {
        return this.myID.toString();
    }
}
