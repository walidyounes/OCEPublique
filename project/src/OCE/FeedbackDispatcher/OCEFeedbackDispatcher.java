/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.FeedbackDispatcher;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Infrastructure;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Medium.Recorder.IRecord;
import OCE.ServiceConnection.Connection;
import OCE.ServiceConnection.IConnectionState;
import OCE.ServiceConnection.ModifiedConnectionState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class OCEFeedbackDispatcher implements PropertyChangeListener{

    ICommunicationAdapter communicationAdapter;             //The reference of the component responsible of transit of messages to the agents
    IRecord  oceRecord;                                     //The reference of the component responsible of resolving references and recording agents
    IOCEBinderAgentFactory binderAgentFactory;              //The reference of the component used to create Binder agents
    List<InfrastructureAgent> listAgentsToInformFeedback;   //The list of agents to wake up to treat user feedback
    Infrastructure infrastructure;                          //The reference of the MAS infrastructure

    /** Holder */
    private static class OCEFeedbackDispatcherSingletonHolder
    {
        //Unique instance not initialized in advance
        private final static OCEFeedbackDispatcher instance = new OCEFeedbackDispatcher();
    }

    /**
     * Access point to the unique instance of the singleton OCEFeedbackDispatcher
     **/
    public static OCEFeedbackDispatcher getInstance()
    {
        return OCEFeedbackDispatcherSingletonHolder.instance;
    }

    /**
     * Private constructor to be called by the Holder to implement the singleton pattern
     */
    private  OCEFeedbackDispatcher() {
        this.listAgentsToInformFeedback = new ArrayList<>();
    }

    /**
     * Set the reference of the component responsible for transit of messages to the agents
     * @param communicationAdapter  : the reference of the medium responsible of transit of messages to the agents
     */
    public void setCommunicationAdapter(ICommunicationAdapter communicationAdapter){
        this.communicationAdapter = communicationAdapter;
    }

    /**
     * Set the reference of the component responsible for resolving references and recording agents
     * @param oceRecord  : the reference of  the component responsible for resolving references and recording agents
     */
    public void setOceRecord(IRecord oceRecord) {
        this.oceRecord = oceRecord;
    }

    /**
     * Set the component responsible for creating binder agents
     * @param binderAgentFactory    : the reference of the component responsible for creating Binder agents in OCE
     */
    public void setBinderAgentFactory(IOCEBinderAgentFactory binderAgentFactory) {
        this.binderAgentFactory = binderAgentFactory;
    }

    /**
     * Set the Multi-agent infrastructure
     * @param infrastructure : the reference for the MAS infrastructure
     */
    public void setInfrastructure(Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }

    /**
     * Treat the annotated connections depending on user feedback, and dispatch the feedback to the corresponding agents
     * @param annotatedConnections  :   the list of connections after annotation using user feedbacks
     */
    public void  dispatchFeedback(List<Connection> annotatedConnections){

        for (Connection connection: annotatedConnections) {
            //Check if the connection is annotated
            if(connection.getMyConnectionState().isPresent()){
                //Treat the connection depending on it's annotation
                connection.getMyConnectionState().get().treatConnection(connection, this.communicationAdapter, this.oceRecord, this.binderAgentFactory, this.listAgentsToInformFeedback,  annotatedConnections);
            }
        }
    }

    /**
     * This method gets called when a bound property is changed (in this case is the property exist in the feedbackManager component in the interaction component between OCE-ICE, when the feedback is computed
     * and the connections were annotated the listeners are notified
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equalsIgnoreCase("AnnotatedConnection")){
            System.out.println("Value changed " + evt.getNewValue());
            //Clear previous reference of the agents in the list of agents to wake up to treat feedback
            this.listAgentsToInformFeedback.clear();
            //Treat the annotated connections
            dispatchFeedback((List<Connection>) evt.getNewValue());
            //Lunch in the infrastructure the special scheduling
            int numberOfCycle = this.listAgentsToInformFeedback.size()*3;
            this.infrastructure.startSpecialScheduling(this.listAgentsToInformFeedback, numberOfCycle);
        }

    }
}
