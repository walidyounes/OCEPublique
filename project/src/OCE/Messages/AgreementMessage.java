/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.AdPerception;
import OCE.Perceptions.AgreementPerception;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class AgreementMessage extends Message {

    /**
     * Creer un message d'acceptation
     *
     */
    public AgreementMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter = emitter;
        this.recievers = recievers;
    }

/*    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a agreement message ! ");
        return null;
    }*/

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new AgreementPerception(referenceResolver.retrieveServiceAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveServiceAgentsByInfraAgentReferences(this.recievers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }
}
