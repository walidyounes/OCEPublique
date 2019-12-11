/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.ReferenceSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;
import OCE.OCEMessages.FeedbackValues;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.logging.Level;

/**
 * This class implement the agent responsible of a physical service
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgent extends OCEAgent implements Comparable {


    private OCService handledService;                                           // The service that the agent is responsible for
    private ServiceAgentConnexionState myConnexionState;                        // The state of the service agent
    private Optional<ServiceAgent> connectedTo;                                 // The reference for the service agent which are connected to, it's set to null if the agent is not connected
    private IOCEBinderAgentFactory myBinderAgentFactory;                        // The reference for the binder agent factory
    private Optional<Situation<CurrentSituationEntry>> myCurrentSituation;                // The current situation under construction
    private Optional<Situation<ScoredCurrentSituationEntry>> myScoredCurrentSituation;    // The scored current situation of the agent
    private Set<Situation<ReferenceSituationEntry>> myKnowledgeBase;            // The agent's knowledge base
    private int myCurrentCycleNumber;                                           // Todo just for the test of the presentation issue of an assembly to the user
    private boolean feedbackReceived;                                           // The variable to indicate whether the agent received the feedback or not
    private FeedbackValues feedbackValue;                                       // The feedback value
    private Optional<BinderAgent> myBinderAgent;                                // The binder agent

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
        this.myInfrastructureAgent = null;
        this.myConnexionState = ServiceAgentConnexionState.Created;
        this.connectedTo = Optional.empty(); //Initialised the attribute to empty cause the agent start not connected
        this.myBinderAgent = Optional.empty(); //Initialised the attribute to empty cause the agent doesn't have a binder agent yet
        //Initialise at null it means that it's the start of an engine cycle
        this.myCurrentSituation = Optional.empty();
        this.myScoredCurrentSituation = Optional.empty();
        this.myCurrentCycleNumber = 0;
        //Todo : change implementation to add uploading of old knowledge
        this.myKnowledgeBase = new HashSet<>();
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
        this.myInfrastructureAgent = null;
        this.myConnexionState = ServiceAgentConnexionState.Created;
        this.connectedTo = Optional.empty(); //Initialised the attribute to empty cause the agent start not connected
        this.myBinderAgent = Optional.empty(); //Initialised the attribute to empty cause the agent doesn't have a binder agent yet
        //Initialise to Empty it means that it's the start of an engine cycle
        this.myCurrentSituation = Optional.empty();
        this.myScoredCurrentSituation =  Optional.empty();
        this.myCurrentCycleNumber = 0;
        //Todo : change implementation to add uploading of old knowledge
        this.myKnowledgeBase = new HashSet<>();
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
     * Get the Factory which allow th creation of a Binder Agent
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
     * @return the reference of the current situation or "emtpy" if it's the beginning of an engine cycle
     */
    public Optional<Situation<CurrentSituationEntry>> getMyCurrentSituation() {
        return myCurrentSituation;
    }

    /**
     * Set the new current situation
     * @param myCurrentSituation : the new current situation
     */
    public void setMyCurrentSituation(Situation<CurrentSituationEntry> myCurrentSituation) {
        this.myCurrentSituation = Optional.ofNullable(myCurrentSituation);
    }

    /**
     * reset the value of the current situation to default value (empty)
     */
    public void resetMyCurrentSituation() {
        this.myCurrentSituation = Optional.empty();
    }

    /**
     * Get the scored current situation
     * @return the reference of the scored current situation or "empty" if it's the beginning of an engine cycle
     */
    public Optional<Situation<ScoredCurrentSituationEntry>> getMyScoredCurrentSituation() {
        return myScoredCurrentSituation;
    }

    /**
     * set a new scored current situation for the agent
     * @param myScoredCurrentSituation : the new scored current situation
     */
    public void setMyScoredCurrentSituation(Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation) {
        this.myScoredCurrentSituation = Optional.ofNullable(myScoredCurrentSituation);
    }

    /**
     * reset the value of the scored current situation to default value (empty)
     */
    public void resetMyScoredCurrentSituation() {
        this.myScoredCurrentSituation = Optional.empty();
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
     * Increment by 1 the value of the current cycle number of this agent
     */
    public void incrementMyCurrentCycleNumber(){
        this.myCurrentCycleNumber ++;
    }

    /**
     * Get the reference of the service agent to who this agent is connected to
     * @return if this agent is connected then it return the reference of the other service agent, otherwise it returns "Empty"
     */
    public Optional<ServiceAgent> getConnectedTo() {
        return connectedTo;
    }

    /**
     * Set the value for the attribute expressing with whom this agent is connected to
     * @param connectedTo : the new value to update
     */
    public void setConnectedTo(ServiceAgent connectedTo) {
        this.connectedTo = Optional.ofNullable(connectedTo);
    }

    /**
     * Reset to default "empty" the value for the field "connectedTo" expressing with whom this agent is connected to
     */
    public void resetConnectedTo() {
        this.connectedTo = Optional.empty();
    }

    /**
     * Get the content of the knowledge base of the service agent
     * @return the list of reference situation that construct the agent's knowledge base
     */
    public Set<Situation<ReferenceSituationEntry>> getMyKnowledgeBase() {
        return myKnowledgeBase;
    }

    /**
     * Set a new content for the service agent"s knowledge base
     * @param myKnowledgeBase : the new knowledge base
     */
    public void setMyKnowledgeBase(Set<Situation<ReferenceSituationEntry>> myKnowledgeBase) {
        this.myKnowledgeBase = myKnowledgeBase;
    }

    /**
     * Transform the scored current situation to a reference situation and store it in the database
     */
    public void updateMyKnowledgeBase(){
        //If the scored current situation exist
        if(this.myScoredCurrentSituation.isPresent()){
            //Transform the scored current situation to a reference situation
            Situation<ReferenceSituationEntry> referenceSituationToAdd = new Situation<>();
            Map<IDAgent, ScoredCurrentSituationEntry> setScoredCurrentSituationEntry = this.myScoredCurrentSituation.get().getAgentSituationEntries();
            // Transform the scored situation entry to a reference situation entry
            for(IDAgent idAgent : setScoredCurrentSituationEntry.keySet()){
                referenceSituationToAdd.addSituationEntry(idAgent, setScoredCurrentSituationEntry.get(idAgent).toReferenceSituationEntry());
            }
            // If the reference situation doesn't exists in the database ( i.e : their is no RS in database with the same IDAgents and the same score)
            if (!this.myKnowledgeBase.contains(referenceSituationToAdd)){
                //Check if the reference situation exists in the database ( it exists a reference situation with the same agents BUT NOT THE SAME SCORES)
                boolean found=false;
                Situation<ReferenceSituationEntry> duplicatedReferenceSituation = null;
                int iteratorIndex = 0;
                Iterator<Situation<ReferenceSituationEntry>> customIterator  = this.myKnowledgeBase.iterator();
                // Get the set of IDAgent of the reference situation to be added to the knowledgeDatabase
                Set<IDAgent> referenceSIDAgentSet = referenceSituationToAdd.getAgentSituationEntries().keySet();
                while(iteratorIndex < this.myKnowledgeBase.size() && !found){
                    Situation<ReferenceSituationEntry> currentReferenceSituation = customIterator.next();
                    //Get the Set of IDAgents of the current RS and compare them
                    Set<IDAgent> currentRSIDAgentSet = currentReferenceSituation.getAgentSituationEntries().keySet();
                    if (referenceSIDAgentSet.equals(currentRSIDAgentSet)){
                        found=true;
                        // Save the reference situation
                        duplicatedReferenceSituation = currentReferenceSituation;
                    }
                    iteratorIndex++;
                }
                //If the duplicated reference situation was found
                if(found){
                    //We delete from the database the old reference Situation (which may have an old scores values)
                    this.myKnowledgeBase.remove(duplicatedReferenceSituation);
                    //Add the reference Situation containing the new score values
                    this.myKnowledgeBase.add(referenceSituationToAdd);
                }else {
                    //Add the reference Situation containing the new score values
                    this.myKnowledgeBase.add(referenceSituationToAdd);
                }
            }else{
                //Add the reference situation to the database
                this.myKnowledgeBase.add(referenceSituationToAdd);
            }
            //Reinitialise the situation attribute in the agent
            this.myScoredCurrentSituation = Optional.empty();
            this.myCurrentSituation = Optional.empty();
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
     * Set the value of the received feedback "ACCEPTED" or "REJECTED"
     * @param feedbackValue : the value of the feedback
     */
    public void setFeedbackValue(FeedbackValues feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    /**
     * Launch the suicide mechanism of the agent
     */
    public void suicide(){
        OCELogger.log(Level.INFO, " The agent = " + this.toString() + " is committing SUICIDE !");
        //Check if the agent is connected
        if(this.myConnexionState.equals(ServiceAgentConnexionState.Connected) && this.connectedTo.isPresent()){
            //Todo : walid 29 11H55 on rentre pas ici je pense que le service connected to n'existe pas
            OCELogger.log(Level.INFO, " The agent = " + this.toString() + " is ready to SUICIDE!");
            //Put the indicator of suicide to true so in the decision process, the agent will send a disconnect message
            ((ServiceAgentDecision)this.myWayOfDecision).setCommitSuicide(true);
        }
        //Save the knowledge of the service agent | Todo : For now we don't do it -> we put to sleep

        //Put the serviceAgent to sleep
        this.myConnexionState = ServiceAgentConnexionState.Sleep;
        OCELogger.log(Level.INFO, " The agent = " + this.toString() + " is put to SLEEP !");

    }

    /**
     * Get the reference if the binder agent of this service agent
     * @return  : the reference of the binderAgent if it exist or Empty
     */
    public Optional<BinderAgent> getMyBinderAgent() {
        return myBinderAgent;
    }

    /**
     * Set the reference to the binder agent of this service agent
     * @param myBinderAgent : the reference of the binder agent
     */
    public void setMyBinderAgent(BinderAgent myBinderAgent) {
        this.myBinderAgent = Optional.ofNullable(myBinderAgent);
    }

    /**
     * Set the reference to the binder agent of this service agent to Empty (NULL)
     */
    public void deleteMyBinderAgent(){
        this.myBinderAgent = Optional.empty();
    }

    /**
     * Reset the set of attributes of this agent to factory setting except for the knowledge
     */
    public void resetToFactoryDefaultSettings(){
        //Mark  the state of the connection to not connected
        this.setMyConnexionState(ServiceAgentConnexionState.NotConnected);
        //Delete the agent to which this agent is maybe connected to
        this.resetConnectedTo();
        //If the agent has a binder agent delete it
        this.deleteMyBinderAgent();
        //reset the field Current situation and scored current situation
        this.resetMyCurrentSituation();
        this.resetMyScoredCurrentSituation();
        //Reset the number of the cycle of the agent
        this.myCurrentCycleNumber = 0;
        //Reset the field indicating the feedback is received
        this.feedbackReceived = false;
    }

    /**
     *  Compare two Service Agents (the comparison is compute on the handled Service)
     * @param o the service agent to compare this to
     * @return true if the two object are equal, false else
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o== null || getClass() != o.getClass()) return false;
        ServiceAgent that = (ServiceAgent) o;
        return myID.equals(that.myID);
    }

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
        if(athat == null || getClass() != athat.getClass()) return -1;
        if(athat == this) return 0;
        ServiceAgent that = (ServiceAgent) athat;
        return this.myID.compareTo(that.getMyID());
    }

    @Override
    public String toString() {
        return this.myID.toString();
    }
}
