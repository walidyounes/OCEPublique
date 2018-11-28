/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.ReplyPerception;

import java.util.ArrayList;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ReplyMessage extends Message {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param recievers the references of the receivers of the response, if null == Broadcast
     */
    public ReplyMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.receivers = recievers;
        this.myType = MessageTypes.REPLY;
    }


/*
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
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
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new ReplyPerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter), referenceResolver.retrieveOCEAgentsByInfraAgentReferences(this.receivers));
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
        return "ReplyMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
