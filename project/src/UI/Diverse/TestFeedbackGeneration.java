/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import MOICE.feedbackManager.FeedbackManager;
import OCE.OCEMessages.FeedbackMessage;
import OCE.ServiceConnection.Connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestFeedbackGeneration {


    public static void main(String[] args){
        File fileOCE = new File("MyLogFiles\\oldFile.xml");
        File fileICE = new File("MyLogFiles\\newFile.xml");
        List<Connection> listOCEConnections  = new ArrayList<>();

        FeedbackManager feedbackManager = new FeedbackManager();
        feedbackManager.registerUserConfiguration("MyLogFiles\\oldFile.xml", "MyLogFiles\\newFile.xml");

    }
}
