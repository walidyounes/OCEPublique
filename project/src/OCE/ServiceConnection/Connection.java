/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;


import AmbientEnvironment.MockupCompo.MockupService;
import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.Optional;
import java.util.logging.Level;

/**
 * This class represent a connexion between two service agents
 * @author Walid YOUNES
 * @version 1.0
 */
public class Connection implements Comparable {

    private ServiceAgent firstServiceAgent;
    private ServiceAgent secondServiceAgent;
    private MockupService firstService;
    private MockupService secondService;
    private BinderAgent binderAgent;
    private Optional<IConnectionState> myConnectionState;

    /**
     * Construct a connexion
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     */
    public Connection(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
        this.myConnectionState = Optional.empty();
    }

    /**
     * Construct a connexion with an associated binder agent
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     * @param binderAgent           : the binder agent witch is attached to this connexion
     */
    public Connection(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent, BinderAgent binderAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
        this.binderAgent = binderAgent;
        this.myConnectionState = Optional.empty();
    }

    /**
     * Construct a connexion with an associated binder agent
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     * @param firstService          : the first service
     * @param secondService         : the second service
     * @param binderAgent           : the binder agent witch is attached to this connexion
     */
    public Connection(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent, MockupService firstService, MockupService secondService, BinderAgent binderAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
        this.firstService = firstService;
        this.secondService = secondService;
        this.binderAgent = binderAgent;
        this.myConnectionState = Optional.empty();
    }

    /**
     *  Get the first service agent
     * @return the reference of the first service agent
     */
    public ServiceAgent getFirstServiceAgent() {
        return firstServiceAgent;
    }

    /**
     * Set the first service agent which is part of the connexion
     * @param firstServiceAgent : the reference of the first service
     */
    public void setFirstServiceAgent(ServiceAgent firstServiceAgent) {
        this.firstServiceAgent = firstServiceAgent;
    }

    /**
     *  Get the second service agent
     * @return the reference of the second service agent
     */
    public ServiceAgent getSecondServiceAgent() {
        return secondServiceAgent;
    }

    /**
     * Set the second service agent which is part of the connexion
     * @param secondServiceAgent : the reference of the second service
     */
    public void setSecondServiceAgent(ServiceAgent secondServiceAgent) {
        this.secondServiceAgent = secondServiceAgent;
    }

    /**
     * Get the first service agent
     * @return the reference of the first service
     */
    public MockupService getFirstService() {
        return firstService;
    }

    /**
     * Set the first service
     * @param firstService  : the reference of the first service
     */
    public void setFirstService(MockupService firstService) {
        this.firstService = firstService;
    }

    /**
     * Get the second service
     * @return the reference of the second service
     */
    public MockupService getSecondService() {
        return secondService;
    }

    /**
     * Set the second service
     * @param secondService : the reference of the second service
     */
    public void setSecondService(MockupService secondService) {
        this.secondService = secondService;
    }

    /**
     * Get the binder agent attached to this connexion
     * @return the reference of the binder agent
     */
    public BinderAgent getBinderAgent() {
        return binderAgent;
    }


    /**
     * Set the binder agent for this connexion
     * @param binderAgent   :   the reference of the binder agent
     */
    public void setBinderAgent(BinderAgent binderAgent) {
        this.binderAgent = binderAgent;
    }

    /**
     * Check if this connexion is between the two service with the name send in parameters, (the description of the service has the format "name-Type-Owner")
     * @param idFirstService    : the id of the first service
     * @param idSecondService   : the id of the second service
     */
    public boolean isTheSameServices(String idFirstService, String idSecondService){
        //Get the string representation of the two services containing in this connexion
        String thisIDFirstService = "" + this.firstService.getName() + this.firstService.getMatchingID() + this.firstService.getOwner()+this.firstService.getWay();
        String thisIDSecondService = "" + this.secondService.getName() + this.secondService.getMatchingID() +this.secondService.getOwner()+this.secondService.getWay();
        //Check if the two representation of the two local services are equal to those send in parameters
        return thisIDFirstService.equals(idFirstService) && thisIDSecondService.equals(idSecondService);
    }

    /**
     * Check if this connection concern the service send as parameter
     * @param service : the service
     * @return true if this connection contains the service "service"
     */
    public boolean containService(MockupService service){
        return this.firstService.equals(service) || this.secondService.equals(service);
    }

    /**
     * Get the state of this connection
     * @return the state of the connection if it exists
     */
    public Optional<IConnectionState> getMyConnectionState() {
        return myConnectionState;
    }

    /**
     * Set the state's value of this connection
     * @param myConnectionState : the new value
     */
    public void setMyConnectionState(IConnectionState myConnectionState) {
        this.myConnectionState = Optional.of(myConnectionState);
    }

    /**
     * Treat the connection according to her state (accepted, rejected or modified)
     */
    public void toSelfTreat(){
        if (this.myConnectionState.isPresent()){
            //Todo :  15/11/2019 : complete this section
            //this.myConnectionState.get().treatConnection(this, );
        }else{
            OCELogger.log(Level.INFO, "Connection without a state, it can't be treated !");
        }
    }
    /**
     * Compare two connexions (the comparison is compute on all the attribute of the connexion)
     * @param o the connexion to compare this one
     * @return true if the two object are equal, false else
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;

        Connection that = (Connection) o;
        ServiceAgent thatFirstSserviceAgent = that.getFirstServiceAgent();
        ServiceAgent thatSecondSserviceAgent = that.getSecondServiceAgent();
        BinderAgent thatBinderAgent = that.getBinderAgent();
        MockupService thatFirstService = that.getFirstService();
        MockupService thatSecondService = that.getSecondService();

        return this.firstServiceAgent.equals(thatFirstSserviceAgent) && this.secondServiceAgent.equals(thatSecondSserviceAgent) && this.binderAgent.equals(thatBinderAgent) && this.firstService.equals(thatFirstService) && this.secondService.equals(thatSecondService);
    }


    /**
     *  Compare two connexions (the comparison is compute on both the first agent and the second agent)
     * @param athat the connexion to compare this one
     * @return 0 if the two object are equal
     */
    @Override
    public int compareTo(Object athat) {
        Connection that = (Connection) athat;
        ServiceAgent thatFirstServiceAgent = that.getFirstServiceAgent();
        ServiceAgent thatSecondServiceAgent = that.getSecondServiceAgent();
        BinderAgent thatBinderAgent = that.getBinderAgent();

        if (this.firstServiceAgent.compareTo(thatFirstServiceAgent)==0 && this.secondServiceAgent.compareTo(thatSecondServiceAgent)==0 ) return 0;
        else return -1;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "{ ("+this.firstServiceAgent + ", "+ this.secondServiceAgent+") - ( "+ this.firstService + ", "+this.secondService + ") - ( "+ this.binderAgent+ ") - ( " + this.myConnectionState + ") }";
    }
}
