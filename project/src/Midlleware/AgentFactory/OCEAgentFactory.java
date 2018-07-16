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
import OCE.*;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * InfraAgent Factory implementation : implements the functions in the IOCEAgentFactory Interface to create different type of agent
 * @author Walid YOUNES
 * @version 1.0
 */
public class OCEAgentFactory implements IOCEAgentFactory {

    private Infrastructure infrastructure;
    private ICommunicationAdapter communicationManager;

    public OCEAgentFactory(Infrastructure infrastructure, ICommunicationAdapter communicationManager) {
        this.infrastructure = infrastructure;
        this.communicationManager = communicationManager;
    }

    /**
     * create a service agent
     * @return the association between service InfraAgent created and the physical reference of the agent
     */
    @Override
    public Map.Entry<ServiceAgent, InfraAgentReference> createServiceAgent(OCService attachedService) {
        MyLogger.log(Level.INFO, "Creating the agent for the service * " + attachedService.toString() + " *");
        //Create the component of service InfraAgent
        IPerceptionState myWayOfPerception = new PerceptionAgent();
        IDecisionState myWayOfDecision = new ServiceAgentDecision();
        IActionState myWayOfAction = new ServiceAgentAction();
        // Create The service InfraAgent
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
        InfraAgent associatedInfraAgent = this.infrastructure.creer(attachedService, lifeCycle, this.infrastructure);
        // Associate the serviceAgent to the agent in the infrastructure
        serviceAgent.setMyInfraAgent(associatedInfraAgent);

        AbstractMap.SimpleEntry agentS_referenceAgent_Association = new AbstractMap.SimpleEntry(serviceAgent, associatedInfraAgent.getInfraAgentReference());
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
