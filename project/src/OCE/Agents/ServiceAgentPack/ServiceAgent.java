/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;

/**
 * This class implement the agent responsable of a physical service
 * @author Walid YOUNES
 * @version 1.0
 */

public class ServiceAgent extends OCEAgent implements Comparable {

    private OCService handledService;
    private ServiceAgentConnexionState myConnexionState;
    private IOCEBinderAgentFactory myBinderAgentFactory;

    public ServiceAgent(OCService handledService, IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.handledService = handledService;
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.myInfraAgent = null;
        this.myConnexionState = ServiceAgentConnexionState.Created;
    }



    /**
     * Get the service handled by the agent
     * @return the handled service
     */
    public OCService getHandledService() {
        return handledService;
    }


    /**
     * Get the current connexion state of this agent
     * @return the connexion state : connected , not connected, created, waiting
     */
    public ServiceAgentConnexionState getMyConnexionState() {
        return myConnexionState;
    }

    /**
     * Update the value of the connexion's state of this agent
     * @param myConnexionState : the new value
     */
    public void setMyConnexionState(ServiceAgentConnexionState myConnexionState) {
        this.myConnexionState = myConnexionState;
    }

    /**
     * Get the Factory wich allow th creation of a Binder Agent
     * @return the BinderAgent Factory
     */
    public IOCEBinderAgentFactory getMyBinderAgentFactory() {
        return myBinderAgentFactory;
    }

    /**
     * Set the reference of the BinderAgent factory of this agent
     * @param myBinderAgentFactory : the reference of the BinderAgentFactory
     */
    public void setMyBinderAgentFactory(IOCEBinderAgentFactory myBinderAgentFactory) {
        this.myBinderAgentFactory = myBinderAgentFactory;
    }

    /**
     *  Compare two Service Agents (the comparison is compute on the handled Service)
     * @param o the service agent to compare this to
     * @return true if the two object are equal, false else
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceAgent)) return false;

        ServiceAgent that = (ServiceAgent) o;

        return myID.equals(that.myID);
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof ServiceAgent)) return false;
//
//        ServiceAgent that = (ServiceAgent) obj;
//
//        return handledService.equals(that.handledService);
//    }

    @Override
    public int hashCode() {
        return myID.hashCode();
    }


    /**
     *  Compare two Service Agents (the comparison is compute on the handled Service)
     * @param athat the service Agent to compare this to
     * @return 0 if the two object are equal
     */
    @Override
    public int compareTo(Object athat) {
        ServiceAgent that = (ServiceAgent) athat;

        return this.myID.compareTo(that.getMyID());
    }

//    @Override
//    public int compareTo(Object obj) {
//        return ((MockupService)this.handledService).compareTo(obj);
//    }


    @Override
    public String toString() {
        return this.myID.toString();
    }
}
