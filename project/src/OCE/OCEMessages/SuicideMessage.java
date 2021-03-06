/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.OCEDecision;

import java.util.ArrayList;
import java.util.logging.Level;

public class SuicideMessage extends OCEMessage {

    private Boolean exist; // Boolean attribute used to inform the agent if it's service still exists or not

    /**
     * Create A suicideMessage
     * @param exist :  boolean value informing if the service attached to the agent is still existing in the environment or not
     * @param receivers : the references of the receivers of the ad, if null == Broadcast
     */
    public SuicideMessage(Boolean exist, ArrayList<OCEAgent> receivers) {
        this.exist = exist;
        this.emitter = null; // The probe has no reference
        this.receivers = receivers;
    }

    /**
     * Create a Probe message (empty message)
     */
    public SuicideMessage() {
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

    /**
     * treat the probe message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, CONNECTED, NotConnected, WAITING"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a probe message ");
        return null;
    }
    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.SUICIDE);
    }
}
