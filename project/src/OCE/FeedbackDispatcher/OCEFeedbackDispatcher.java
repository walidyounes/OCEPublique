/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.FeedbackDispatcher;

import MASInfrastructure.Communication.ICommunication;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceConnection.Connection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class OCEFeedbackDispatcher implements PropertyChangeListener{

    ICommunicationAdapter communicationAdapter;         //The reference of the medium responsible of transit of messages to the agents

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
     * Set the instance of the component responsible for transit of messages to the agents
     * @param communicationAdapter  : the reference of the medium responsible of transit of messages to the agents
     */
    public void setCommunicationAdapter(ICommunicationAdapter communicationAdapter){
        this.communicationAdapter = communicationAdapter;
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
                connection.getMyConnectionState().get().treatConnection(connection, this.communicationAdapter);
            }
        }

    }

    /**
     * This method gets called when a bound property is changed (in this case is the property exist in the feedbackManager component in the interaction component between OCE-ICE, when the feedback is computed
     * and the connections were annotated the listeners are notified)
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
           System.out.println("Value changed " + evt.getNewValue());
           //Treat the annotated connections
           dispatchFeedback((List<Connection>) evt.getNewValue());
    }
}
