/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.AdPerception;

import java.util.ArrayList;

public class AdMessage extends Message {

    private OCService distantService; // the information about the service of the agent that send this advertisement
    /**
     * Create an advertise message
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AdMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

/*
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef, OCService localService) {
        MyLogger.log(Level.INFO, "Treating an advertisement message ! ");
        //Verify the connexion state of the agent
        if (stateConnexionAgent.equals(ServiceAgentConnexionState.NotConnected) || stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){
            //Verify the matching between the services Todo use the matching in the future
            //todo here we are casting the type to MockupService
            MockupService service1 =  (MockupService)localService;
            MockupService service2 = (MockupService)this.distantService;

            //verify the matching
            if(service1.equals(service2)){
                // Send a reply message to the emitter of this message
                ArrayList<InfraAgentReference> replyRecievers = new ArrayList<>();
                replyRecievers.add(this.emitter);
                return new ReplyDecision(serviceAgentRef, replyRecievers);
            }
            else return new EmptyDecision();
        }else return new EmptyDecision();
    }*/

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new AdPerception(referenceResolver.retrieveServiceAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveServiceAgentsByInfraAgentReferences(this.recievers));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

}
