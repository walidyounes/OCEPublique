/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public interface IAnnuaire extends ICommunication, IGestionAgent {

    void ajouterAgentListener(IAgentListener agentListener);

    void retirerAgentListener(IAgentListener agentListener);

    void ajouterMessageAgentListener(IMessageAgentListener messageAgentListener);

    void retirerMessageAgentListener(IMessageAgentListener messageAgentListener);

    List<IMessageAgentListener> getMessageAgentListeners();

    ConcurrentMap<AgentReference, ConcurrentLinkedQueue<IMessage>> getAgentsMessagesQueues();

    ConcurrentMap<AgentReference, Agent> getAgents();

    //walid
    Agent getAgentByRef(AgentReference agentReference);
}
