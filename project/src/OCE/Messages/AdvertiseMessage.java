/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.AdvertisePerception;

import java.util.ArrayList;

public class AdvertiseMessage extends Message {

    private OCService myService; // the information about the service of the agent that send this advertisement
    /**
     * Create an advertise message
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the receivers of the ad, if empty == Broadcast
     */
    public AdvertiseMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.receivers = recievers;
    }

    public AdvertiseMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> receivers, OCService myService) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.myService = myService;
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
            MockupService service2 = (MockupService)this.myService;

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
            if( ! this.receivers.isEmpty()){ // If the message was send in broadcast the receivers would be empty
                return new AdvertisePerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers), this.myService);
            }else
                return new AdvertisePerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), new ArrayList<>(), this.myService);

        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "AdvertiseMessage{" +
                "myService=" + myService +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
