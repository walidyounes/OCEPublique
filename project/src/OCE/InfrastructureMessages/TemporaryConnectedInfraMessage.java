/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.TemporaryConnectedMessage;

import java.util.ArrayList;

public class TemporaryConnectedInfraMessage extends InfraMessage {

    /**
     * Create a Temporary connected infra message
     *
     * @param emitter    reference of the agent sending the temporary connected message (it's usually A binder Agent)
     * @param receivers the references of the receivers of the message
     */
    public TemporaryConnectedInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers) {
        this.emitter= emitter;
        this.receivers = receivers;
        this.myType = MessageTypes.TemporaryConnected;
    }

    /**
     * get the transmitter of the message
     *
     * @return the reference of the transmitter of the message
     */
    @Override
    public InfraAgentReference getEmitter() {
        return super.getEmitter();
    }

    /**
     * set the reference of the transmitter of the message
     *
     * @param emitter : the reference of the transmitter
     */
    @Override
    public void setEmitter(InfraAgentReference emitter) {
        super.setEmitter(emitter);
    }

    /**
     * get the list of the receivers of the message
     *
     * @return the receivers of the message
     */
    @Override
    public ArrayList<InfraAgentReference> getReceivers() {
        return super.getReceivers();
    }

    /**
     * set the list of receivers for this message
     *
     * @param receivers : the list of receivers
     */
    @Override
    public void setReceivers(ArrayList<InfraAgentReference> receivers) {
        super.setReceivers(receivers);
    }

    /**
     * get the matchingID of message,  which can be : Advertise, reply, select, agree, bind, disconnect, TemporaryConnected.....
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
            return new TemporaryConnectedMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }
}
