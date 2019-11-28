/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

public class FeedbackMessage extends OCEMessage {

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
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a feedback message");
        //Cast the OCEAgentRef from OCEAgent to ServiceAgent
        ServiceAgent serviceAgentRef = (ServiceAgent) OCEAgentRef;
        //Set that the feedback is received
        serviceAgentRef.setFeedbackReceived(true);
        //Set the value of the received feedback
        serviceAgentRef.setFeedbackValue(this.feedbackValue);
//        //Get the reference of the other agent connected to the agent treating this message
//        Optional<OCEAgent> connectedToAgent = this.getAgentChosenUser();
//        //If the value returned by the function is not empty
//        if(connectedToAgent.isPresent()){
//            //Cast the variable ConnectedTo
//            ServiceAgent otherServiceAgent = (ServiceAgent) connectedToAgent.get();
//            //Set the reference of the service agent that the service agent treating this message is connected to
//            serviceAgentRef.setConnectedTo(Optional.ofNullable(otherServiceAgent));
//            OCELogger.log(Level.INFO, "Service agent = " + serviceAgentRef.toString() + " is connected to = "+ otherServiceAgent.toString());
//        }
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
