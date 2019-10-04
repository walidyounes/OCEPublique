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
import OCE.Decisions.ARSADecisions.AdvertiseDecision;
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

    //ARSA parameters
    private int waitingCycleAgentBeforeAdvertiseCounter = 0;            //The variable used to count the number of cycle to wait by the agent before advertising again
    private final int getWaitingCycleAgentBeforeAdvertiseBound = 4;     //The number of cycle to wait by the agent before advertising another time
    private int waitingCycleAgentAfterSelectCounter = 0;                //The variable used to count the number of cycle that the agent wait after selecting an other service agent
    private final int waitingCycleAgentAfterSelectBound = 6;            //The number of cycle to wait by the agent for the answer from the selected service agent and the binding agent

    // Learning parameters
    private final double initialValue = 0.0;    //The value used to initialise the score
    private double reinforcement = 0.0;         //The value of the reinforcement
    private final double beta = 1;              //The value used to compute the reinforcement from the feedback
    private final double alpha = 0.4;           //The Learning rate
    private double threshold = 0.3;             //Define the threshold for selecting similar reference situation
    private  double epsilon = 0.2;              //The value of the threshold used by the strategy of selection of best agent

    /**
     * Create the decision component of a service agent
     * @param selectionMessageStrategy  : the strategy used to select messages
     * @param myServiceAgent            : the reference of the service agent that this component is part of
     * @param referenceResolver         : component used to resolve the physical references of the agents
     */
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
        ArrayList<InfraMessage> filteredPerception = matchingAdvertiseCriteria.meetCriteria(perception);

        //Create a list of decisions
        ArrayList<OCEDecision> myListOfDecisions = new ArrayList<>();

        // If the agent isn't in sleep Mode -> if it's the case the service attached to it disappeared ->  for now the agent doesn't do anything
        if(this.myServiceAgent.getMyConnexionState()!= ServiceAgentConnexionState.Sleep) {

            //If the agent received the feedback and we already selected the best agent we update the score
//            if (this.myServiceAgent.isFeedbackReceived() && this.cycleBestAgent != null) {
//
//                MyLogger.log(Level.INFO, "Agent : Decision -> Updating Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
//
//                if (this.myServiceAgent.getFeedbackValue() == FeedbackValues.VALIDATED) reinforcement = +beta;
//                else reinforcement = -beta;
//                SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation(), this.cycleBestAgent.getKey(), alpha, reinforcement);
//                SituationUtility.normalizeScoresSCS(this.myServiceAgent.getMyScoredCurrentSituation());
//                // set the feedback received to false
//                this.myServiceAgent.setFeedbackReceived(false);
//                MyLogger.log(Level.INFO, "Agent : Decision -> Updated Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
//                //Update the agent Knowledge base
//                this.myServiceAgent.updateMyKnowledgeBase();
//                MyLogger.log(Level.INFO, "Agent : Decision -> Knowledge Base = " + this.myServiceAgent.getMyKnowledgeBase().toString());
//                //Reinitialize the cycle number of the agent
//                this.myServiceAgent.setMyCurrentCycleNumber(0);
//                //Put the state of the agent to connected
//                this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.Connected);
//                // the agent will do nothing
//                myListOfDecisions.add(new DoNothingDecision());
//
//            } else {


                //Check if the service agent didn't receive any messages (empty perception)
                if (filteredPerception.isEmpty()) {
                    //If the service agent was just created -> it advertises
                    if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.Created)) {
                        //Create an advertisement decision
                        OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                        //Change the state of the service agent to not connected
                        this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NotConnected);
                        myListOfDecisions.add(myDecision);
                    } else {
                        switch (this.myServiceAgent.getMyConnexionState()){
                            case NotConnected:
                                this.waitingCycleAgentBeforeAdvertiseCounter++; //Increment the value
                                MyLogger.log(Level.INFO, "Agent -> Decision -> Value Cycle =  " + this.waitingCycleAgentBeforeAdvertiseCounter);
                                if (this.waitingCycleAgentBeforeAdvertiseCounter == this.getWaitingCycleAgentBeforeAdvertiseBound) { // After n cycles we advertise again
                                    MyLogger.log(Level.INFO, "Agent -> Decision -> is Advertising !");
                                    //The agent will advertise
                                    OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                                    myListOfDecisions.add(myDecision);
                                    //Reinitialize the value of the cycle
                                    this.waitingCycleAgentBeforeAdvertiseCounter = 0;
                                }else{
                                    //The agent do nothing
                                    myListOfDecisions.add(new DoNothingDecision());
                                }
                                break;

                            case Waiting:
                                this.waitingCycleAgentAfterSelectCounter++; //Increment the value
                                //If the agent reaches the maximum cycle number to wait after selecting an other agent
                                if(this.waitingCycleAgentAfterSelectCounter == this.waitingCycleAgentAfterSelectBound){
                                    //We change the agent's stata to NOT Connected
                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NotConnected);
                                    //Reinitialise the waiting counter after the select to 0
                                    this.waitingCycleAgentAfterSelectCounter=0;
                                }
                                myListOfDecisions.add(new DoNothingDecision());
                                break;

                            default:
                                myListOfDecisions.add(new DoNothingDecision());
                        }

                    }
                } else {
                    // Reinitialize the value of the counters to 0
                    this.waitingCycleAgentBeforeAdvertiseCounter = 0;
                    this.waitingCycleAgentAfterSelectCounter = 0;
                    //Transform the Infrastructure messages to OCEMessages
                    List<OCEMessage> OCEPerception = filteredPerception.stream().map(m -> m.toOCEMessage(referenceResolver)).collect(Collectors.toList());

                    //Check if we received the feedback message from the agent binder (This action is priority)
                    List<OCEMessage> OCEPerceptionFeedback = OCEPerception.stream().filter(m -> m instanceof FeedbackMessage).collect(Collectors.toList());
                    if(!OCEPerceptionFeedback.isEmpty()){ // The agent received the feedback message
                        //Get the feedback Message -> index 0 cause there is only one and treat the feedback message -> for now it returns "do nothing decision"
                        OCEDecision myDecision = OCEPerceptionFeedback.get(0).toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
                        //If the agent received the feedback and we already selected the best agent we update the score
                        if (this.myServiceAgent.isFeedbackReceived() && this.cycleBestAgent != null) {

                            MyLogger.log(Level.INFO, "Agent : Decision -> Before updating Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());

                            //Compute the value of the reinforcement depending on the value of the feedback
                            if (this.myServiceAgent.getFeedbackValue() == FeedbackValues.VALIDATED) reinforcement = +beta;
                            else reinforcement = -beta;
                            //Update the scores in the current scored situation and normalize the values
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
                            //Set the state of the agent to connected
                            this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.Connected);
                        }
                        // the agent will do nothing after this
                        myListOfDecisions.add(myDecision);

                    }else{
                        //If the service agent is not connected
                        if(this.myServiceAgent.getMyConnexionState()== ServiceAgentConnexionState.NotConnected) {
                            //Create a mapping of the agent's id and their messages
                            Map<IDAgent, OCEMessage> OCEPerceptionSortedByID = OCEPerception.stream().collect(Collectors.toMap(OCEMessage::getIDEmitter, s -> s, (x, y) -> y));

                            //Create the current situation
                            //Check if it's the start of the agent cycle
                            if (this.myServiceAgent.getMyCurrentCycleNumber() == 0) {
                                //Start of the cycle // We initialise a new current situation
                                this.myServiceAgent.setMyCurrentSituation(new Situation<CurrentSituationEntry>(OCEPerception));
                            } else {
                                //We update the current one
                                Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                                this.myServiceAgent.getMyCurrentSituation().getMySetAgents().putAll(myCurrentSituation.getMySetAgents());
                            }
                            MyLogger.log(Level.INFO, "Agent : Decision -> Current Situation = " + this.myServiceAgent.getMyCurrentSituation().toString());
                            //Check for similar Reference Situation

                            Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation(), this.myServiceAgent.getMyKnowledgeBase(), threshold);
                            MyLogger.log(Level.INFO, "Agent : Decision -> The list of RS selected with a threshold '" + threshold + "' = " + listSimilarRS.toString());
                            //Score the current situation
                            //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
                            Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation(), listSimilarRS, initialValue);
                            this.myServiceAgent.setMyScoredCurrentSituation(myScoredCurrentSituation);
                            MyLogger.log(Level.INFO, "Agent : Decision -> The scored current situation = " + myScoredCurrentSituation.toString());

                            //Select the best agent to respond to from the scored current situation
                            IAgentSelectionStrategy agentSelectionStrategy = new BestScoreEpsilonGreedy(epsilon);
                            this.cycleBestAgent = SituationUtility.selectBestAgent(myScoredCurrentSituation, agentSelectionStrategy);
                            MyLogger.log(Level.INFO, " Agent : Decision -> Using the maximum score and epsilon greedy strategy (" + epsilon + "), the best agent = " + this.cycleBestAgent);
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
                        }else{
                            //If the service agent is in waiting state
                            if(this.myServiceAgent.getMyConnexionState()== ServiceAgentConnexionState.Waiting){
                                MyLogger.log(Level.INFO, " Agent : Decision -> Waiting : let me check if I received messages from the service agent I selected ");
                                //Check if we received messages from the service agent that we selected before changing the state
                                List<OCEMessage> OCEPerceptionSelectedAgentMessages = OCEPerception.stream().filter(m -> m.getEmitter().getMyID().equals(this.myServiceAgent.getMySelectedAgent().getMyID())).collect(Collectors.toList());

                                if(!OCEPerceptionSelectedAgentMessages.isEmpty()){
                                    MyLogger.log(Level.INFO, " Agent : Decision -> Waiting : I received messages from my selected agent -> i treat them");
                                    //Get the messages and treat them ->
                                    for(int index=0; index < OCEPerceptionSelectedAgentMessages.size(); index++){
                                        OCEDecision myDecision = OCEPerceptionSelectedAgentMessages.get(index).toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
                                        myListOfDecisions.add(myDecision);
                                    }

                                }
                                //Increment the waiting counter
                                this.waitingCycleAgentAfterSelectCounter++;
                                //If the agent reaches the maximum cycle number to wait after selecting an other agent -> we change it's state to NOT Connected
                                if(this.waitingCycleAgentAfterSelectCounter == this.waitingCycleAgentAfterSelectBound){
                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NotConnected);
                                    //Reinitialise the waiting counter after the select to 0
                                    this.waitingCycleAgentAfterSelectCounter=0;
                                }
                            }
                            //Else -> the agent is in Connected state -> for now we DO NOTHING

                            //The agent will do nothing
                            myListOfDecisions.add(new DoNothingDecision());
                        }

                    }
                }
           // }
        }else{
            MyLogger.log(Level.INFO, "Agent : Decision -> is in SLEEP mode !");
        }
        MyLogger.log(Level.INFO, "Agent : Decision -> List of decisions = " + myListOfDecisions.toString());
        return myListOfDecisions;
    }
 }
