/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Infrastructure;
import Midlleware.ThreeState.*;
import OCE.*;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Agent Factory implementation : implements the functions in the IAgentFactory Interface to create different type of agent
 * @author Walid YOUNES
 * @version 1.0
 */
public class AgentFactory implements IAgentFactory {

    private Infrastructure infrastructure;
    private ICommunicationAdapter communicationManager;

    public AgentFactory(Infrastructure infrastructure, ICommunicationAdapter communicationManager) {
        this.infrastructure = infrastructure;
        this.communicationManager = communicationManager;
    }

    /**
     * create a service agent
     * @return the association between service Agent created and the physical reference of the agent
     */
    @Override
    public Map.Entry<ServiceAgent, AgentReference> createServiceAgent(OCService attachedService) {
        MyLogger.log(Level.INFO, "Creating the agent for the service * " + attachedService.toString() + " *");
        //Create the component of service Agent
        IPerceptionState myWayOfPerception = new PerceptionAgent();
        IDecisionState myWayOfDecision = new DecisionServiceAgent();
        IActionState myWayOfAction = new ActionServiceAgent();
        // Create The service Agent
        ServiceAgent serviceAgent = new ServiceAgent(attachedService, myWayOfPerception, myWayOfDecision, myWayOfAction);

        // Update the attributes of the perception with the reference of the serviceAgent and the communicationManager
        myWayOfPerception.setServiceAgent(serviceAgent);
        myWayOfPerception.setCommunicationManager(this.communicationManager);

        //Create the cycle : perception -> decision -> action
        PerceptionState perceptionState = new PerceptionState(null,myWayOfPerception );
        ActionState actionState = new ActionState(perceptionState,myWayOfAction);
        DecisionState decisionState = new DecisionState(actionState,myWayOfDecision);
        perceptionState.setNextState(decisionState);
        // create the agent's life cycle
        LifeCycle lifeCycle = new LifeCycle(perceptionState);
        // create the agent in the infrastructure
        Agent associatedAgent = this.infrastructure.creer(attachedService, lifeCycle);
        // Associate the serviceAgent to the agent in the infrastructure
        serviceAgent.setMyAssociatedAgent(associatedAgent);

        AbstractMap.SimpleEntry agentS_referenceAgent_Association = new AbstractMap.SimpleEntry(serviceAgent, associatedAgent.getAgentReference());
        return agentS_referenceAgent_Association;
    }

    /**
     * create a binding agent
     * @return the binding agent
     */
    @Override
    public BinderAgent createBinderAgent() {
        return null;
    }
}
