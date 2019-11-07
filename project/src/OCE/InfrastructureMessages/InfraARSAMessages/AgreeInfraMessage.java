/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages.InfraARSAMessages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.ARSAMessages.AgreeMessage;

import java.util.ArrayList;

/**
 * This class represents the agree message (Infrastructure level) sent in the fourth and last step of the ARSA protocol
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeInfraMessage extends ARSAInfraMessage {

    /**
     * Creer un message d'acceptation
     *
     */
    public AgreeInfraMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter = emitter;
        this.receivers = recievers;
        this.myType = MessageTypes.AGREE;
    }

/*    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        OCELogger.log(Level.INFO, "Treating a agreement message ! ");
        return null;
    }*/

    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
        try {
            return new AgreeMessage(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    /**
     * Get the matchingID of this message
     * @return the matchingID of the message
     */
    @Override
    public MessageTypes getMyType() {
        return myType;
    }

    @Override
    public String toString() {
        return "AgreeInfraMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
