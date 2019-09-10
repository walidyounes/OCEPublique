/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Infrastructure;
import Midlleware.ThreeState.*;
import OCE.Agents.AgentPerception;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.BinderAgentPack.BinderAgentAction;
import OCE.Agents.BinderAgentPack.BinderAgentDecision;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Medium.Medium;
import OCE.Selection.IMessageSelection;
import OCE.Selection.RandomSelection;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;

public class OCEBinderAgentFactory implements IOCEBinderAgentFactory {

    private Infrastructure infrastructure;
    private Medium medium; // it's stand for communicationManager and recordManager

    public OCEBinderAgentFactory(Infrastructure infrastructure, Medium communicationManager) {
        this.infrastructure = infrastructure;
        this.medium = communicationManager;
    }
    /**
     * create a binding agent
     * @return the binding agent
     */
    @Override
    public Map.Entry<BinderAgent, InfraAgentReference> createBinderAgent() {
        MyLogger.log(Level.INFO, "Creating the binder agent * " );
        //Create the attributes of service InfraAgent
        //Create the perception component
        IPerceptionState myWayOfPerception = new AgentPerception();

        //Create the decision component
        IActionState myWayOfAction = new BinderAgentAction();
        // Create The service InfraAgent
        BinderAgent binderAgent = new BinderAgent(myWayOfPerception, null, myWayOfAction);

        //Create the strategy of message selection
        IMessageSelection messageSelectionStrategy = new RandomSelection(Integer.MAX_VALUE);
        //Create the decision component And Update the referenceResolver (Record) of the decision component of the agent
        IDecisionState myWayOfDecision = new BinderAgentDecision(messageSelectionStrategy, binderAgent, this.medium);

        //update the decision component in the service Agent
        binderAgent.setMyWayOfDecision(myWayOfDecision);
        //Update the communication component of the action component of the agent
        myWayOfAction.setCommunicationManager(this.medium);

        //Create the cycle : perception -> decision -> action
        PerceptionState perceptionState = new PerceptionState(null,myWayOfPerception );
        ActionState actionState = new ActionState(perceptionState,myWayOfAction);
        DecisionState decisionState = new DecisionState(actionState,myWayOfDecision);
        perceptionState.setNextState(decisionState);
        // create the agent's life cycle
        LifeCycle lifeCycle = new LifeCycle(perceptionState);
        // create the agent in the infrastructure
        InfraAgent associatedInfraAgent = this.infrastructure.creer(null, lifeCycle, this.infrastructure);
        // Associate the serviceAgent to the agent in the infrastructure
        binderAgent.setMyInfraAgent(associatedInfraAgent);
        // Add the reference of the binder Agent in the Action state module
        myWayOfAction.setBinderAgent(binderAgent);

        // Update the attributes of the perception with the reference of the Infrastructure Agent
        myWayOfPerception.setInfraAgent(associatedInfraAgent);

        //Make the ID of the binder Agent the same as the associated infrastructure agent
        binderAgent.setMyIDAgent(new IDAgent(""+associatedInfraAgent.getInfraAgentReference().getReferenceInterne()));

        // Register the binder Agent in the Record
        medium.registerOCEAgent(binderAgent, associatedInfraAgent.getInfraAgentReference());
        AbstractMap.SimpleEntry agentB_referenceAgent_Association = new AbstractMap.SimpleEntry(binderAgent, associatedInfraAgent.getInfraAgentReference());
        return agentB_referenceAgent_Association;
    }
}
