/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import java.io.File;

public interface IFeedbackManager {

    void registerUserConfiguration(File OCEConfiguration, File ICEUserConfiguration);

    void collectFeedback();
}
