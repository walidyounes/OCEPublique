/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.OCELogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.BestScoreHighestPriority;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.Agents.ServiceAgentPack.Learning.*;
import OCE.InfrastructureMessages.BindInfraMessage;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
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

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentDecision implements IDecisionState {

    private ICommunicationAdapter communicationToolOCE; // The component used to send messages between agents in the engine OCE
    private ServiceAgent myServiceAgent;                // The reference of the service Agent that this component is part of
    private IRecord oceAgentRecorder;                   // Component used to resolve the infrastructure references for service agents, delete agents 'binder agent'
    private boolean commitSuicide;                      // Boolean variable that's indicate for the agent that it's service disappeared and it is connected so it inform the other service Agent and launch disconnection


    //ARSA parameters
    private int waitingCycleAgentBeforeAdvertiseCounter = 0;                //The variable used to count the number of cycle to wait by the agent before advertising again
    private final int waitingCycleAgentBeforeAdvertiseBound = 6;        //The number of cycle to wait by the agent before advertising another time
    private int waitingCycleAgentAfterSelectCounter = 0;                    //The variable used to count the number of cycle that the agent wait after selecting an other service agent
    private final int waitingCycleAgentAfterSelectBound = 6;               //The number of cycle to wait by the agent for the answer from the selected service agent and the binding agent

    // Learning parameters
    private final double initialValue = 0.0;        //The value used to initialise the score
    //private double similarityThreshold = 0.3;       //Define the similarityThreshold for selecting similar reference situation
    private double epsilon = 0.2;                   //The value of the threshold used by the strategy of selection of best agent

    private Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> oceCycleBestAgent; // 2-Uplet that indicates the best agent selected in the previous cycle agent

    private Map<IDAgent, OCEMessage> oceServiceAgentPerceptionHistory; //Store the history of messages received by this agent during the engine cycle

    /**
     * Create the decision component of a service agent
     *
     * @param myServiceAgent       : the reference of the service agent that this component is part of
     * @param oceAgentRecorder     : component used to resolve the physical references of the agents
     * @param communicationToolOCE : component used to send messages between the agents of the engine
     */
    public ServiceAgentDecision(ServiceAgent myServiceAgent, IRecord oceAgentRecorder, ICommunicationAdapter communicationToolOCE) {
        this.myServiceAgent = myServiceAgent;
        this.oceAgentRecorder = oceAgentRecorder;
        this.communicationToolOCE = communicationToolOCE;
        this.oceCycleBestAgent = Optional.empty();
        this.oceServiceAgentPerceptionHistory = new TreeMap<>();
    }


    /**
     * Get the service Agent that this component is part of
     *
     * @return the reference of the service agent which own this decision module
     */
    public ServiceAgent getMyServiceAgent() {
        return myServiceAgent;
    }

    /**
     * Update the service Agent which this component is part of
     *
     * @param myServiceAgent :  the service agent
     */
    public void setMyServiceAgent(ServiceAgent myServiceAgent) {

        this.myServiceAgent = myServiceAgent;
    }

    /**
     * Update the component which is in charge of resolving the reference
     *
     * @param oceAgentRecorder : the recorder component of the medium
     */
    public void setOceAgentRecorder(IRecord oceAgentRecorder) {
        this.oceAgentRecorder = oceAgentRecorder;
    }

    /**
     * Implement the decision mechanism of the binder agent, and produce a list of decisions
     */
    @Override
    public ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perceptions) {
        // Filter the advertisement (if they exist) and keep only those who matches
        IMatching matching = new Matching();
        Criteria matchingAdvertiseCriteria = new MatchingAdvertiseCriteria(myServiceAgent.getHandledService(), matching);
        ArrayList<InfraMessage> filteredPerception = matchingAdvertiseCriteria.meetCriteria(perceptions);

        // OCELogger.log(Level.INFO, "Agent -> Decision -> filtered Messages after matching =  " + filteredPerception.toString());

        //Create a list of decisions
        ArrayList<OCEDecision> myListOfDecisions = new ArrayList<>();

        //Check if the service Agent is in suicide mechanism -> we inform the other partner of the disconnection
        if (this.commitSuicide) { //Create the disconnection decision
            this.suicideTreatment(myListOfDecisions);
        }
        // If the agent isn't in sleep Mode -> if it's the case the service attached to it disappeared ->  for now the agent doesn't do anything
        if (!this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.SLEEP)) {

            //Transform the Infrastructure messages to OCEMessages
            List<OCEMessage> OCEPerception = filteredPerception.stream().map(m -> m.toOCEMessage(oceAgentRecorder)).collect(Collectors.toList());

            if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.CREATED)) {
                this.createdStateTreatment(myListOfDecisions);
            } else {
                //Check if the agent didn't receive any messages
                if (OCEPerception.isEmpty() && this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.NOT_CONNECTED)) {
                    this.waitingCycleAgentBeforeAdvertiseCounter++; //Increment the value
                    OCELogger.log(Level.INFO, "Agent -> Decision -> Value Cycle =  " + this.waitingCycleAgentBeforeAdvertiseCounter);
                    if (this.waitingCycleAgentBeforeAdvertiseCounter == this.waitingCycleAgentBeforeAdvertiseBound) { // After n cycles we advertise again
                        OCELogger.log(Level.INFO, "Agent -> Decision -> is Advertising !");
                        //The agent will advertise
                        OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                        myListOfDecisions.add(myDecision);
                        //Reinitialize the value of the cycle agent counter before advertising
                        this.waitingCycleAgentBeforeAdvertiseCounter = 0;
                    } else {
                        //The agent do nothing
                        myListOfDecisions.add(new DoNothingDecision());
                    }
                } else {
                    // Reinitialize the value of the counters to 0
                    this.waitingCycleAgentBeforeAdvertiseCounter = 0;
                    //Todo 15/01 see if this is where we need to put back the counter to wait before select to 0 -> i think it should go elsewhere - 15/01 it works without it
                    //this.waitingCycleAgentAfterSelectCounter = 0;

                    // Check if we received a disconnect message from the service agent with whom it is connected to
                    List<OCEMessage> OCEPerceptionDisconnect = OCEPerception.stream().filter(m -> m instanceof DisconnectMessage).collect(Collectors.toList());
                    if (!OCEPerceptionDisconnect.isEmpty()) { // The agent received the disconnect message
                        //We check if the agent who sends the disconnect message is the same as the one that we are connected to
                        ServiceAgent emitterDisconnect = (ServiceAgent) OCEPerceptionDisconnect.get(0).getEmitter();
                        if (this.myServiceAgent.getConnectedTo().get().equals(emitterDisconnect)) {
                            //Treat the message
                            OCEPerceptionDisconnect.get(0).toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
                            //Delete from the current situation of this service agent , all entries belonging the agent sending the disconnect message
                            if (this.myServiceAgent.getMyCurrentSituation().isPresent()) {
                                this.myServiceAgent.getMyCurrentSituation().get().deleteSituationEntryByIDAgent(emitterDisconnect.getMyID());
                            }
                            //Delete from the history of messages, the ones that were send by the emitter of the disconnect message
                            oceServiceAgentPerceptionHistory.remove(emitterDisconnect.getMyID());
                        }
                    } else {
                        //Check if the agent received a temporary connected message from the binder agent
                        List<OCEMessage> OCEPerceptionTemporaryConnected = OCEPerception.stream().filter(m -> m instanceof WaitingForFeedbackMessage).collect(Collectors.toList());
                        if (!OCEPerceptionTemporaryConnected.isEmpty()) {
                            //Get the received a temporary connected Message -> index 0 cause there is only one and treat the temporary connected message -> for now it returns "do nothing decision"
                            WaitingForFeedbackMessage receivedWaitingForFeedbackMessage = (WaitingForFeedbackMessage) OCEPerceptionTemporaryConnected.get(0);
                            // Treat the received temporary connected Message
                            OCEDecision myDecision = receivedWaitingForFeedbackMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
                            // Reinitialize the value of the counters to 0 cause we are not waiting for the agent response anymore
                            this.waitingCycleAgentAfterSelectCounter = 0;
                        }else{
                            //Check if the agent received the feedback message from the agent binder
                            List<OCEMessage> OCEPerceptionFeedback = OCEPerception.stream().filter(m -> m instanceof FeedbackMessage).collect(Collectors.toList());
                            if (!OCEPerceptionFeedback.isEmpty()) { // The agent received the feedback message
                                feedbackTreatment(myListOfDecisions, OCEPerceptionFeedback);
                            } else {
                                //If the service agent is in waiting state
                                if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.WAITING)) {
                                    this.waitingStateTreatment(myListOfDecisions, OCEPerception);
                                } else {
                                    if (this.myServiceAgent.isStartingNewEngineCycle()) {
                                        //Clear the content of the history of messages received in the last cycle
                                        oceServiceAgentPerceptionHistory.clear();
                                    }
                                    //Create a mapping of the agent's id and their messages
                                    oceServiceAgentPerceptionHistory.putAll(OCEPerception.stream().collect(Collectors.toMap(OCEMessage::getIDEmitter, s -> s, (x, y) -> y)));
                                    //Create the current situation
                                    //Check if it's the start of the agent cycle
                                    if (!this.myServiceAgent.getMyCurrentSituation().isPresent()) {
                                        //Start of the cycle // We initialise a new current situation
                                        this.myServiceAgent.setMyCurrentSituation(new Situation<CurrentSituationEntry>(OCEPerception));
                                    } else {
                                        //We update the current one
                                        Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                                        this.myServiceAgent.getMyCurrentSituation().get().getAgentSituationEntries().putAll(myCurrentSituation.getAgentSituationEntries());
                                    }
                                    //Check if the service agent is connected (in the previous engine cycle)
                                    if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.CONNECTED) && this.myServiceAgent.getConnectedTo().isPresent()) {
                                        //Add the service to whom we are connected to the current situation
                                        this.myServiceAgent.getMyCurrentSituation().get().addSituationEntry(this.myServiceAgent.getConnectedTo().get().getMyID(), new CurrentSituationEntry(this.myServiceAgent.getConnectedTo().get().getMyID(), MessageTypes.AGREE));
                                    }
                                    OCELogger.log(Level.INFO, "Agent : Decision -> Current Situation = " + this.myServiceAgent.getMyCurrentSituation().toString());
                                    //Check for similar Reference Situation
                                    Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation().get(), this.myServiceAgent.getMyKnowledgeBase(), this.myServiceAgent.getSimilarityThreshold());
                                    OCELogger.log(Level.INFO, "Agent : Decision -> The list of RS selected with a similarityThreshold '" + this.myServiceAgent.getSimilarityThreshold() + "' = " + listSimilarRS.toString());
                                    //Score the current situation
                                    //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue such as Sum(Scores)==1
                                    Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation().get(), listSimilarRS, initialValue);
                                    this.myServiceAgent.setMyScoredCurrentSituation(myScoredCurrentSituation);
                                    OCELogger.log(Level.INFO, "Agent : Decision -> The scored current situation = " + myScoredCurrentSituation.toString());

                                    OCEDecision myDecision;
                                    if (!this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.EXPECTING_FEEDBACK)) {
                                        //Check if the service agent is connected (in the previous engine cycle) we don't select a best agent, the best agent is the one with whom we are connected
                                        if (this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.CONNECTED) && this.myServiceAgent.getConnectedTo().isPresent()) {
                                            //Mark the agent with whom we are connected to as the best agent
                                            this.oceCycleBestAgent = Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(this.myServiceAgent.getConnectedTo().get().getMyID(),
                                                    this.myServiceAgent.getMyScoredCurrentSituation().get().getSituationEntryByIDAgent(this.myServiceAgent.getConnectedTo().get().getMyID())));

                                            //if it's the beginning of the engine cycle
                                            if (this.myServiceAgent.isStartingNewEngineCycle()) {
                                                //Update the field in the service agent
                                                this.myServiceAgent.setOceCycleBestAgent(this.oceCycleBestAgent.get());
                                                //Send a bind message to the binder agent
                                                BindInfraMessage bindMessage = new BindInfraMessage(null, null);
                                                ArrayList<OCEAgent> bindReceiver = new ArrayList<>();
                                                bindReceiver.add(this.myServiceAgent.getMyBinderAgent().get());
                                                communicationToolOCE.sendMessage(bindMessage, this.myServiceAgent, bindReceiver);
                                            }
                                            myDecision = new DoNothingDecision();
                                        } else {
                                            //Select the best agent to respond to from the scored current situation
                                            // IAgentSelectionStrategy agentSelectionStrategy = new BestScoreEpsilonGreedy(epsilon);
                                            IAgentSelectionStrategy agentSelectionStrategy = new BestScoreHighestPriority(this.myServiceAgent.getEpsilon());
                                            //IAgentSelectionStrategy agentSelectionStrategy = new HighestPriorityBestScore(epsilon);
                                            this.oceCycleBestAgent = SituationUtility.selectBestAgent(myScoredCurrentSituation, agentSelectionStrategy);
                                            OCELogger.log(Level.INFO, " Agent : Decision -> Using the maximum score and epsilon greedy strategy (" + this.myServiceAgent.getEpsilon() + "), the best agent = " + this.oceCycleBestAgent);

                                            if (this.oceCycleBestAgent.isPresent()) {
                                                //Update the field in the service agent
                                                this.myServiceAgent.setOceCycleBestAgent(this.oceCycleBestAgent.get());
                                                //Get from the map the corresponding OCEMessage corresponding to the agent that has been selected
                                                OCEMessage bestOCEMessage;
                                                bestOCEMessage = oceServiceAgentPerceptionHistory.get(this.oceCycleBestAgent.get().getKey());
                                                myDecision = bestOCEMessage.toSelfTreat(
                                                        this.myServiceAgent.getMyConnexionState(),
                                                        this.myServiceAgent,
                                                        this.myServiceAgent.getHandledService()
                                                );
                                            } else {
                                                myDecision = new DoNothingDecision();
                                            }

                                        }
                                    }else {
                                        myDecision = new DoNothingDecision();
                                    }
                                    myListOfDecisions.add(myDecision);

                                    //Mark the end of the engine cycle (so that we don't redo the special case again)
                                    this.myServiceAgent.deactivateStartingNewEngineCycle();

                                    //Increment the current cycle number of the agent
                                    this.myServiceAgent.incrementMyCurrentCycleNumber();
                                }
                            }
                        }

                    }
                }
            }
        } else {
            OCELogger.log(Level.INFO, "Agent : Decision -> is in SLEEP mode !");
        }

        OCELogger.log(Level.INFO, "Agent : Decision -> List of decisions = " + myListOfDecisions.toString());

        return myListOfDecisions;
    }



    /**
     * Internal function called to execute the suicide order
     *
     * @param myListOfDecisions : the list of decisions of the service agent in the current agent cycle
     */
    private void suicideTreatment(List<OCEDecision> myListOfDecisions) {
        //Create the receivers
        ArrayList<OCEAgent> receivers = new ArrayList<>();
        receivers.add(this.myServiceAgent.getConnectedTo().get()); //We are sure that the agent is connected cause the boolean attribute is set to true when the agent is in suicide mechanism
        DisconnectDecision disconnectDecision = new DisconnectDecision(this.myServiceAgent, receivers);
        //Add to the list of decisions
        myListOfDecisions.add(disconnectDecision);
        OCELogger.log(Level.INFO, "The agent=  " + this.myServiceAgent.toString() + "is  making a disconnect decision to =  " + this.myServiceAgent.getConnectedTo().get().toString());
        //Reset the commit suicide variable to false to define the end of the suicide mechanism
        this.commitSuicide = false;
    }

    /**
     * Internal function called to make the suitable decision when the agent is just got created (by default advertisement)
     *
     * @param myListOfDecisions : the list of decisions of the service agent in the current agent cycle
     */
    private void createdStateTreatment(List<OCEDecision> myListOfDecisions) {
        //Create an advertisement decision
        OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
        //Change the state of the service agent to not connected
        this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
        myListOfDecisions.add(myDecision);
    }

    /**
     * Internal function called to make the suitable decision when the agent is in waiting
     *
     * @param myListOfDecisions :    the list of decisions of the service agent in the current agent cycle
     * @param OCEPerception     :    the set of messages perceived by the agent in the current agent cycle
     */
    private void waitingStateTreatment(List<OCEDecision> myListOfDecisions, List<OCEMessage> OCEPerception) {
        OCELogger.log(Level.INFO, " Agent : Decision -> WAITING : let me check if I received messages from the service agent I selected ");
        //Check if we received messages from the service agent that we selected before changing the state
        List<OCEMessage> OCEPerceptionSelectedAgentMessages = OCEPerception.stream().filter(m -> m.getEmitter().getMyID().equals(this.myServiceAgent.getMySelectedAgent().getMyID())).collect(Collectors.toList());

        if (!OCEPerceptionSelectedAgentMessages.isEmpty()) {
            OCELogger.log(Level.INFO, " Agent : Decision -> WAITING : I received messages from my selected agent -> i treat them");
            //Get the messages and treat them ->
            for (int index = 0; index < OCEPerceptionSelectedAgentMessages.size(); index++) {
                OCEDecision myDecision = OCEPerceptionSelectedAgentMessages.get(index).toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
                myListOfDecisions.add(myDecision);
            }
        }
        //Increment the waiting counter
        this.waitingCycleAgentAfterSelectCounter++;
        //If the agent reaches the maximum cycle number to wait after selecting an other agent -> we change it's state to NOT CONNECTED
        if (this.waitingCycleAgentAfterSelectCounter == this.waitingCycleAgentAfterSelectBound) {
            this.myServiceAgent.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
            //Reinitialise the waiting counter after the select to 0
            this.waitingCycleAgentAfterSelectCounter = 0;
            //Reset the cycleBestAgent
            this.oceCycleBestAgent = Optional.empty();
            //The checks if the binder agent was created -> if it's the case we delete it
            if (this.myServiceAgent.getMyBinderAgent().isPresent()) {
                //Get the binder agent
                BinderAgent binderAgent = this.myServiceAgent.getMyBinderAgent().get();
                OCELogger.log(Level.INFO, this.myServiceAgent + " stops WAITING Deleting its binder agent " + this.myServiceAgent.getMyBinderAgent().get());
                binderAgent.suicide();
                //Delete the reference of the binder agent from the service agent
                this.myServiceAgent.deleteMyBinderAgent();
            }
        }
        //The agent will do nothing
        myListOfDecisions.add(new DoNothingDecision());
    }

    /**
     * Internal function called to make the suitable decision and treat the feedback received from the user
     *
     * @param myListOfDecisions     :   the list of decisions of the service agent in the current agent cycle
     * @param OCEPerceptionFeedback :   the set of Feedback messages perceived by the agent in the current agent cycle
     */
    private void feedbackTreatment(List<OCEDecision> myListOfDecisions, List<OCEMessage> OCEPerceptionFeedback) {
        //Set to false the value of the boolean variable indicating that we updated the BA if we received modified feedback
        this.myServiceAgent.setUpdateBAFeedbackModified(false);

        //Iterate over the feedback received to treat them
        for(int i=0; i< OCEPerceptionFeedback.size(); i++){
            //Get the received feedback Message -> index 0 cause there is only one and treat the feedback message -> for now it returns "do nothing decision"
            FeedbackMessage receivedFeedbackMessage = (FeedbackMessage) OCEPerceptionFeedback.get(i);
            // Treat the received feedback Message
            OCEDecision myDecision = receivedFeedbackMessage.toSelfTreat(this.myServiceAgent.getMyConnexionState(), this.myServiceAgent, this.myServiceAgent.getHandledService());
            // the agent is deciding to do nothing (it can be changed later)
            myListOfDecisions.add(myDecision);
        }
        //Once we treat all the feedback received, we update the knowledge of the agent

        //Normalise the scores of the agents in the scored current situation
        if(this.myServiceAgent.getMyScoredCurrentSituation().isPresent()){
            SituationUtility.normalizeScoresSCS(this.myServiceAgent.getMyScoredCurrentSituation().get());
            OCELogger.log(Level.INFO, "Agent : Decision -> Updated and Normalized SCS = " + this.myServiceAgent.getMyScoredCurrentSituation().toString());
        }
        //Update the agent Knowledge base
        this.myServiceAgent.updateMyKnowledgeBase();
        OCELogger.log(Level.INFO, "Agent : Decision -> Knowledge Base = " + this.myServiceAgent.getMyKnowledgeBase().toString());

        //Reinitialize the cycle number of the agent
        this.myServiceAgent.setMyCurrentCycleNumber(0);
        //Set the variable indicating that the agent will be starting a new agent cycle
        // this.serviceAgentRef.setStartingNewEngineCycle(true);
        //Reinitialise the current situation and the scored current situation of the service agent
        this.myServiceAgent.resetMyCurrentSituation();
        this.myServiceAgent.resetMyScoredCurrentSituation();
        //Set to false the value of the boolean variable indicating that we updated the BA if we received modified feedback
        this.myServiceAgent.setUpdateBAFeedbackModified(false);
    }

    /**
     * Set the reference of the component used to send messages between the agents in OCE
     *
     * @param communicationToolOCE : the reference of the communication manager in OCE
     */
    public void setCommunicationToolOCE(ICommunicationAdapter communicationToolOCE) {
        this.communicationToolOCE = communicationToolOCE;
    }

    /**
     * Set the indicator that the service agent of this component is launching suicide mechanism
     *
     * @param commitSuicide : true if the agent is committing suicide, false otherwise
     */
    public void setCommitSuicide(boolean commitSuicide) {
        this.commitSuicide = commitSuicide;
    }
}
