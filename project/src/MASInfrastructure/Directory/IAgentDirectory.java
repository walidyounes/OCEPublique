/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public interface IAgentDirectory extends ICommunication, IAgentDirectoryManager {

    void ajouterAgentListener(IAgentListener agentListener);

    void retirerAgentListener(IAgentListener agentListener);

    void ajouterMessageAgentListener(IMessageAgentListener messageAgentListener);

    void retirerMessageAgentListener(IMessageAgentListener messageAgentListener);

    List<IMessageAgentListener> getMessageAgentListeners();

    ConcurrentMap<InfraAgentReference, ConcurrentLinkedQueue<IMessage>> getAgentsMessagesQueues();

    ConcurrentMap<InfraAgentReference, InfraAgent> getAgents();

    //walid
    InfraAgent getAgentByRef(InfraAgentReference infraAgentReference);
}
