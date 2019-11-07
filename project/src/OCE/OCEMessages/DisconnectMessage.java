/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

public class DisconnectMessage extends OCEMessage {

    private DisconnectionReason disconnectionReason;
    /**
     * Create a disconnect message, send by the agent to inform the other agent with whom it's connected that the connection is disestablished
     * @param emitter   : the service agent emitter of this message  (the agent who initiate the disconnection)
     * @param receivers : the set of receivers of the disconnect message (usually is one receiver)
     */
    public  DisconnectMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers){
        this.emitter = emitter;
        this.receivers = receivers;
    }

    /**
     * treat the message and make the suitable decision
     *
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef         : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService        : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a Disconnect message");
        //Cast the matchingID of the agent to a service agent
        ServiceAgent serviceAgent = (ServiceAgent) OCEAgentRef;
        //Change the connexion state of the service agent
        serviceAgent.setMyConnexionState(ServiceAgentConnexionState.NotConnected);
        //Reset the attribute "connectedTo" to initial value (empty)
        serviceAgent.setConnectedTo(Optional.empty());
        return new DoNothingDecision();
    }

    /**
     * This function is called to filter a list of messages depending on their types
     *
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     *
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.DISCONNECT);
    }

    @Override
    public String toString() {
        return "DisconnectMessage{" +
                "emitter=" + this.emitter +
                ", receivers=" + receivers +
                '}';
    }
}
