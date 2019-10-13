/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import MOICE.feedbackManager.FeedbackManager;
import OCE.OCEMessages.FeedbackMessage;

import java.io.File;

public class TestFeedbackGeneration {


    public static void main(String[] args){
        File fileOCE = new File("MyLogFiles\\oldFile.xml");
        File fileICE = new File("MyLogFiles\\newFile.xml");

        FeedbackManager feedbackManager = new FeedbackManager();
        feedbackManager.registerUserConfiguration(fileOCE, fileICE);

    }
}
