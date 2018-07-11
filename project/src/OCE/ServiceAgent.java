/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.Agent;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;

/**
 * This class implement the agent responsable of a physical service
 * @author Walid YOUNES
 * @version 1.0
 */

public class ServiceAgent  {
    private IDAgent myID;
    private IPerceptionState myWayOfPerception;
    private IDecisionState myWayOfDecision;
    private IActionState myWayOfAction;
    private OCService handledService;
    private Agent myAssociatedAgent; // Todo : maybe we will not need it after all ^^

    public ServiceAgent(OCService handledService, IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.handledService = handledService;
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.myAssociatedAgent = null;
    }

    /**
     * Get the unique identifier of this agent
     * @return the ID of this Agent
     */
    public IDAgent getMyID() {
        return myID;
    }

    /**
     * get the service handled by the agent
     * @return the handled service
     */
    public OCService getHandledService() {
        return handledService;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceAgent)) return false;

        ServiceAgent that = (ServiceAgent) o;

        return myID.equals(that.myID);
    }

    @Override
    public int hashCode() {
        return myID.hashCode();
    }
}
