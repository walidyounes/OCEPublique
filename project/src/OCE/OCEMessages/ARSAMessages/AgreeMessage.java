/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages.ARSAMessages;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEMessages.MessageTypes;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the agree message sent in the fourth and last step of the ARSA protocol (at this step the physical connection can be triggered)
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgreeMessage extends ARSAMessage {

    /**
     * create an agreement message
     *
     */
    public AgreeMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }

    /**
     * treat the agree message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "CREATED, CONNECTED, NOT_CONNECTED, WAITING"
     * @param oceAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent oceAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, oceAgentRef + " treats an agreement message ");
        // change the connexion's state of the agent
        //((ServiceAgent)oceAgentRef).setMyConnexionState(ServiceAgentConnexionState.WAITING); //Walid 02/09/2019 : je pense que c'est inutile de faire ça - 04/10/2019 : je le remet car j'ai changé dans le code de decision de l'agent - 04/09 17h : je l'ai enlevé
        ((ServiceAgent)oceAgentRef).setMyConnexionState(ServiceAgentConnexionState.EXPECTING_FEEDBACK); // Todo walid 13/12/2019 : je l'ai ajouté car lorsqu'un agent traite se message il n'est plus en mode WAITING FOR AGREE il passe directement vers l'attente de feedback
        //OCELogger.log(Level.INFO, oceAgentRef + " is now in waiting state ");
        return new DoNothingDecision();
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
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.AGREE);
    }
}
