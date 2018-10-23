/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.BindPerception;

import java.util.ArrayList;

public class BindMessage extends Message {

    /**
     * create a bind message
     * @param emitter    reference of the agent sending the selection message
     * @param receivers the references of the receivers of the selection message
     */
    public BindMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }



    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new BindPerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }
}
