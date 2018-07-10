/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.AgentFactory;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.Agent;
import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Etat.LifeCyrcle;
import Infrastructure.Infrastructure;
import Midlleware.ThreeState.*;
import OCE.*;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Agent Factory implementation : implements the functions in the IAFactory Interface to create different type of agent
 * @author Walid YOUNES
 * @version 1.0
 */
public class AFactory implements IAFactory {

    private Infrastructure infrastructure;

    public AFactory(Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }

    /**
     * create a service agent
     * @return the association between service Agent created and the physical reference of the agent
     */
    @Override
    public Map.Entry<ServiceAgent, ReferenceAgent> createServiceAgent(OCService attachedService) {

        IPerceptionState myWayOfPerception = new PerceptionAgent();
        IDecisionState myWayOfDecision = new DecisionAgentService();
        IActionState myWayOfAction = new ActionAgentService();

        ServiceAgent serviceAgent = new ServiceAgent(attachedService, myWayOfPerception, myWayOfDecision, myWayOfAction);

        //Create the cyrcle : perception -> decision -> action
        PerceptionState perceptionState = new PerceptionState(null,myWayOfPerception );
        ActionState actionState = new ActionState(perceptionState,myWayOfAction);
        DecisionState decisionState = new DecisionState(actionState,myWayOfDecision);
        perceptionState.setNextState(decisionState);
        // create the aent's life cyrcle
        LifeCyrcle lifeCyrcle = new LifeCyrcle(perceptionState);
        // create the agent in the infrastructure
        Agent associatedAgent = this.infrastructure.creer(attachedService, lifeCyrcle);
        // Assoicate the serviceAgent to the agent in the infrastructure
        serviceAgent.setMyAssociatedAgent(associatedAgent);

        AbstractMap.SimpleEntry agentS_referenceAgent_Association = new AbstractMap.SimpleEntry(serviceAgent, associatedAgent.getReferenceAgent());
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
