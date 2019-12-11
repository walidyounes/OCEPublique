/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.State.LifeCycle;
import MASInfrastructure.Infrastructure;
import Midlleware.ThreeState.*;
import OCE.Agents.*;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentAction;
import OCE.Agents.ServiceAgentPack.ServiceAgentDecision;
import OCE.Medium.Medium;
import OCE.Selection.IMessageSelection;
import OCE.Selection.RandomSelection;

import java.util.AbstractMap;
import java.util.Map;

/**
 * InfrastructureAgent Factory implementation : implements the functions in the IOCEServiceAgentFactory Interface to create different matchingID of agent
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
     * @return the association between service InfrastructureAgent created and the physical reference of the agent
     */
    @Override
    public Map.Entry<ServiceAgent, InfraAgentReference> createServiceAgent(OCService attachedService) {
        //OCELogger.log(Level.INFO, "Creating the agent for the service * " + attachedService.toString() + " *");
        //Create the attributes of service InfrastructureAgent
        //Create the perception component
        IPerceptionState myWayOfPerception = new AgentPerception();

        //Create the decision component
        IActionState myWayOfAction = new ServiceAgentAction();
        // Create The service InfrastructureAgent
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
        InfrastructureAgent associatedInfrastructureAgent = this.infrastructure.createInfrastructureAgent(attachedService, lifeCycle, this.infrastructure);
        // Associate the serviceAgent to the agent in the infrastructure
        serviceAgent.setMyInfrastructureAgent(associatedInfrastructureAgent);

        // Update the attributes of the perception with the reference of the Infrastructure Agent 
        myWayOfPerception.setInfraAgent(associatedInfrastructureAgent);

        // Create the Binder Agent and update the service agent
        IOCEBinderAgentFactory myBinderAgentFactory = new OCEBinderAgentFactory(this.infrastructure,this.medium);
        serviceAgent.setMyBinderAgentFactory(myBinderAgentFactory);

        //Make the ID of the service Agent the same as the associated infrastructure agent
        serviceAgent.setMyIDAgent(new IDAgent(""+ associatedInfrastructureAgent.getInfraAgentReference().getReferenceInterne()));
        //Set the name for the visualization of this agent as the name of it's associated  service
        String visualizationName = ((MockupService) attachedService).getSummaryStringRepresentation();
        serviceAgent.getMyID().setVisualizingName(""+visualizationName);

        AbstractMap.SimpleEntry agentS_referenceAgent_Association = new AbstractMap.SimpleEntry(serviceAgent, associatedInfrastructureAgent.getInfraAgentReference());
        return agentS_referenceAgent_Association;
    }


}
