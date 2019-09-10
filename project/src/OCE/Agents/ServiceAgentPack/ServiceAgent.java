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
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.ReferenceSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;
import OCE.OCEMessages.FeedbackValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implement the agent responsible of a physical service
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgent extends OCEAgent implements Comparable {

    private OCService handledService;
    private ServiceAgentConnexionState myConnexionState;
    private IOCEBinderAgentFactory myBinderAgentFactory;
    private Situation<CurrentSituationEntry> myCurrentSituation;
    private Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation;
    private int myCurrentCycleNumber; // Todo just for the test of the presentation issue of an assembly to the user
    private boolean feedbackReceived; // variable to indicate whether the agent received the feedback or not
    private FeedbackValues feedbackValue;
    private List<Situation<ReferenceSituationEntry>> myKnowledgeBase;
    /**
     * Create a service Agent specifying a random ID
     * @param handledService    : the service handled by the agent
     * @param myWayOfPerception : the perception module of the agent
     * @param myWayOfDecision   : the decision module of the agent
     * @param myWayOfAction     : the action module of the agent
     */
    public ServiceAgent(OCService handledService, IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.handledService = handledService;
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.myInfraAgent = null;
        this.myConnexionState = ServiceAgentConnexionState.Created;
        //Initialise at null it means that it's the start of an engine cycle
        this.myCurrentSituation = null;
        this.myScoredCurrentSituation = null;
        this.myCurrentCycleNumber = 0;
        //Todo : change implementation to add uploading of old knowledge
        myKnowledgeBase = new ArrayList<>();
    }

    /**
     * Create a service Agent specifying a custom ID
     * @param idAgent           : the custom ID for the service agent
     * @param handledService    : the service handled by the agent
     * @param myWayOfPerception : the perception module of the agent
     * @param myWayOfDecision   : the decision module of the agent
     * @param myWayOfAction     : the action module of the agent
     */
    public ServiceAgent(IDAgent idAgent, OCService handledService, IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = idAgent;
        this.handledService = handledService;
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.myInfraAgent = null;
        this.myConnexionState = ServiceAgentConnexionState.Created;
        //Initialise at null it means that it's the start of an engine cycle
        this.myCurrentSituation = null;
        this.myScoredCurrentSituation = null;
        this.myCurrentCycleNumber = 0;
        //Todo : change implementation to add uploading of old knowledge
        myKnowledgeBase = new ArrayList<>();
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
     * Change the Id of the service agent, it's used so that the OCE agent have the same ID as the Infrastructure Agent's ID
     * @param newIDAgent : the new ID
     */
    public void setMyIDAgent(IDAgent newIDAgent){
        this.myID = newIDAgent;
    }

    /**
     * Get the current situation of the agent in its current cycle
     * @return the reference of the current situation or null if it's the beginning of an engine cycle
     */
    public Situation<CurrentSituationEntry> getMyCurrentSituation() {
        return myCurrentSituation;
    }

    /**
     * Set the new current situation
     * @param myCurrentSituation : the new current situation
     */
    public void setMyCurrentSituation(Situation<CurrentSituationEntry> myCurrentSituation) {
        this.myCurrentSituation = myCurrentSituation;
    }

    /**
     * Get the scored current situation
     * @return the reference of the scored current situation or null if it's the beginning of an engine cycle
     */
    public Situation<ScoredCurrentSituationEntry> getMyScoredCurrentSituation() {
        return myScoredCurrentSituation;
    }

    /**
     * set a new scored current situation for the agent
     * @param myScoredCurrentSituation : the new scored current situation
     */
    public void setMyScoredCurrentSituation(Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation) {
        this.myScoredCurrentSituation = myScoredCurrentSituation;
    }

    /**
     * Get the current agent cycle value
     * @return the value of the current agent cycle
     */
    public int getMyCurrentCycleNumber() {
        return myCurrentCycleNumber;
    }

    /**
     * Set the value of the current agent cycle
     * @param myCurrentCycleNumber
     */
    public void setMyCurrentCycleNumber(int myCurrentCycleNumber) {
        this.myCurrentCycleNumber = myCurrentCycleNumber;
    }

    /**
     * Get the content of the knowledge base of the service agent
     * @return the list of reference situation that construct the agent's knowledge base
     */
    public List<Situation<ReferenceSituationEntry>> getMyKnowledgeBase() {
        return myKnowledgeBase;
    }

    /**
     * Set a new content for the service agent"s knowledge base
     * @param myKnowledgeBase : the new knowledge base
     */
    public void setMyKnowledgeBase(List<Situation<ReferenceSituationEntry>> myKnowledgeBase) {
        this.myKnowledgeBase = myKnowledgeBase;
    }

    /**
     * Transform the scored current situation to a reference situation and store it in the database
     */
    public void updateMyKnowledgeBase(){
        //If the scored current situation exist
        if(this.myScoredCurrentSituation != null){
            Situation<ReferenceSituationEntry> referenceSituation = new Situation<>();
            Map<IDAgent, ScoredCurrentSituationEntry> setScoredCurrentSituationEntry = this.myScoredCurrentSituation.getMySetAgents();
            // Transform the scored situation entry to a reference situation entry
            for(IDAgent idAgent : setScoredCurrentSituationEntry.keySet()){
                referenceSituation.addSituationEntry(idAgent, setScoredCurrentSituationEntry.get(idAgent).toReferenceSituationEntry());
            }
            //Add the reference situation to the database
            this.myKnowledgeBase.add(referenceSituation);
            //Reinitialise the situation attribute in the agent
            this.myScoredCurrentSituation = null;
        }

    }
    /**
     * Check whether the feedback is received
     * @return true if the agent received the feedback, false otherwise
     */
    public boolean isFeedbackReceived() {
        return feedbackReceived;
    }

    /**
     * Set the whether the feedback received or not
     * @param feedbackReceived : true = feedback received, false = not received
     */
    public void setFeedbackReceived(boolean feedbackReceived) {
        this.feedbackReceived = feedbackReceived;
    }

    /**
     * Get the value of the last feedback received
     * @return the value of the last received feedback
     */
    public FeedbackValues getFeedbackValue() {
        return feedbackValue;
    }

    /**
     * Set the value of the received feedback "VALIDATED" or "REJECTED"
     * @param feedbackValue : the value of the feedback
     */
    public void setFeedbackValue(FeedbackValues feedbackValue) {
        this.feedbackValue = feedbackValue;
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
