/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import OCE.ServiceConnection.Connection;

import java.io.File;
import java.util.List;

public interface IFeedbackManager {

    void registerUserConfiguration(File OCEConfiguration, File ICEUserConfiguration, List<Connection> OCEConnectionList);

    void collectFeedback();
}
