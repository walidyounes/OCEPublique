/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgent;
import Midlleware.ThreeState.IPerceptionState;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class represent one way hwo we can perceive the environment, it's used both by the ServiceAgent and the BindingAgent
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
    public ArrayList<InfraMessage> percept() {

        // Read the infraMessages from the mail-Box and convert them from IMessage to InfraMessage
        ArrayList<InfraMessage> infraMessages = new ArrayList<>(this.myInfraAgent.readMessages().stream().map(x -> (InfraMessage)x).collect(Collectors.toList()));

        MyLogger.log(Level.INFO, "Agent : Perception -> Received infraMessages = "+ infraMessages.toString());

//        if (infraMessages.isEmpty()){ // If the agent didn't receive any infraMessages he send a EmptyInfraMessage (equivalent to an Empty Perception)
//            infraMessages.add(new EmptyInfraMessage(myInfraAgent.getInfraAgentReference()));
//        }
        return infraMessages;
    }
}
