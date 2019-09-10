/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.BindMessage;

import java.util.ArrayList;

public class BindInfraMessage extends InfraMessage {

    /**
     * create a bind message
     * @param emitter    reference of the agent sending the bind message
     * @param receivers the references of the receivers of the bind message
     */
    public BindInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.myType = MessageTypes.BIND;
    }



    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        try {
            return new BindMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    /**
     * Get the type of the message
     * @return : The type of this message
     */
    @Override
    public MessageTypes getMyType() {
        return this.myType;
    }

    @Override
    public String toString() {
        return "BindInfraMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
