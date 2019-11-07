/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.FeedbackMessage;
import OCE.OCEMessages.FeedbackValues;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;

import java.util.ArrayList;

/**
 * This class represents the feedback message (Infrastructure level) sent to the agent after the bind
 * @author Walid YOUNES
 * @version 1.0
 */

public class FeedbackInfraMessage extends InfraMessage
{
    private FeedbackValues feedbackValue; // The value of the feedback

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
        try {
            return new FeedbackMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), this.feedbackValue);
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
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
