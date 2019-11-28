/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.OCEAgent;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.FeedbackMessage;
import OCE.OCEMessages.FeedbackValues;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents the feedback message (Infrastructure level) sent to the agent after the bind
 * @author Walid YOUNES
 * @version 1.0
 */

public class FeedbackInfraMessage extends InfraMessage
{
    private FeedbackValues feedbackValue;           // The value of the feedback
    private Optional<OCEAgent> agentChosenUser;     // ths variable represents the agent to whom the receiver of this message is connected to (it migh be the one that he proposed or a new one if the user modify
                                                    // or empty if the user delete the connection


    /**
     * create a feedback message
     * @param emitter           : reference of the agent sending the selection message
     * @param receivers         : the references of the receivers of the selection message
     * @param feedbackValue    : the value of the feedback sent
     */
    public FeedbackInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers,FeedbackValues feedbackValue) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.feedbackValue = feedbackValue;
        this.myType = MessageTypes.FEEDBACK;
        this.agentChosenUser = Optional.empty();
    }

    /**
     * Get the matchingID of the message
     * @return : The matchingID of this message
     */
    @Override
    public MessageTypes getMyType() {
        return this.myType;
    }

    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        if (this.agentChosenUser.isPresent()){
            try {
                return new FeedbackMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), this.feedbackValue, this.agentChosenUser.get());
            } catch (ReferenceResolutionFailure referenceResolutionFailure) {
                referenceResolutionFailure.printStackTrace();
                return null;
            }
        }else{ //In case of the receiver of this message is not connected
            try {
                return new FeedbackMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), this.feedbackValue);
            } catch (ReferenceResolutionFailure referenceResolutionFailure) {
                referenceResolutionFailure.printStackTrace();
                return null;
            }
        }

    }

    /**
     * Get the reference to the service agent that the user chooses to connect to the service agent receiving this message
     * @return the reference of the service agent connected to the receiver of this message or empty if the user deleted the connection
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
        return "FeedbackInfraMessage{" +
                "emitter=" + this.emitter +
                ", receivers=" + this.receivers +
                ", value= " + this.feedbackValue +
                '}';
    }
}
