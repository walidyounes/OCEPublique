/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.DisconnectMessage;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;

import java.util.ArrayList;

public class DisconnectInfraMessage extends  InfraMessage {

    /**
     * Create a Disconnect message
     *
     * @param emitter    reference of the agent sending the disconnection message
     * @param receivers the references of the receivers of the message
     */
    public DisconnectInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
        this.myType = MessageTypes.DISCONNECT;
    }

    /**
     * get the matchingID of message,  which can be : Advertise, reply, select, agree...
     *
     * @return the matchingID of the message
     */
    @Override
    public MessageTypes getMyType() {
        return this.myType;
    }

    /**
     * Transform the message into perception (this method is useful because in the OCE engine we deal with ServiceAgent reference not InfrastructureAgent reference)
     *
     * @param referenceResolver : the component used to resolve the address ServiceAgent <> InfraAgentReference
     * @return the perception equivalent to the message
     */
    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        try {
            return new DisconnectMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }
}
