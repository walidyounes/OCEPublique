/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Infrastructure;
import MASInfrastructure.State.LifeCycle;
import Midlleware.ThreeState.*;
import OCE.Agents.AgentPerception;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.BinderAgentPack.BinderAgentAction;
import OCE.Agents.BinderAgentPack.BinderAgentDecision;
import OCE.Agents.IDAgent;
import OCE.Medium.Medium;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;

public class OCEBinderAgentFactory implements IOCEBinderAgentFactory {

    private Infrastructure infrastructure;
    private Medium medium; // it's stand for communicationManager and recordManager
    private static int binderAgentSerialNumber=0;

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
        OCELogger.log(Level.INFO, "Creating the binder agent * " );
        //Create the attributes of service InfrastructureAgent
        //Create the perception component
        IPerceptionState myWayOfPerception = new AgentPerception();

        //Create the decision component
        IActionState myWayOfAction = new BinderAgentAction();
        // Create The binder Agent and Register in the binder agent the reference to the Record component of OCE 'the medium'
        BinderAgent binderAgent = new BinderAgent(myWayOfPerception, null, myWayOfAction, this.medium);

        //Create the decision component And Update the referenceResolver (Record) of the decision component of the agent
        IDecisionState myWayOfDecision = new BinderAgentDecision(binderAgent, this.medium);

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
        InfrastructureAgent associatedInfrastructureAgent = this.infrastructure.createInfrastructureAgent(null, lifeCycle, this.infrastructure);
        // Associate the serviceAgent to the agent in the infrastructure
        binderAgent.setMyInfrastructureAgent(associatedInfrastructureAgent);
        // Add the reference of the binder Agent in the Action state module
        myWayOfAction.setBinderAgent(binderAgent);

        // Update the attributes of the perception with the reference of the Infrastructure Agent
        myWayOfPerception.setInfraAgent(associatedInfrastructureAgent);

        //Make the ID of the binder Agent the same as the associated infrastructure agent
        binderAgent.setMyIDAgent(new IDAgent(""+ associatedInfrastructureAgent.getInfraAgentReference().getReferenceInterne()));

        //Set the name for the visualization of this agent as "BinderAgent' + Sequential Number
        String visualizationName = "BA-"+this.binderAgentSerialNumber;
        binderAgent.getMyID().setVisualizingName(visualizationName);



        // Increment the serial Number for binder agents
        this.binderAgentSerialNumber++;

        // Register the binder Agent in the Record
        medium.registerOCEAgent(binderAgent, associatedInfrastructureAgent.getInfraAgentReference());


        AbstractMap.SimpleEntry agentB_referenceAgent_Association = new AbstractMap.SimpleEntry(binderAgent, associatedInfrastructureAgent.getInfraAgentReference());
        return agentB_referenceAgent_Association;
    }
}
