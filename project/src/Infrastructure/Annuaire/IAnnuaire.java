/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import sma.infrastructure.agent.Agent;
import sma.infrastructure.agent.ReferenceAgent;
import Infrastructure.Communication.ICommunication;
import Infrastructure.Communication.IMessageAgent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public interface IAnnuaire extends ICommunication, IGestionAgent {

    void ajouterAgentListener(IAgentListener agentListener);

    void retirerAgentListener(IAgentListener agentListener);

    void ajouterMessageAgentListener(IMessageAgentListener messageAgentListener);

    void retirerMessageAgentListener(IMessageAgentListener messageAgentListener);

    List<IMessageAgentListener> getMessageAgentListeners();

    ConcurrentMap<ReferenceAgent, ConcurrentLinkedQueue<IMessageAgent>> getAgentsMessagesQueues();

    ConcurrentMap<ReferenceAgent, Agent> getAgents();

    //walid
    Agent getAgentByRef(ReferenceAgent referenceAgent);
}
