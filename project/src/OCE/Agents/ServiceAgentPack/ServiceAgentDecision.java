/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.BestScoreEpsilonGreedy;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.Agents.ServiceAgentPack.Learning.*;
import OCE.Decisions.AdvertiseDecision;
import OCE.Decisions.DoNothingDecision;
import OCE.Decisions.OCEDecision;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.FeedbackMessage;
import OCE.OCEMessages.FeedbackValues;
import OCE.OCEMessages.OCEMessage;
import OCE.Selection.IMessageSelection;
import OCE.Tools.Criteria;
import OCE.Tools.FilterTool.MatchingAdvertiseCriteria;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentDecision implements IDecisionState {

    private IMessageSelection selectionMessageStrategy;
    private ServiceAgent myServiceAgent;
    private IRecord referenceResolver;
    private Map.Entry<IDAgent, ScoredCurrentSituationEntry> cycleBestAgent;
    private int waitingCycleAgentBeforeAdvertize = 0; // the number of cycle to wait by the agent before advertising again
    private final double initialValue = 0.0; //The value used to initialise the score

    public ServiceAgentDecision(IMessageSelection selectionMessageStrategy, ServiceAgent myServiceAgent, IRecord referenceResolver) {
        this.selectionMessageStrategy = selectionMessageStrategy;
        this.myServiceAgent = myServiceAgent;
        this.referenceResolver = referenceResolver;
        this.cycleBestAgent = null;
    }

    /**
     * Get the service Agent that this component is part of
     * @return the reference of the service agent which own this decision module
     */
    public ServiceAgent getMyServiceAgent() {
        return myServiceAgent;
    }

    /**
     * Update the service Agent which this component is part of
     * @param myServiceAgent :  the service agent
     */
    public void setMyServiceAgent(ServiceAgent myServiceAgent) {

        this.myServiceAgent = myServiceAgent;
    }

    /**
     * Update the component which is in charge of resolving the reference
     * @param referenceResolver : the recorder component of the medium
     */
    public void setReferenceResolver(IRecord referenceResolver) {
        this.referenceResolver = referenceResolver;
    }

    /**
     *  Implement the decision mechanism of the binder agent, and produce a list of decisions
     */
    @Override
    public ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perception) {
        // Filter the advertisement (if they exist) and keep only those who matches
        IMatching matching = new Matching();

        Criteria matchingAdvertiseCriteria = new MatchingAdvertiseCriteria(myServiceAgent.getHandledService(), matching);
        ArrayList<InfraMessage> filtredPerception = matchingAdvertiseCriteria.meetCriteria(perception);

        /*
        InfraMessage infraMessageSelected;
        if(filtredPerception.size()>0){
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(filtredPerception);
        }else{
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(perception);
        }
        */

        //Create a list of decisions
        ArrayList<OCEDecision> myListOfDecisions = new ArrayList<>();
        // If the agent isn't in sleep Mode -> if it's the case the service attached to it disappeared ->  for now the agent doesn't do anything
        if(this.myServiceAgent.getMyConnexionState()!= ServiceAgentConnexionState.Sleep) {
            //If the agent received the feedback and we already selected the best agent we update the score
            if (this.myServiceAgent.isFeedbackReceived() && this.cycleBestAgent != null) {
                MyLogger.log(Level.INFO, "Agent : Decision -> Updating Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
                double reinforcement = 0.0;
                double beta = 1;
                double alpha = 0.4; // Learning rate
                if (this.myServiceAgent.getFeedbackValue() == FeedbackValues.VALIDATED) reinforcement = +beta;
                else reinforcement = -beta;
                SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation(), this.cycleBestAgent.getKey(), alpha, reinforcement);
                SituationUtility.normalizeScoresSCS(this.myServiceAgent.getMyScoredCurrentSituation());
                // set the feedback received to false
                this.myServiceAgent.setFeedbackReceived(false);
                MyLogger.log(Level.INFO, "Agent : Decision -> Updated Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
                //Update the agent Knowledge base
                this.myServiceAgent.updateMyKnowledgeBase();
                MyLogger.log(Level.INFO, "Agent : Decision -> Knowledge Base = " + this.myServiceAgent.getMyKnowledgeBase().toString());
                //Reinitialize the cycle number of the agent
                this.myServiceAgent.setMyCurrentCycleNumber(0);
                // the agent will do nothing Todo: this part also can be enhanced later no
                myListOfDecisions.add(new DoNothingDecision());
            } else {
                // Check if the service agent didn't receive any messages (empty perception)
                if (filtredPerception.isEmpty()) {
                    if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.Created)) { // if the service agent was just created -> it advertises
                        //Create an advertisement decision todo : this could be enhanced later on
                        OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                        myListOfDecisions.add(myDecision);
                        //Increment the current cycle number of the agent
                        //this.myServiceAgent.incrementMyCurrentCycleNumber();
                    } else {
                        this.waitingCycleAgentBeforeAdvertize++; //Increment the cycle Agent
                        MyLogger.log(Level.INFO, "Agent -> Decision -> Value Cycle =  " + this.waitingCycleAgentBeforeAdvertize);
                        if (waitingCycleAgentBeforeAdvertize ==3){ // After 3 cycles we advertise if we are not connected
                            if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.NotConnected)){
                                MyLogger.log(Level.INFO, "Agent -> Decision -> is Advertizing !");
                                // the agent will do nothing Todo: this part also can be enhanced later no
                                OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                                myListOfDecisions.add(myDecision);
                                //Increment the current cycle number of the agent
                                //this.myServiceAgent.incrementMyCurrentCycleNumber();
                            }else{
                                // the agent will do nothing Todo: this part also can be enhanced later no
                                myListOfDecisions.add(new DoNothingDecision());
                                //Increment the current cycle number of the agent
                                //this.myServiceAgent.incrementMyCurrentCycleNumber();
                            }
                            this.waitingCycleAgentBeforeAdvertize = 0; // Reinitialize the value of the cycle
                        }else{
                            // the agent will do nothing Todo: this part also can be enhanced later no
                            myListOfDecisions.add(new DoNothingDecision());
                            //Increment the current cycle number of the agent
                            //this.myServiceAgent.incrementMyCurrentCycleNumber();
                        }
                    }
                } else {
                    this.waitingCycleAgentBeforeAdvertize = 0; // Reinitialize the value of the cycle
                    // ********** Version before learning
                    //            //Select a message to treat
                    //            InfraMessage infraMessageSelected;
                    //            IMessageSelection messageSelection = new PrioritySelection(myServiceAgent.getHandledService(), matching);
                    //            infraMessageSelected = messageSelection.singleSelect(perception);
                    //            //Treat the selected message
                    //            OCEMessage perceptionSelected = infraMessageSelected.toOCEMessage(referenceResolver);
                    //            OCEDecision myDecision = perceptionSelected.toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
                    //            myListOfDecisions.add(myDecision);


                    // ********** Version After Learning
                    //transform the Infrastructure messages to OCEMessages
                    List<OCEMessage> OCEPerception = filtredPerception.stream().map(m -> m.toOCEMessage(referenceResolver)).collect(Collectors.toList());
                    //Check if we received the feedback message from the agent binder
                    List<OCEMessage> OCEPerceptionFeedback = OCEPerception.stream().filter(m -> m instanceof FeedbackMessage).collect(Collectors.toList());

                    if(!OCEPerceptionFeedback.isEmpty()){ // The agent received the feedback message
                        //Get the feedback Message -> index 0 cause there is only one
                        OCEDecision myDecision = OCEPerceptionFeedback.get(0).toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
                        // myListOfDecisions.add(new DoNothingDecision()); // Todo : delete just for test
                        myListOfDecisions.add(myDecision);
                    }else{
                        //Create a mapping of the agent's id and their messages
                        Map<IDAgent, OCEMessage> OCEPerceptionSortedByID = OCEPerception.stream().collect(Collectors.toMap(OCEMessage::getIDEmitter, s -> s, (x, y) -> y));

                        //Create the current situation
                        //Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                        //check if it's the start of the agent cycle
                        if (this.myServiceAgent.getMyCurrentCycleNumber() == 0) {
                            // Start of the cycle
                            // We initialise a new current situation
                            this.myServiceAgent.setMyCurrentSituation(new Situation<CurrentSituationEntry>(OCEPerception));
                        } else {
                            // we update the current one
                            Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                            this.myServiceAgent.getMyCurrentSituation().getMySetAgents().putAll(myCurrentSituation.getMySetAgents());
                        }
                        MyLogger.log(Level.INFO, "Agent : Decision -> Current Situation = " + this.myServiceAgent.getMyCurrentSituation().toString());
                        //Check for similar Reference Situation
                        //Define the threshold for selecting similar reference situation
                        double threshold = 0.3;
                        Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation(), this.myServiceAgent.getMyKnowledgeBase(), threshold);
                        MyLogger.log(Level.INFO, "Agent : Decision -> The list of RS selected with a threshold '" + threshold + "' = " + listSimilarRS.toString());
                        //Score the current situation
                        //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
                        Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation(), listSimilarRS, initialValue);
                        this.myServiceAgent.setMyScoredCurrentSituation(myScoredCurrentSituation);
                        MyLogger.log(Level.INFO, "Agent : Decision -> The scored current situation = " + myScoredCurrentSituation.toString());

                        //Select the best agent to respond to from the scored current situation
                        double epsilon = 0.2;
                        IAgentSelectionStrategy agentSelectionStrategy = new BestScoreEpsilonGreedy(epsilon);
                        this.cycleBestAgent = SituationUtility.selectBestAgent(myScoredCurrentSituation, agentSelectionStrategy);
                        MyLogger.log(Level.INFO, " Agent : Decision -> Using the maximum score and epsilon greedy strategy (" + epsilon + "), the best agent = " + this.cycleBestAgent);
                        //Todo : convert the situation entry selected to OCE message
                        //Get from the map the corresponding OCEMessage corresponding to the agent that has been selected
                        OCEMessage bestOCEMessage;
                        try {
                             bestOCEMessage = OCEPerceptionSortedByID.get(this.cycleBestAgent.getKey());
                            OCEDecision myDecision = bestOCEMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
                            myListOfDecisions.add(myDecision);
                        }catch (NullPointerException e){
                            System.out.println(" Best Agent  = "+ this.cycleBestAgent);
                            System.out.println(" Best Message agent  = "+ this.cycleBestAgent);
                            System.out.println(" ConnexionState  = "+ this.myServiceAgent.getMyConnexionState());
                            System.out.println(" Current Service agent  = "+ this.myServiceAgent);
                            System.out.println(" Le service géré = "+ this.myServiceAgent.getHandledService());
                            e.printStackTrace();
                        }

                        //Increment the current cycle number of the agent
                        this.myServiceAgent.incrementMyCurrentCycleNumber();
                    }
                }
            }
        }else{
            MyLogger.log(Level.INFO, "Agent : Decision -> is in SLEEP mode !");
        }
        MyLogger.log(Level.INFO, "Agent : Decision -> List of decisions = " + myListOfDecisions.toString());
        return myListOfDecisions;
    }
 }
