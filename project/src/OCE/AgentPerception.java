/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Communication.IMessage;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent one way hwo we can perceive the environnement, it's used both by the ServiceAgent and the BindingAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgentPerception implements IPerceptionState {

    private InfraAgent myInfraAgent;


    /**
     * Update the reference of the Infrastructure Agent that this component is attached to
     * @param myInfraAgent : the Infrastructure Agent whom this component is attached to
     */
    @Override
    public void setInfraAgent(InfraAgent myInfraAgent) {
        this.myInfraAgent = myInfraAgent;
    }

    /**
     * Update the communication manager used to retrieve the messages from the mail Box
     * @param communicationManager
     */
   /* @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }*/

    /**
     * Implement the perception process of the agent, which consist in reading the received messages
     */
    @Override
    public ArrayList<IMessage> percept() {
        MyLogger.log(Level.INFO, "The service agent is percepting the envirnment !");
        // Read the messages from the mail-Box
        ArrayList<IMessage> messages = this.myInfraAgent.readMessages();
        MyLogger.log(Level.INFO, "La Liste des messages reçu est !"+ messages.toString());
        return messages;
    }
}
