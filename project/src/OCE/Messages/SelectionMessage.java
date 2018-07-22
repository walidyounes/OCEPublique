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
import OCE.Perceptions.SelectionPerception;
import OCE.ServiceAgentConnexionState;

import java.util.ArrayList;
import java.util.logging.Level;

public class SelectionMessage extends Message {
    private InfraAgentReference binderAgent; // The reference of the binding InfraAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param recievers the references of the recievers of the selection message
     * @param bindingAgent the agent responsible of executing the physical binding
     */
    public SelectionMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers, InfraAgentReference bindingAgent) {
        this.emitter = emitter;
        this.recievers = recievers;
        this.binderAgent = bindingAgent;
    }

    /**
     * create a Selection message (empty)
     */
    public SelectionMessage() {
        this.emitter = null;
        this.recievers= null;
        this.binderAgent = null;
    }

    /**
     * get the reference of the binding agent
     * @return the reference
     */
    public InfraAgentReference getBinderAgent() {
        return binderAgent;
    }

    /**
     * set the reference of the binding agent
     * @param binderAgent : the reference of the binding agent
     */
    public void setBinderAgent(InfraAgentReference binderAgent) {
        this.binderAgent = binderAgent;
    }

/*
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, InfraAgentReference serviceAgentRef,  OCService localService) {
        MyLogger.log(Level.INFO, "Treating a selection message ! ");
        return null;
    }*/

    @Override
    public AbstractPerception toPerception(IRecord referenceResolver) {

            return new SelectionPerception(); // TOdO : retravailler cette partie

    }
}
