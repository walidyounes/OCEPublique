/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.*;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class FeedbackMessage extends OCEMessage {

    private ServiceAgent    serviceAgentRef;        //The reference of the service agent treating this message
    private FeedbackValues feedbackValue;           // the feedbackValue given for the feedback, to simplify it has two possible values "ACCEPTED" "REJECTED"
    private Optional<OCEAgent> agentChosenUser;     // ths variable represents the agent to whom the receiver of this message is connected to (it migh be the one that he proposed or a new one if the user modify
                                                    // or empty if the user delete the connection

    /**
     * create a feedback perception
     * @param emitter   : reference of the agent sending the feedback message
     * @param receivers : the references of the receivers of the feedback message
     * @param feedbackValues : the matchingID of the feedback
     */
    public FeedbackMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers, FeedbackValues feedbackValues) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.feedbackValue = feedbackValues;
        this.agentChosenUser = Optional.empty();
    }

    /**
     * create a feedback perception
     * @param emitter   : reference of the agent sending the feedback message
     * @param receivers : the references of the receivers of the feedback message
     * @param feedbackValues : the matchingID of the feedback
     */
    public FeedbackMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers, FeedbackValues feedbackValues, OCEAgent agentChosenUser) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.feedbackValue = feedbackValues;
        this.agentChosenUser = Optional.ofNullable(agentChosenUser);
    }

    /**
     * treat the feedback message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "CREATED, CONNECTED, NOT_CONNECTED, WAITING"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a feedback message");
        //Cast the OCEAgentRef from OCEAgent to ServiceAgent
        this.serviceAgentRef = (ServiceAgent) OCEAgentRef;

        //Get the parameters for the learning
        double initialValue = this.serviceAgentRef.getInitialValue();                           //The value used to initialise the score
        double reinforcement = 0.0;                                                             //The value of the reinforcement
        double beta = this.serviceAgentRef.getBeta();                                           //The value used to compute the reinforcement from the feedback
        double learningRate = this.serviceAgentRef.getLearningRate();                           //The Learning rate
        double similarityThreshold = this.serviceAgentRef.getSimilarityThreshold();             //Define the similarityThreshold for selecting similar reference situation
        // double epsilon = this.serviceAgentRef.getEpsilon();                                     //The value of the threshold used by the strategy of selection of best agent

        //Check if th value of feedback is ADDED
        if(this.feedbackValue.equals(FeedbackValues.ADDED)){ // we don't check for the state of the agent -> we will make it connected
            //Check if it has a binder agent
            if (this.serviceAgentRef.getMyBinderAgent().isPresent()){
                //We delete it -> order it to suicide
                OCELogger.log(Level.INFO, this.serviceAgentRef + "  After receiving ADDED feedback -> deleting it's binder Agent  " + this.serviceAgentRef.getMyBinderAgent().get());
                this.serviceAgentRef.getMyBinderAgent().get().suicide();
            }
            //Add the new binder agent
            this.serviceAgentRef.setMyBinderAgent((BinderAgent)this.emitter);
            //Update Knowledge
            //Get the agent chosen by the user
            Optional<OCEAgent> agentChosenByUser = this.getAgentChosenUser();
            if (agentChosenByUser.isPresent()) {
                //This service agent got connected to another service agent
                OCELogger.log(Level.INFO, "Agent : Decision -> Feedback ADDED - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
                //Check if the agent chosen by the user is in the CS
                if (!this.serviceAgentRef.getMyScoredCurrentSituation().get().containServiceAgent(agentChosenByUser.get().getMyID())) { //The chosen agent doesn't exist in the current situation
                    //Add the chosen agent to the Scored CS (the Message type is not important)
                    double maxValue = SituationUtility.computeMaxScoresSCS(this.serviceAgentRef.getMyScoredCurrentSituation().get()); // If the SCS is empty max value == 1
                    this.serviceAgentRef.getMyScoredCurrentSituation().get().addSituationEntry(agentChosenByUser.get().getMyID(), new ScoredCurrentSituationEntry(agentChosenByUser.get().getMyID(), MessageTypes.AGREE, maxValue));
                }
                reinforcement = +beta;
                //Update the score of the agent chosen by the user in the current scored situation
                SituationUtility.updateScoreCurrentSituation(this.serviceAgentRef.getMyScoredCurrentSituation().get(), agentChosenByUser.get().getMyID(), learningRate, reinforcement);
                //Cast the variable ConnectedTo
                ServiceAgent otherServiceAgent = (ServiceAgent) agentChosenByUser.get();
                //Set the reference of the service agent that the service agent treating this message is connected to
                this.serviceAgentRef.setConnectedTo(otherServiceAgent);
                //Set the state of the agent to connected
                this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
            }

        }else{
            //If the service agent receiving this feedback was waiting
            if (this.serviceAgentRef.getMyConnexionState().equals(ServiceAgentConnexionState.EXPECTING_FEEDBACK) || this.serviceAgentRef.getMyConnexionState().equals(ServiceAgentConnexionState.CONNECTED)) {
                if(this.serviceAgentRef.getOceCycleBestAgent().isPresent()){
                    OCELogger.log(Level.INFO, "Agent : Decision -> Before updating Scored Current Situation = " + this.serviceAgentRef.getMyScoredCurrentSituation().toString());
                    //Compute the value of the reinforcement depending on the value of the feedback
                    if (this.feedbackValue.equals(FeedbackValues.MODIFIED)) {
                        //Check if the agent got connected to another one or left unconnected
                        Optional<OCEAgent> agentChosenByUser = this.getAgentChosenUser();
                        if (agentChosenByUser.isPresent()) {
                            //This service agent got connected to another service agent
                            OCELogger.log(Level.INFO, "Agent : Decision -> Feedback Modify - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
                            System.out.println("Agent : Decision -> Feedback Modify - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
                            //Check if the agent chosen by the user is in the CS
                            if (!this.serviceAgentRef.getMyScoredCurrentSituation().get().containServiceAgent(agentChosenByUser.get().getMyID())) { //The chosen agent doesn't exist in the current situation
                                //Add the chosen agent to the Scored CS (the Message type is not important)
                                double maxValue = SituationUtility.computeMaxScoresSCS(this.serviceAgentRef.getMyScoredCurrentSituation().get()); // If the SCS is empty max value == 1
                                this.serviceAgentRef.getMyScoredCurrentSituation().get().addSituationEntry(agentChosenByUser.get().getMyID(), new ScoredCurrentSituationEntry(agentChosenByUser.get().getMyID(), MessageTypes.AGREE, maxValue));
                            }
                            //Get the score of the agent chosen by the USER
                            double scoreAgentChosenByUser = this.serviceAgentRef.getMyScoredCurrentSituation().get().getAgentSituationEntries().get(agentChosenByUser.get().getMyID()).getScore();
                            //Get the score of the agent chosen by OCE
                            double scoreAgentProposedByOCE = this.serviceAgentRef.getOceCycleBestAgent().get().getValue().getScore();
                            //Compute the reinforcement
                            reinforcement = Math.abs(scoreAgentProposedByOCE - scoreAgentChosenByUser) + beta;
                            //Update the score of the agent chosen by the user in the current scored situation
                            SituationUtility.updateScoreCurrentSituation(this.serviceAgentRef.getMyScoredCurrentSituation().get(), agentChosenByUser.get().getMyID(), learningRate, reinforcement);
                            //Set the state of the agent to connected
                            this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
                            //Update the binder agent
                            //Get the emitter of this message
                            BinderAgent potentialBinderAgent = (BinderAgent) this.getEmitter();
                            if(!this.serviceAgentRef.isUpdateBAFeedbackModified()){
                                boolean result = potentialBinderAgent.addHandledServices((MockupService) this.serviceAgentRef.getHandledService(), (MockupService)((ServiceAgent)agentChosenByUser.get()).getHandledService());
                                if(result){
                                    this.serviceAgentRef.setMyBinderAgent(potentialBinderAgent);
                                    this.serviceAgentRef.setUpdateBAFeedbackModified(true);
                                }
                            }
                        } else {
                            //The service agent left unconnected -> the agent which was proposed by OCE we reinforce negatively
                            OCELogger.log(Level.INFO, "Agent : Decision -> Feedback Modify - > Service Agent was not connected by user");
                            System.out.println("Agent : Decision -> Feedback Modify - > Service Agent was not connected by user");
                            //Set the state of the agent to Not connected
                            this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
                            //Delete the reference of the old binder agent
                            if (this.serviceAgentRef.getMyBinderAgent().isPresent()) {
                                //Delete the reference of the service from the binder agent
                                this.serviceAgentRef.getMyBinderAgent().get().deleteMyService(this.serviceAgentRef.getHandledService());
                                //Delete the reference of the binder agent from the service agent
                                this.serviceAgentRef.deleteMyBinderAgent();
                            }
                        }
                        //Update the score (negatively) for the agent proposed by OCE
                        reinforcement = -beta;
                        //Update the score of the agent initially proposed by OCE in the current scored situation
                        SituationUtility.updateScoreCurrentSituation(this.serviceAgentRef.getMyScoredCurrentSituation().get(), this.serviceAgentRef.getOceCycleBestAgent().get().getKey(), learningRate, reinforcement);
                    } else {
                        if (this.feedbackValue.equals(FeedbackValues.ACCEPTED)) {
                            //Set the value of the reinforcement (positive reinforcement)
                            reinforcement = +beta;
                            //Set the state of the agent to connected
                            this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
                        } else {
                            //It's rejected
                            //Set the value of the reinforcement (negative reinforcement)
                            reinforcement = -beta;
                            //Set the state of the agent to Not connected
                            this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
                            //Delete the service handled by tis agent from the binder agent, if the two service were deleted the binder agent suicide
                            this.serviceAgentRef.getMyBinderAgent().get().deleteMyService(this.serviceAgentRef.getHandledService());
                            //delete the reference of the binder agent
                            this.serviceAgentRef.deleteMyBinderAgent();
                        }
                        //Update the scores in the current scored situation and normalize the values
                        SituationUtility.updateScoreCurrentSituation(this.serviceAgentRef.getMyScoredCurrentSituation().get(), this.serviceAgentRef.getOceCycleBestAgent().get().getKey(), learningRate, reinforcement);
                    }
                }else{
                    OCELogger.log(Level.INFO, "Agent : Decision -> Feedback - > Error : service agent chosen by OCE is Empty = " + this.serviceAgentRef.getOceCycleBestAgent().toString());
                    System.out.println("Agent : Decision -> Feedback - > Error : service agent chosen by OCE is Empty = " + this.serviceAgentRef.getOceCycleBestAgent().toString());
                }

            } else { // The service agent was not expecting feedback -> it must got connected by the USER
                //Check if the agent got connected to another one or left unconnected
                Optional<OCEAgent> agentChosenByUser = this.getAgentChosenUser();
                if (agentChosenByUser.isPresent()) { //The agent was connected by the user
                    //This service agent got connected to another service agent
                    OCELogger.log(Level.INFO, "Agent : Decision -> Feedback - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());
                    System.out.println("Agent : Decision -> Feedback - > Service Agent was connected by user to = " + agentChosenByUser.get().toString());

                    //Check if the agent constructed it's current situation
                    if (!this.serviceAgentRef.getMyCurrentSituation().isPresent()) {
                        OCELogger.log(Level.INFO, "Agent : Decision -> Feedback - > Service Agent DOESN'T Have a Current Situation ! -> Creating and scoring the CS ");
                        System.out.println("Agent : Decision -> Feedback - > Service Agent DOESN'T Have a Current Situation ! ");

                        //Add a new current situation
                        this.serviceAgentRef.setMyCurrentSituation(new Situation<>());
                        Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.serviceAgentRef.getMyCurrentSituation().get(), this.serviceAgentRef.getMyKnowledgeBase(), similarityThreshold);
                        OCELogger.log(Level.INFO, "Agent : Decision : creating new Scored Situation after Feedback -> The list of RS selected with a similarityThreshold '" + similarityThreshold + "' = " + listSimilarRS.toString());
                        //Score the current situation
                        //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
                        Situation<ScoredCurrentSituationEntry> ScoredS = SituationUtility.scoreCurrentSituation(this.serviceAgentRef.getMyCurrentSituation().get(), listSimilarRS, initialValue);
                        this.serviceAgentRef.setMyScoredCurrentSituation(ScoredS);
                        OCELogger.log(Level.INFO, "Agent : Decision : creating new Scored Situation after Feedback -> The scored Situation = " + ScoredS.toString());
                    }
                    //Check if the agent chosen by the user is in the current situation
                    if (!this.serviceAgentRef.getMyScoredCurrentSituation().get().containServiceAgent(agentChosenByUser.get().getMyID())) { //The chosen agent doesn't exist in the current situation
                        //Add the chosen agent to the Scored CS (the Message type is not important)
                        double maxValue = SituationUtility.computeMaxScoresSCS(this.serviceAgentRef.getMyScoredCurrentSituation().get()); // If the SCS is empty max value == 1
                        this.serviceAgentRef.getMyScoredCurrentSituation().get().addSituationEntry(agentChosenByUser.get().getMyID(), new ScoredCurrentSituationEntry(agentChosenByUser.get().getMyID(), MessageTypes.AGREE, maxValue));
                    }

                    reinforcement = beta;
                    //Update the score of the agent chosen by the user in the current scored situation
                    SituationUtility.updateScoreCurrentSituation(this.serviceAgentRef.getMyScoredCurrentSituation().get(), agentChosenByUser.get().getMyID(), learningRate, reinforcement);
                    //Set the state of the agent to connected
                    this.serviceAgentRef.setMyConnexionState(ServiceAgentConnexionState.CONNECTED);
                    //The checks if the binder agent was created before -> if it's the case we delete it
                    if (this.serviceAgentRef.getMyBinderAgent().isPresent()) {
                        //Get the binder agent
                        BinderAgent binderAgent = this.serviceAgentRef.getMyBinderAgent().get();
                        OCELogger.log(Level.INFO, this.serviceAgentRef + "  After receiving feedback -> deleting it's binder Agent  " + this.serviceAgentRef.getMyBinderAgent().get());
                        binderAgent.suicide();
                        //Delete the reference of the binder agent from the service agent
                        this.serviceAgentRef.deleteMyBinderAgent();
                    }
                    //Add the binder agent (the agent who sent the feedbackMessage)
                    this.serviceAgentRef.setMyBinderAgent((BinderAgent) this.getEmitter());

                } else {
                    OCELogger.log(Level.WARNING, "Agent : Decision -> Feedback - > the agent received feedback but WAS  NOT CONNECTED BY THE USER !! ");
                }
            }
            OCELogger.log(Level.INFO, "Agent : Decision -> Updated Scored Current Situation = " + this.serviceAgentRef.getMyScoredCurrentSituation().toString());

//        //Normalise the scores of the agents in the scored current situation
//        SituationUtility.normalizeScoresSCS(this.serviceAgentRef.getMyScoredCurrentSituation().get());
//        OCELogger.log(Level.INFO, "Agent : Decision -> Updated and Normalized SCS = " + this.serviceAgentRef.getMyScoredCurrentSituation().toString());
//
//        //Update the agent Knowledge base
//        this.serviceAgentRef.updateMyKnowledgeBase();
//        OCELogger.log(Level.INFO, "Agent : Decision -> Knowledge Base = " + this.serviceAgentRef.getMyKnowledgeBase().toString());

            //Set to whom the service agent is connected, it may be an other service agent or Empty
            Optional<OCEAgent> connectedToAgent = this.getAgentChosenUser();
            //If the service agent was connected by the user (or left connected)
            if (connectedToAgent.isPresent()) {
                //Cast the variable ConnectedTo
                ServiceAgent otherServiceAgent = (ServiceAgent) connectedToAgent.get();
                //Set the reference of the service agent that the service agent treating this message is connected to
                this.serviceAgentRef.setConnectedTo(otherServiceAgent);
                OCELogger.log(Level.INFO, "Service agent = " + this.serviceAgentRef.toString() + " is connected to = " + otherServiceAgent.toString());
                System.out.println("Service agent = " + this.serviceAgentRef.toString() + " is connected to = " + otherServiceAgent.toString());
            } else {
                //The user deleted the connection
                this.serviceAgentRef.resetConnectedTo();
                OCELogger.log(Level.INFO, "Service agent = " + this.serviceAgentRef.toString() + " is NOT connected to any service agent ! ");
                System.out.println("Service agent = " + this.serviceAgentRef.toString() + " is NOT connected to any service agent ! ");
            }
        }

//        //Set the feedback received to false
//        this.serviceAgentRef.setFeedbackReceived(false);

//        //Reinitialize the cycle number of the agent
//        this.serviceAgentRef.setMyCurrentCycleNumber(0);
//        //Set the variable indicating that the agent will be starting a new agent cycle
//        // this.serviceAgentRef.setStartingNewEngineCycle(true);
//        //Reinitialise the current situation and the scored current situation of the service agent
//        this.serviceAgentRef.resetMyCurrentSituation();
//        this.serviceAgentRef.resetMyScoredCurrentSituation();
//        // the agent will do nothing after this

        return new DoNothingDecision();
    }

    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((BinderAgent) this.emitter).getMyID(), MessageTypes.FEEDBACK);
    }

//    /**
//     * Get the reference of the agent the agent "askingOCEAgent" is connected to (this message is send to both agent which are connected)
//     * @param askingOCEAgent    : the reference of the agent which is searching it's is connected to
//     * @return the reference of the agent connected to the agent "askingOCEAgent". It returns Empty response if the asking agent isn't one of the receivers of this message
//     */
//    private Optional<OCEAgent> getConnectedTo(OCEAgent askingOCEAgent){
//        Optional<OCEAgent> connectedTo = Optional.empty();
//        // Check if the asking agent is in the receivers of this message (We have only two receivers since cardinality == 1)
//        if(this.receivers.size() == 2) {
//            OCELogger.log(Level.INFO, " found two receivers ! ");
//            if(this.receivers.get(0).equals(askingOCEAgent)){
//                connectedTo = Optional.of(this.receivers.get(1));
//            }else{
//                if(this.receivers.get(1).equals(askingOCEAgent)){
//                    connectedTo = Optional.of(this.receivers.get(0));
//                }
//            }
//        }
//        return connectedTo;
//    }

    /**
     * Get the reference to the service agent that the user chooses to connect to the service agent receiving this message
     * @return the reference of the service agent connected to the receiver of this message or empty if the user deleted the connection and the agent is no longer connected
     */
    public Optional<OCEAgent> getAgentChosenUser() {
        return agentChosenUser;
    }

    /**
     * Set the reference to the service agent that the user chooses to connect to the service agent receiving this message
     * @param agentChosenUser   : the reference of the service agent that the user chooses to connect to the service agent receiving this message
     */
    public void setAgentChosenUser(OCEAgent agentChosenUser) {
        this.agentChosenUser = Optional.ofNullable(agentChosenUser);
    }

    /**
     * Delete the value of the variable of the reference to the service agent that the user chooses to connect to the service agent receiving this message
     */
    public void deleteAgentChosenUser(){
        this.agentChosenUser = Optional.empty();
    }

    @Override
    public String toString() {
        return "FeedbackMessage {" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                ", userChosenAgent= " + this.agentChosenUser + '}';
    }
}
