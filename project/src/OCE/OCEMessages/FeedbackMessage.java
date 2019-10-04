/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Decisions.DoNothingDecision;
import OCE.Decisions.OCEDecision;

import java.util.ArrayList;
import java.util.logging.Level;

public class FeedbackMessage extends OCEMessage {

    private FeedbackValues feedbackValue; // the feedbackValue given for the feedback, to simplify it has two possible values "VALIDATED" "REJECTED"

    /**
     * create a feedback perception
     * @param emitter   : reference of the agent sending the feedback message
     * @param receivers : the references of the receivers of the feedback message
     * @param feedbackValues : the type of the feedback
     */
    public FeedbackMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers, FeedbackValues feedbackValues) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.feedbackValue = feedbackValues;
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
        MyLogger.log(Level.INFO, OCEAgentRef + " treats a feedback message");
        //Set that the feedback is received
        ((ServiceAgent)OCEAgentRef).setFeedbackReceived(true);
        //Set the value of the received feedback
        ((ServiceAgent)OCEAgentRef).setFeedbackValue(this.feedbackValue);
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

    @Override
    public String toString() {
        return "FeedbackMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
