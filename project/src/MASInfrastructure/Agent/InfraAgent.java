/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Agent;


import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 */
public class InfraAgent {

    private final InfraAgentReference infraAgentReference;
    private LifeCycle lifeCycle;
    private OCService handledService; //TOdo walid : vérfier si ce champs est utile
    private ICommunication myMailBoxManager;

    public InfraAgent(OCService handledService, LifeCycle lifeCycle, ICommunication myMailBoxManager) {
        // Create a unique ID for the InfraAgent
        this.infraAgentReference = new InfraAgentReference();
        //Attach the service to handle by this agent
        this.handledService = handledService;
        //Create the life cycle of the agent
        this.lifeCycle = lifeCycle;
        // Attache the mailBox manager to let the agent read his message
        this.myMailBoxManager = myMailBoxManager;
    }

    /**
     * run the agent's behavior characterised by his life circle
     */
    public void run() {
        this.lifeCycle.run();
    }

    /**
     * get the references id of the agent
     * @return the unique reference of the agent
     */
    public InfraAgentReference getInfraAgentReference() {
        return infraAgentReference;
    }

    /**
     * get the current state of the agent
     * @return the current state
     */
    public IEtat getState() {
        return lifeCycle.getCurrentState();
    }

    /**
     * get the service handled by the agent
     * @return the handled service
     */
    public OCService getHandledService() {
        return handledService;
    }

    /**
     * Read all the messages from the mailBox of this agent
     * @return the list of the received messages
     */
    public ArrayList<IMessage> readMessages(){
        return this.myMailBoxManager.receiveMessages(this.infraAgentReference);
    }

    /**
     * Read one message from the mail box of this agent
     * @return first message in the mailBox of the agent
     */
    public Optional<IMessage> readMessage(){
        return this.myMailBoxManager.receiveMessage(this.infraAgentReference);
    }

    @Override
    public String toString() {
        return "INFRA.IDAgent{" + infraAgentReference + '}';
    }
}
