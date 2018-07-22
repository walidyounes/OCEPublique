/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.AbstractDecision;
import OCE.ServiceAgent;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class SondePerception extends AbstractPerception {

    private Boolean exist; // Boolean attribute used to inform the agent if it's service still exsit or not

    /**
     * Create A Sonde Message
     * @param exist :  boolean value informing if the service attached to the agent is still existing in the environement or not
     * @param recievers : the references of the recievers of the ad, if null == Broadcast
     */
    public SondePerception(Boolean exist, ArrayList<ServiceAgent> recievers) {
        this.exist = exist;
        this.emitter = null; // The sonde has no reference
        this.recievers = recievers;
    }

    /**
     * Create a Sonde message (empty message)
     */
    public SondePerception() {
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

    /**
     * treat the sonde message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param serviceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the deicision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, ServiceAgent serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a sonde message ! ");
        return null;
    }
}
