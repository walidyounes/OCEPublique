/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.EmptyPerception;

public class EmptyMessage extends Message {

    /**
     * Create an empty message
     */
    public EmptyMessage(InfraAgentReference myInfraAgentRef) {
        this.emitter = myInfraAgentRef; // Here the emitter is the agent who recieved this empty message --> it's usuful to have the reference Of the Agent
        this.receivers = null;
    }

/*
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef, OCService localService) {
        MyLogger.log(Level.INFO, "Treating an empty message ! ");
        if(stateConnexionAgent.equals(ServiceAgentConnexionState.Created)){ // if the agent was created ->
            //send a advertisement message todo "this could be enhaced later on
            return new AdvertiseDecision(this.emitter, null);
        }else return new EmptyDecision();

    }
*/
    /**
     * Transfome this empty message into an empty perception (this action is useful because in the OCE engine we deal with ServiceAgent reference not InfraAgent reference)
     * @return the perception equivalent to the message
     */
    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
        try {
            return new EmptyPerception(referenceResolver.retrieveOCEAgentByInfraAgentReference(this.emitter));
        } catch (ReferenceResolutionFailure referenceResolutionFailure) {
            referenceResolutionFailure.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "EmptyMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
