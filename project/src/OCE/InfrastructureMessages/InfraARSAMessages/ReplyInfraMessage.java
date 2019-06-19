/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages.InfraARSAMessages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.ARSAMessages.ReplyMessage;

import java.util.ArrayList;

/**
 * This class represents the response message (Infrastructure level) sent in the second step of the ARSA protocol
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyInfraMessage extends ARSAInfraMessage {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param recievers the references of the receivers of the response, if null == Broadcast
     */
    public ReplyInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.receivers = recievers;
        this.myType = MessageTypes.REPLY;
    }


/*
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating an advertisement message ! ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            // Send a selection message to the emitter of this message
            ArrayList<InfraAgentReference> SelectionRecievers = new ArrayList<>();
            SelectionRecievers.add(this.emitter);
            return new SelectDecision(serviceAgentRef, SelectionRecievers);
        }
        return null;
    }
    */

    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        try {
            return new ReplyMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    /**
     * Get the type of the message
     * @return : the type of this message
     */
    @Override
    public MessageTypes getMyType() {
        return this.myType;
    }

    @Override
    public String toString() {
        return "ReplyInfraMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
