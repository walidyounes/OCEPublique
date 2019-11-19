/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import OCE.ServiceConnection.Connection;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

public interface IFeedbackManager {

    /**
     * Use the configuration send by ICE, compute the difference with the configuration proposed by OCE and use it to annotate the connections
     * @param OCEConfigurationPath      : the path of the file send by ICE
     * @param ICEUserConfigurationPath  : the path of the saved configuration proposed by OCE
     */
    void registerUserConfiguration(String OCEConfigurationPath, String ICEUserConfigurationPath);

    /**
     *
     */
    void collectFeedback();

    /**
     * Add a listener to be informed when the feedback is computed
     * @param listener  : the reference to the listener
     */
    void addFeedbackComputedListener(PropertyChangeListener listener);


    /**
     * Remove a listener from the list of the entities to be informed when the feedback is computed
     * @param listener  : the reference to the listener
     */
    void removeFeedbackComputedListener(PropertyChangeListener listener);
}
