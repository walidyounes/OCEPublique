/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.AgreePerception;

import java.util.ArrayList;

public class AgreeMessage extends Message {

    /**
     * Creer un message d'acceptation
     *
     */
    public AgreeMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter = emitter;
        this.receivers = recievers;
    }

/*    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a agreement message ! ");
        return null;
    }*/

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new AgreePerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "AgreeMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
