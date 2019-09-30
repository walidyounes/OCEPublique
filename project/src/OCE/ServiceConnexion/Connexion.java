/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnexion;


import AmbientEnvironment.MockupCompo.MockupService;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

/**
 * This class represent a connexion between two service agents
 * @author Walid YOUNES
 * @version 1.0
 */
public class Connexion implements Comparable {

    private ServiceAgent firstServiceAgent;
    private ServiceAgent secondServiceAgent;
    private MockupService firstService;
    private MockupService secondService;
    private BinderAgent binderAgent;

    /**
     * Construct a connexion
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     */
    public Connexion(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
    }

    /**
     * Construct a connexion with an associated binder agent
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     * @param binderAgent           : the binder agent witch is atached to this connexion
     */
    public Connexion(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent, BinderAgent binderAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
        this.binderAgent = binderAgent;
    }

    /**
     * Construct a connexion with an associated binder agent
     * @param firstServiceAgent     : the first service agent responsible of the first service
     * @param secondServiceAgent    : the second service agent responsible of the second service
     * @param firstService          : the first service
     * @param secondService         : the second service
     * @param binderAgent           : the binder agent witch is atached to this connexion
     */
    public Connexion(ServiceAgent firstServiceAgent, ServiceAgent secondServiceAgent, MockupService firstService, MockupService secondService, BinderAgent binderAgent) {
        this.firstServiceAgent = firstServiceAgent;
        this.secondServiceAgent = secondServiceAgent;
        this.firstService = firstService;
        this.secondService = secondService;
        this.binderAgent = binderAgent;
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
        String thisIDFirstService = "" + this.firstService.getName() + this.firstService.getType() + this.firstService.getOwner()+this.firstService.getWay();
        String thisIDSecondService = "" + this.secondService.getName() + this.secondService.getType() +this.secondService.getOwner()+this.secondService.getWay();
        //Check if the two representation of the two local services are equal to those send in parameters
        return thisIDFirstService.equals(idFirstService) && thisIDSecondService.equals(idSecondService);
    }

    /**
     * Compare two connexions (the comparison is compute on all the attribute of the connexion)
     * @param o the connexion to compare this one
     * @return true if the two object are equal, false else
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connexion)) return false;

        Connexion that = (Connexion) o;
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
        Connexion that = (Connexion) athat;
        ServiceAgent thatFirstServiceAgent = that.getFirstServiceAgent();
        ServiceAgent thatSecondServiceAgent = that.getSecondServiceAgent();
        BinderAgent thatBinderAgent = that.getBinderAgent();

        if (this.firstServiceAgent.compareTo(thatFirstServiceAgent)==0 && this.secondServiceAgent.compareTo(thatSecondServiceAgent)==0 ) return 0;
        else return -1;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "{ ("+this.firstServiceAgent + ", "+ this.secondServiceAgent+") - ( "+ this.firstService + ", "+this.secondService + ") - ( "+ this.binderAgent+ ") }";
    }
}
