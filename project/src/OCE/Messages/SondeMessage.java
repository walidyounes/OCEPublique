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
import OCE.Perceptions.SondePerception;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class SondeMessage extends Message {

    private Boolean exist; // Boolean attribute used to inform the agent if it's service still exsit or not

    /**
     * Create A Sonde Message
     * @param exist :  boolean value informing if the service attached to the agent is still existing in the environement or not
     * @param recievers : the references of the recievers of the ad, if null == Broadcast
     */
    public SondeMessage(Boolean exist, ArrayList<InfraAgentReference> recievers) {
        this.exist = exist;
        this.emitter = null; // The sonde has no reference
        this.recievers = recievers;
    }

    /**
     * Create a Sonde message (empty message)
     */
    public SondeMessage() {
        this.exist = false;
        this.emitter = null;
        this.recievers = null;
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
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a sonde message ! ");
        return null;
    }
    */

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {
            return new SondePerception();
    }

    @Override
    public String toString() {
        return "SondeMessage{" +
                "exist=" + exist +
                ", emitter=" + emitter +
                ", recievers=" + recievers +
                '}';
    }
}
