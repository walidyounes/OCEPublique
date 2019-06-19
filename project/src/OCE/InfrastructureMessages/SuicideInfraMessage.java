/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.InfrastructureMessages;

import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.SuicideMessage;

import java.util.ArrayList;

public class SuicideInfraMessage extends InfraMessage {

    private Boolean exist; // Boolean attribute used to inform the agent if it's service still exsit or not

    /**
     * Create A Sonde InfraMessage
     * @param exist :  boolean value informing if the service attached to the agent is still existing in the environement or not
     * @param receivers : the references of the receivers of the ad, if null == Broadcast
     */
    public SuicideInfraMessage(Boolean exist, ArrayList<InfraAgentReference> receivers) {
        this.exist = exist;
        this.emitter = null; // The sonde has no reference
        this.receivers = receivers;
        this.myType = MessageTypes.SUICIDE;
    }

    /**
     * Create a Sonde message (empty message)
     */
    public SuicideInfraMessage() {
        this.exist = false;
        this.emitter = null;
        this.receivers = null;
    }

    /**
     * get the value of the boolean indiating if the sevice still exist or not
     * @return the value of the boolean
     */
    public Boolean getExist() {
        return exist;
    }

    /**
     * Set the value of the boolean indicating if the service exist or not
     * @param exist
     */
    public void setExist(Boolean exist) {
        this.exist = exist;
    }
/*
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a sonde message ! ");
        return null;
    }
    */

    @Override
    public OCEMessage toOCEMessage(IRecord referenceResolver) {
            return new SuicideMessage();
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
        return "SuicideInfraMessage{" +
                "exist=" + exist +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
