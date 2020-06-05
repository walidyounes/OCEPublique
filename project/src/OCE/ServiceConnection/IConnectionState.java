/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ServiceConnection;

import MASInfrastructure.Agent.InfrastructureAgent;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;

import java.util.List;

public interface IConnectionState {

    /**
     * Treat the connexion according to the it's state
     * @param connection                :  the connection to be treated
     * @param communicationManager      : the medium used to send messages to the concerned agent which are part of this connection
     * @param oceRecord                 : the reference to the component responsible for reference resolving
     * @param binderAgentFactory        : the reference to the component which allows creating binder agents
     * @param infrastructureAgentList   : the list of agents to wake up to inform them of the arrival of user feedback
     * @param annotatedConnections      : the list of annotated connections received after user's feedback
     */
    void treatConnection(Connection connection, ICommunicationAdapter communicationManager, IRecord oceRecord, IOCEBinderAgentFactory binderAgentFactory, List<InfrastructureAgent> infrastructureAgentList, List<Connection> annotatedConnections);
}
