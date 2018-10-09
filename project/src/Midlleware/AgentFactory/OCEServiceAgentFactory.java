/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Infrastructure;
import Midlleware.ThreeState.*;
import OCE.Agents.*;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.BinderAgentPack.BinderAgentAction;
import OCE.Agents.BinderAgentPack.BinderAgentDecision;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentAction;
import OCE.Agents.ServiceAgentPack.ServiceAgentDecision;
import OCE.Medium.Medium;
import OCE.Selection.IMessageSelection;
import OCE.Selection.RandomSelection;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * InfraAgent Factory implementation : implements the functions in the IOCEServiceAgentFactory Interface to create different type of agent
 * @author Walid YOUNES
 * @version 1.0
 */
public class OCEServiceAgentFactory implements IOCEServiceAgentFactory {

    private Infrastructure infrastructure;
    private Medium medium; // it's stand for communicationManager and recordManager

    public OCEServiceAgentFactory(Infrastructure infrastructure, Medium communicationManager) {
        this.infrastructure = infrastructure;
        this.medium = communicationManager;
    }

    /**
     * create a service agent
     * @return the association between service InfraAgent created and the physical reference of the agent
     */
    @Override
    public Map.Entry<ServiceAgent, InfraAgentReference> createServiceAgent(OCService attachedService) {
        //MyLogger.log(Level.INFO, "Creating the agent for the service * " + attachedService.toString() + " *");
        //Create the attributes of service InfraAgent
        //Create the perception component
        IPerceptionState myWayOfPerception = new AgentPerception();

        //Create the decision component
        IActionState myWayOfAction = new ServiceAgentAction();
        // Create The service InfraAgent
        ServiceAgent serviceAgent = new ServiceAgent(attachedService, myWayOfPerception, null, myWayOfAction);

        //Create the strategy of message selection
        IMessageSelection messageSelectionStrategy = new RandomSelection(Integer.MAX_VALUE);
        //Create the decision component And Update the referenceResolver (Record) of the decision component of the agent
        IDecisionState myWayOfDecision = new ServiceAgentDecision(messageSelectionStrategy, serviceAgent, this.medium);

        //update the decision component in the service Agent
        serviceAgent.setMyWayOfDecision(myWayOfDecision);
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
        InfraAgent associatedInfraAgent = this.infrastructure.creer(attachedService, lifeCycle, this.infrastructure);
        // Associate the serviceAgent to the agent in the infrastructure
        serviceAgent.setMyInfraAgent(associatedInfraAgent);

        // Update the attributes of the perception with the reference of the Infrastructure Agent 
        myWayOfPerception.setInfraAgent(associatedInfraAgent);

        // Create the Binder Agent and update the service agent
        IOCEBinderAgentFactory myBinderAgentFactory = new OCEBinderAgentFactory(this.infrastructure,this.medium);
        serviceAgent.setMyBinderAgentFactory(myBinderAgentFactory);

        AbstractMap.SimpleEntry agentS_referenceAgent_Association = new AbstractMap.SimpleEntry(serviceAgent, associatedInfraAgent.getInfraAgentReference());
        return agentS_referenceAgent_Association;
    }


}
