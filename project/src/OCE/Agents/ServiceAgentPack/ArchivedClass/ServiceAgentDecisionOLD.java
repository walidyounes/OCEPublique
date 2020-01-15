/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.ArchivedClass;

import Logger.OCELogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.BestScoreEpsilonGreedy;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.Agents.ServiceAgentPack.Learning.*;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEDecisions.ARSADecisions.AdvertiseDecision;
import OCE.OCEDecisions.DisconnectDecision;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEMessages.*;
import OCE.Tools.Criteria;
import OCE.Tools.FilterTool.MatchingAdvertiseCriteria;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentDecisionOLD implements IDecisionState {

    private ServiceAgent myServiceAgent;                // The reference of the service Agent that this component is part of
    private IRecord oceAgentRecorder;                  // Component used to resolve the infrastructure references for service agents, delete agents 'binder agent'
    private boolean commitSuicide;                          // Boolean variable that's indicate for the agent that it's service disappeared and it is connected so it inform the other service Agent and launch disconnection

    //ARSA parameters
    private int waitingCycleAgentBeforeAdvertiseCounter = 0;            //The variable used to count the number of cycle to wait by the agent before advertising again
    private final int waitingCycleAgentBeforeAdvertiseBound = 10;     //The number of cycle to wait by the agent before advertising another time
    private int waitingCycleAgentAfterSelectCounter = 0;                //The variable used to count the number of cycle that the agent wait after selecting an other service agent
    private final int waitingCycleAgentAfterSelectBound = 10;            //The number of cycle to wait by the agent for the answer from the selected service agent and the binding agent

    // Learning parameters
    private final double initialValue = 0.0;    //The value used to initialise the score
    private double reinforcement = 0.0;         //The value of the reinforcement
    private double beta = 1;              //The value used to compute the reinforcement from the feedback
    private double learningRate = 0.4;           //The Learning rate
    private double similarityThreshold = 0.3;             //Define the similarityThreshold for selecting similar reference situation
    private double epsilon = 0.2;              //The value of the threshold used by the strategy of selection of best agent
    private Map.Entry<IDAgent, ScoredCurrentSituationEntry> cycleBestAgent;

    /**
     * Create the decision component of a service agent
     * @param myServiceAgent            : the reference of the service agent that this component is part of
     * @param oceAgentRecorder         : component used to resolve the physical references of the agents
     */
    public ServiceAgentDecisionOLD(ServiceAgent myServiceAgent, IRecord oceAgentRecorder) {
        this.myServiceAgent = myServiceAgent;
        this.oceAgentRecorder = oceAgentRecorder;
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
     * @param oceAgentRecorder : the recorder component of the medium
     */
    public void setOceAgentRecorder(IRecord oceAgentRecorder) {
        this.oceAgentRecorder = oceAgentRecorder;
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

//        //Check if the service Agent is in suicide mechanism -> we inform the other partner of the disconnection
//        if(this.commitSuicide == true){
//            //Create the disconnection decision
//            //Create the receivers
//            ArrayList<OCEAgent> receivers = new ArrayList<>();
//            receivers.add(this.myServiceAgent.getConnectedTo().get()); //We are sure that the agent is connected cause the boolean attribute is set to true when the agent is in suicide mechanism
//            DisconnectDecision disconnectDecision = new DisconnectDecision(this.myServiceAgent, receivers);
//            //Add to the list of decisions
//            myListOfDecisions.add(disconnectDecision);
//            OCELogger.log(Level.INFO, "The agent=  " + this.myServiceAgent.toString() + "is  making a disconnect decision to =  " + this.myServiceAgent.getConnectedTo().get().toString());
//            //Reset the commit suicide variable to false to define the end of the suicide mechanism
//            this.commitSuicide = false;
//        }
//
//        // If the agent isn't in sleep Mode -> if it's the case the service attached to it disappeared ->  for now the agent doesn't do anything
//        if(this.myServiceAgent.getMyConnexionState()!= ServiceAgentConnexionState.SLEEP) {
//
//            //Check if the service agent didn't receive any messages (empty perception)
//            if (filteredPerception.isEmpty()) {
//                //If the service agent was just created -> it advertises
//                if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.CREATED)) {
//                    //Create an advertisement decision
//                    OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
//                    //Change the state of the service agent to not connected
//                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
//                    myListOfDecisions.add(myDecision);
//                } else {
//                    switch (this.myServiceAgent.getMyConnexionState()){
//                        case NOT_CONNECTED:
//                            this.waitingCycleAgentBeforeAdvertiseCounter++; //Increment the value
//                            OCELogger.log(Level.INFO, "Agent -> Decision -> Value Cycle =  " + this.waitingCycleAgentBeforeAdvertiseCounter);
//                            if (this.waitingCycleAgentBeforeAdvertiseCounter == this.waitingCycleAgentBeforeAdvertiseBound) { // After n cycles we advertise again
//                                OCELogger.log(Level.INFO, "Agent -> Decision -> is Advertising !");
//                                //The agent will advertise
//                                OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
//                                myListOfDecisions.add(myDecision);
//                                //Reinitialize the value of the cycle
//                                this.waitingCycleAgentBeforeAdvertiseCounter = 0;
//                            }else{
//                                //The agent do nothing
//                                myListOfDecisions.add(new DoNothingDecision());
//                            }
//                            break;
//
//                        case WAITING:
//                            this.waitingCycleAgentAfterSelectCounter++; //Increment the value
//                            //If the agent reaches the maximum cycle number to wait after selecting an other agent
//                            if(this.waitingCycleAgentAfterSelectCounter == this.waitingCycleAgentAfterSelectBound){
//                                //We change the agent's state to NOT CONNECTED
//                                this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
//                                //Reinitialise the waiting counter after the select to 0
//                                this.waitingCycleAgentAfterSelectCounter=0;
//                                //Reset the cycleBestAgent
//                                this.cycleBestAgent = null;
//
//                                //The checks if the binder agent was created -> if it's the case we delete it
//                                if (this.myServiceAgent.getMyBinderAgent().isPresent()){
//                                    //Get the binder agent
//                                    BinderAgent binderAgent = this.myServiceAgent.getMyBinderAgent().get();
//                                    OCELogger.log(Level.INFO, this.myServiceAgent + " stops WAITING Deleting its binder agent " + this.myServiceAgent.getMyBinderAgent().get());
//                                    binderAgent.suicide();
//                                    //Delete the reference of the binder agent from the service agent
//                                    this.myServiceAgent.deleteMyBinderAgent();
//                                }
//                            }
//                            myListOfDecisions.add(new DoNothingDecision());
//                            break;
//
//                        default:
//                            myListOfDecisions.add(new DoNothingDecision());
//                    }
//
//                }
//            } else { // The agent received messages
//
//                // Reinitialize the value of the counters to 0
//                this.waitingCycleAgentBeforeAdvertiseCounter = 0;
//                this.waitingCycleAgentAfterSelectCounter = 0;
//                //Transform the Infrastructure messages to OCEMessages
//                List<OCEMessage> OCEPerception = filteredPerception.stream().map(m -> m.toOCEMessage(oceAgentRecorder)).collect(Collectors.toList());
//
//                //Check if we received A disconnect message from the service agent with whom it is connected to (This action is priority =0)
//                List<OCEMessage> OCEPerceptionDisconnect = OCEPerception.stream().filter(m -> m instanceof DisconnectMessage).collect(Collectors.toList());
//                if(!OCEPerceptionDisconnect.isEmpty()) { // The agent received the disconnect message
//                    //We check if the agent who sends the disconnect message is the same as the one that we are connected to
//                    ServiceAgent emitterDisconnect = (ServiceAgent) OCEPerceptionDisconnect.get(0).getEmitter();
//                    if(this.myServiceAgent.getConnectedTo().get().equals(emitterDisconnect)){
//                        //Treat the message
//                        OCEPerceptionDisconnect.get(0).toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
//                    }
//                }else {
//                    //Check if the agent received a temporary connected message from the binder agent
//                    List<OCEMessage> OCEPerceptionTemporaryConnected = OCEPerception.stream().filter(m -> m instanceof WaitingForFeedbackMessage).collect(Collectors.toList());
//                    if(!OCEPerceptionTemporaryConnected.isEmpty()){
//                        //Get the received a temporary connected Message -> index 0 cause there is only one and treat the temporary connected message -> for now it returns "do nothing decision"
//                        WaitingForFeedbackMessage receivedWaitingForFeedbackMessage = (WaitingForFeedbackMessage) OCEPerceptionTemporaryConnected.get(0);
//                        // Treat the received temporary connected Message
//                        OCEDecision myDecision = receivedWaitingForFeedbackMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
//                    }
//
//                    //Check if the agent received the feedback message from the agent binder (This action is priority= 1)
//                    List<OCEMessage> OCEPerceptionFeedback = OCEPerception.stream().filter(m -> m instanceof FeedbackMessage).collect(Collectors.toList());
//                    if (!OCEPerceptionFeedback.isEmpty()) { // The agent received the feedback message
//                        //Get the received feedback Message -> index 0 cause there is only one and treat the feedback message -> for now it returns "do nothing decision"
//                        FeedbackMessage receivedFeedbackMessage = (FeedbackMessage) OCEPerceptionFeedback.get(0);
//                        // Treat the received feedback Message
//                        OCEDecision myDecision = receivedFeedbackMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
//                        //If the agent received the feedback and we already selected the best agent we update the score
//                        if (this.myServiceAgent.isFeedbackReceived() && this.cycleBestAgent != null) {
//                            OCELogger.log(Level.INFO, "Agent : Decision -> Before updating Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
//                            //Compute the value of the reinforcement depending on the value of the feedback
//                            if(this.myServiceAgent.getFeedbackValue().equals(FeedbackValues.MODIFIED)){
//                                //Check if the agent got connected to another one or left unconnected
//                                Optional<OCEAgent> agentChosenByUser =  receivedFeedbackMessage.getAgentChosenUser();
//                                if(agentChosenByUser.isPresent()){
//                                    //This service agent got connected to another service agent
//                                    OCELogger.log(Level.INFO, "Agent : Decision -> Feedback Modify - > Service Agent was connected by user to = " +agentChosenByUser.get().toString());
//                                    System.out.println("Agent : Decision -> Feedback Modify - > Service Agent was connected by user to = " +agentChosenByUser.get().toString());
//                                    //Check if the agent chosen by the user is in the CS
//                                    if(!this.myServiceAgent.getMyScoredCurrentSituation().get().containServiceAgent(agentChosenByUser.get().getMyID())) { //The chosen agent doesn't exist in the current situation
//                                        //Add the chosen agent to the Scored CS (the Message type is not important)
//                                        this.myServiceAgent.getMyScoredCurrentSituation().get().addSituationEntry(agentChosenByUser.get().getMyID(), new ScoredCurrentSituationEntry(agentChosenByUser.get().getMyID(), MessageTypes.AGREE, initialValue));
//                                    }
//                                    double soreAgentChosenByUser = this.myServiceAgent.getMyScoredCurrentSituation().get().getAgentSituationEntries().get(agentChosenByUser.get().getMyID()).getScore();
//                                    double scoreAgentProposedByOCE = this.cycleBestAgent.getValue().getScore();
//                                    reinforcement = Math.abs(scoreAgentProposedByOCE - soreAgentChosenByUser )+ beta ;
//
//                                    //Update the score of the agent chosen by the user in the current scored situation
//                                    SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation().get(), agentChosenByUser.get().getMyID(), learningRate, reinforcement);
//                                    //Set the state of the agent to connected
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
//
//                                }else {
//                                    //The service agent left unconnected -> the agent which was proposed by OCE we reinforce negatively
//                                    OCELogger.log(Level.INFO, "Agent : Decision -> Feedback Modify - > Service Agent was not connected by user");
//                                    System.out.println("Agent : Decision -> Feedback Modify - > Service Agent was not connected by user");
//                                    //Set the state of the agent to Not connected
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
//                                }
//                                //Update the score (negatively) for the agent proposed by OCE
//                                reinforcement = -beta;
//                                //Update the score of the agent initially proposed by OCE in the current scored situation
//                                SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation().get(), this.cycleBestAgent.getKey(), learningRate, reinforcement);
//
//                            }else{
//                                if (this.myServiceAgent.getFeedbackValue().equals(FeedbackValues.ACCEPTED)){
//                                    //Set the value of the reinforcement (positive reinforcement)
//                                    reinforcement = +beta;
//                                    //Set the state of the agent to connected
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
//                                }
//                                else {
//                                    //Set the value of the reinforcement (negative reinforcement)
//                                    reinforcement = -beta;
//                                    //Set the state of the agent to Not connected
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
//                                }
//                                //Update the scores in the current scored situation and normalize the values
//                                SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation().get(), this.cycleBestAgent.getKey(), learningRate, reinforcement);
//                            }
//
//                        }else{
//                            //Check if the service agent was not connected in the previous OCE cycle but got connected by the User
//                            if (this.myServiceAgent.isFeedbackReceived() && this.cycleBestAgent == null) {
//                                //Check if the agent got connected to another one or left unconnected
//                                Optional<OCEAgent> agentChosenByUser =  receivedFeedbackMessage.getAgentChosenUser();
//                                if(agentChosenByUser.isPresent()) { //The agent was connected by the user
//                                    //This service agent got connected to another service agent
//                                    OCELogger.log(Level.INFO, "Agent : Decision -> Feedback - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
//                                    System.out.println("Agent : Decision -> Feedback - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
//
//                                    //Check if the agent constructed it's current situation
//                                    if(!this.myServiceAgent.getMyCurrentSituation().isPresent()){
//                                        OCELogger.log(Level.INFO, "Agent : Decision -> Feedback - > Service Agent DOESN'T Have a Current Situation ! -> Creating and scoring the CS ");
//                                        System.out.println("Agent : Decision -> Feedback - > Service Agent DOESN'T Have a Current Situation ! ");
//
//                                        //Add a new current situation
//                                        this.myServiceAgent.setMyCurrentSituation(new Situation<>());
//                                        Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation().get(), this.myServiceAgent.getMyKnowledgeBase(), similarityThreshold);
//                                        OCELogger.log(Level.INFO, "Agent : Decision : creating new Scored Situation after Feedback -> The list of RS selected with a similarityThreshold '" + similarityThreshold + "' = " + listSimilarRS.toString());
//                                        //Score the current situation
//                                        //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
//                                        Situation<ScoredCurrentSituationEntry> ScoredS = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation().get(), listSimilarRS, initialValue);
//                                        this.myServiceAgent.setMyScoredCurrentSituation(ScoredS);
//                                        OCELogger.log(Level.INFO, "Agent : Decision : creating new Scored Situation after Feedback -> The scored Situation = " + ScoredS.toString());
//                                    }
//                                    //Check if the agent chosen by the user is in the current situation
//                                    if(!this.myServiceAgent.getMyScoredCurrentSituation().get().containServiceAgent(agentChosenByUser.get().getMyID())) { //The chosen agent doesn't exist in the current situation
//                                        //Add the chosen agent to the Scored CS (the Message type is not important)
//                                        this.myServiceAgent.getMyScoredCurrentSituation().get().addSituationEntry(agentChosenByUser.get().getMyID(), new ScoredCurrentSituationEntry(agentChosenByUser.get().getMyID(), MessageTypes.AGREE, initialValue));
//                                    }
//                                    //We reinforce with beta NOT with [max(scoreAgent)-scoreChosenAgent] + beta
//                                    //Get the score of the agent chosen by the user
////                                            double soreAgentChosenByUser = this.myServiceAgent.getMyScoredCurrentSituation().get().getAgentSituationEntries().get(agentChosenByUser.get().getMyID()).getScore();
////                                        //Compute the maximum of scores in the scored current situation
////                                        double maxiScoreAgent = this.myServiceAgent.getMyScoredCurrentSituation().get().getAgentSituationEntries().values().stream().map(e -> ((ScoredCurrentSituationEntry) e).getScore()).mapToDouble(d -> (double) d).max().getAsDouble();
////                                        reinforcement = Math.abs(maxiScoreAgent - soreAgentChosenByUser )+ beta ;
////
//                                    reinforcement = beta;
//                                    //Update the score of the agent chosen by the user in the current scored situation
//                                    SituationUtility.updateScoreCurrentSituation(this.myServiceAgent.getMyScoredCurrentSituation().get(), agentChosenByUser.get().getMyID(), learningRate, reinforcement);
//                                    //Set the state of the agent to connected
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
//                                    //The checks if the binder agent was created before -> if it's the case we delete it
//                                    if (this.myServiceAgent.getMyBinderAgent().isPresent()){
//                                        //Get the binder agent
//                                        BinderAgent binderAgent = this.myServiceAgent.getMyBinderAgent().get();
//                                        OCELogger.log(Level.INFO, this.myServiceAgent + "  After receiving feedback -> deleting it's binder Agent  " + this.myServiceAgent.getMyBinderAgent().get());
//                                        binderAgent.suicide();
//                                        //Delete the reference of the binder agent from the service agent
//                                        this.myServiceAgent.deleteMyBinderAgent();
//                                    }
//                                    //Add the binder agent (the agent who sent the feedbackMessage)
//                                    this.myServiceAgent.setMyBinderAgent((BinderAgent) receivedFeedbackMessage.getEmitter());
//
//                                }else{
//                                    OCELogger.log(Level.WARNING, "Agent : Decision -> Feedback - > the agent received feedback but WAS  NOT CONNECTED BY THE USER !! " );
//                                }
//                            }
//                        }
//
//                        //Normalise the scores of the agents in the scored current situation
//                        SituationUtility.normalizeScoresSCS(this.myServiceAgent.getMyScoredCurrentSituation().get());
//
//                        OCELogger.log(Level.INFO, "Agent : Decision -> Updated Scored Current Situation = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
//                        //Update the agent Knowledge base
//                        this.myServiceAgent.updateMyKnowledgeBase();
//                        OCELogger.log(Level.INFO, "Agent : Decision -> Knowledge Base = " + this.myServiceAgent.getMyKnowledgeBase().toString());
//
//                        //Set to whom the service agent is connected, it may be an other service agent or Empty
//                        Optional<OCEAgent> connectedToAgent = receivedFeedbackMessage.getAgentChosenUser();
//                        //If the service agent was connected by the user (or left connected)
//                        if(connectedToAgent.isPresent()){
//                            //Cast the variable ConnectedTo
//                            ServiceAgent otherServiceAgent = (ServiceAgent) connectedToAgent.get();
//                            //Set the reference of the service agent that the service agent treating this message is connected to
//                            this.myServiceAgent.setConnectedTo(otherServiceAgent);
//                            OCELogger.log(Level.INFO, "Service agent = " +  this.myServiceAgent.toString() + " is connected to = "+ otherServiceAgent.toString());
//                            System.out.println("Service agent = " +  this.myServiceAgent.toString() + " is connected to = "+ otherServiceAgent.toString());
//                        }else{
//                            //The user deleted the connection
//                            this.myServiceAgent.resetConnectedTo();
//                            OCELogger.log(Level.INFO, "Service agent = " +  this.myServiceAgent.toString() + " is NOT connected to any service agent ! ");
//                            System.out.println("Service agent = " +  this.myServiceAgent.toString() + " is NOT connected to any service agent ! ");
//
//                        }
//                        // set the feedback received to false
//                        this.myServiceAgent.setFeedbackReceived(false);
//                        //Reinitialize the cycle number of the agent
//                        this.myServiceAgent.setMyCurrentCycleNumber(0);
//                        //Reinitialise the current situation and the scored current situation of the service agent
//                        this.myServiceAgent.resetMyCurrentSituation();
//                        this.myServiceAgent.resetMyScoredCurrentSituation();
//                        // the agent will do nothing after this
//                        myListOfDecisions.add(myDecision);
//
//                    } else {
//                        //If the service agent is not connected
//                        if (this.myServiceAgent.getMyConnexionState() == ServiceAgentConnexionState.NOT_CONNECTED) {
//                            //Create a mapping of the agent's id and their messages
//                            Map<IDAgent, OCEMessage> OCEPerceptionSortedByID = OCEPerception.stream().collect(Collectors.toMap(OCEMessage::getIDEmitter, s -> s, (x, y) -> y));
//                            //Create the current situation
//                            //Check if it's the start of the agent cycle
//                            if (this.myServiceAgent.getMyCurrentCycleNumber() == 0) {
//                                //Start of the cycle // We initialise a new current situation
//                                this.myServiceAgent.setMyCurrentSituation(new Situation<CurrentSituationEntry>(OCEPerception));
//                            } else {
//                                //We update the current one
//                                Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
//                                this.myServiceAgent.getMyCurrentSituation().get().getAgentSituationEntries().putAll(myCurrentSituation.getAgentSituationEntries());
//                            }
//                            OCELogger.log(Level.INFO, "Agent : Decision -> Current Situation = " + this.myServiceAgent.getMyCurrentSituation().toString());
////                                //Check if the service agent is connected (in the previous engine cycle)
////                                if(this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.CONNECTED) && this.myServiceAgent.getConnectedTo().isPresent()){
////                                    //Add the service to whom we are connected to the current situation
////                                    this.myServiceAgent.getMyCurrentSituation().get().addSituationEntry(this.myServiceAgent.getConnectedTo().get().getMyID(), new CurrentSituationEntry(this.myServiceAgent.getConnectedTo().get().getMyID(), MessageTypes.AGREE));
////                                }
//
//                            //Check for similar Reference Situation
//                            Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation().get(), this.myServiceAgent.getMyKnowledgeBase(), similarityThreshold);
//                            OCELogger.log(Level.INFO, "Agent : Decision -> The list of RS selected with a similarityThreshold '" + similarityThreshold + "' = " + listSimilarRS.toString());
//                            //Score the current situation
//                            //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
//                            Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation().get(), listSimilarRS, initialValue);
//                            this.myServiceAgent.setMyScoredCurrentSituation(myScoredCurrentSituation);
//                            OCELogger.log(Level.INFO, "Agent : Decision -> The scored current situation = " + myScoredCurrentSituation.toString());
//
//                            //Select the best agent to respond to from the scored current situation
//                            IAgentSelectionStrategy agentSelectionStrategy = new BestScoreEpsilonGreedy(epsilon);
//                            this.cycleBestAgent = SituationUtility.selectBestAgent(myScoredCurrentSituation, agentSelectionStrategy);
//                            OCELogger.log(Level.INFO, " Agent : Decision -> Using the maximum score and epsilon greedy strategy (" + epsilon + "), the best agent = " + this.cycleBestAgent);
//                            //Get from the map the corresponding OCEMessage corresponding to the agent that has been selected
//                            OCEMessage bestOCEMessage;
//                            try {
//                                bestOCEMessage = OCEPerceptionSortedByID.get(this.cycleBestAgent.getKey());
//                                OCEDecision myDecision = bestOCEMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
//                                myListOfDecisions.add(myDecision);
//                            } catch (NullPointerException e) {
//                                System.out.println(" Best Agent  = " + this.cycleBestAgent);
//                                System.out.println(" Best Message agent  = " + this.cycleBestAgent);
//                                System.out.println(" ConnexionState  = " + this.myServiceAgent.getMyConnexionState());
//                                System.out.println(" Current Service agent  = " + this.myServiceAgent);
//                                System.out.println(" Le service géré = " + this.myServiceAgent.getHandledService());
//                                e.printStackTrace();
//                            }
//
//                            //Increment the current cycle number of the agent
//                            this.myServiceAgent.incrementMyCurrentCycleNumber();
//                        } else {
//                            //If the service agent is in waiting state
//                            if (this.myServiceAgent.getMyConnexionState() == ServiceAgentConnexionState.WAITING) {
//                                OCELogger.log(Level.INFO, " Agent : Decision -> WAITING : let me check if I received messages from the service agent I selected ");
//                                //Check if we received messages from the service agent that we selected before changing the state
//                                List<OCEMessage> OCEPerceptionSelectedAgentMessages = OCEPerception.stream().filter(m -> m.getEmitter().getMyID().equals(this.myServiceAgent.getMySelectedAgent().getMyID())).collect(Collectors.toList());
//
//                                if (!OCEPerceptionSelectedAgentMessages.isEmpty()) {
//                                    OCELogger.log(Level.INFO, " Agent : Decision -> WAITING : I received messages from my selected agent -> i treat them");
//                                    //Get the messages and treat them ->
//                                    for (int index = 0; index < OCEPerceptionSelectedAgentMessages.size(); index++) {
//                                        OCEDecision myDecision = OCEPerceptionSelectedAgentMessages.get(index).toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
//                                        myListOfDecisions.add(myDecision);
//                                    }
//                                }
//                                //Increment the waiting counter
//                                this.waitingCycleAgentAfterSelectCounter++;
//                                //If the agent reaches the maximum cycle number to wait after selecting an other agent -> we change it's state to NOT CONNECTED
//                                if (this.waitingCycleAgentAfterSelectCounter == this.waitingCycleAgentAfterSelectBound) {
//                                    this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
//                                    //Reinitialise the waiting counter after the select to 0
//                                    this.waitingCycleAgentAfterSelectCounter = 0;
//                                    //Reset the cycleBestAgent
//                                    this.cycleBestAgent = null;
//                                    //The checks if the binder agent was created -> if it's the case we delete it
//                                    if (this.myServiceAgent.getMyBinderAgent().isPresent()){
//                                        //Get the binder agent
//                                        BinderAgent binderAgent = this.myServiceAgent.getMyBinderAgent().get();
//                                        OCELogger.log(Level.INFO, this.myServiceAgent + " stops WAITING Deleting its binder agent " + this.myServiceAgent.getMyBinderAgent().get());
//                                        binderAgent.suicide();
//                                        //Delete the reference of the binder agent from the service agent
//                                        this.myServiceAgent.deleteMyBinderAgent();
//                                    }
//                                }
//                            }
//                            //Else -> the agent is in CONNECTED state -> for now we DO NOTHING
//
//                            //The agent will do nothing
//                            myListOfDecisions.add(new DoNothingDecision());
//                        }
//
//                    }
//                }
//            }
//            // }
//        }else{
//            OCELogger.log(Level.INFO, "Agent : Decision -> is in SLEEP mode !");
//        }
        OCELogger.log(Level.INFO, "Agent : Decision -> List of decisions = " + myListOfDecisions.toString());
        return myListOfDecisions;
    }

    /**
     * Set the indicator that the service agent of this component is launching suicide mechanism
     * @param commitSuicide : true if the agent is committing suicide, false otherwise
     */
    public void setCommitSuicide(boolean commitSuicide) {
        this.commitSuicide = commitSuicide;
    }

    /**
     * Set the value for "epsilon" which is used in the selection of best agent strategy
     * @param epsilon : the new value
     */
    public void setEpsilon(double epsilon) {
        //OCELogger.log(Level.INFO,"Agent = "+ this.myServiceAgent.toString() + " update the EPSILON value to " + epsilon);
        this.epsilon = epsilon;
    }

    /**
     * Set the value for the learning rate parameter (alpha)
     * @param learningRate : the new value
     */
    public void setLearningRate(double learningRate) {
        //OCELogger.log(Level.INFO,"Agent = "+ this.myServiceAgent.toString() + " update the LEARNING RATE value to " + learningRate);
        this.learningRate = learningRate;
    }

    /**
     * Set the value for the parameter "beta" used to compute the reinforcement value
     * @param beta : the new value
     */
    public void setBeta(double beta) {
        //OCELogger.log(Level.INFO,"Agent = "+ this.myServiceAgent.toString() + " update the BETA (Used to compute reinforcement) value to " + beta);
        this.beta = beta;
    }

    /**
     * Set the value for the similarity similarityThreshold
     * @param similarityThreshold : the new value
     */
    public void setSimilarityThreshold(double similarityThreshold) {
        //OCELogger.log(Level.INFO,"Agent = "+ this.myServiceAgent.toString() + " update the SIMILARITY THRESHOLD value to " + similarityThreshold);
        this.similarityThreshold = similarityThreshold;
    }
}
