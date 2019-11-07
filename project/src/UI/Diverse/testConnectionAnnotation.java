/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import MOICE.feedbackManager.FeedbackManager;
import OCE.ServiceConnection.Connection;
import UI.XMLFileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class testConnectionAnnotation {



    public static void main(String[] args){
        //Create components and services from an XML File
        List<MockupComponent> listComponents = XMLFileTools.readXMLComponentFile("MyLogFiles\\oldFile.xml");
        System.out.println(" Components =  " + listComponents);

        //Simulate connection between the services
            //Get the services (their is two)
            MockupService firstService = null;
            MockupService secondService = null;
            if(listComponents.get(0).getRequiredServices().size()>0){
                firstService = (MockupService) listComponents.get(0).getRequiredServices().get(0);
            }else{
                firstService = (MockupService) listComponents.get(0).getProvidedServices().get(0);
            }

            if(listComponents.get(1).getRequiredServices().size()>0){
                secondService = (MockupService) listComponents.get(1).getRequiredServices().get(0);
            }else{
                secondService = (MockupService) listComponents.get(1).getProvidedServices().get(0);
            }

        //Create the list of simulated connections
        List<Connection> listConnections = new ArrayList<>();
        Connection myConnection = new Connection(null, null, firstService, secondService, null);

        listConnections.add(myConnection);

        //Calculate feedback
        File fileOCE = new File("MyLogFiles\\oldFile.xml");
        File fileICE = new File("MyLogFiles\\newFile.xml");

        FeedbackManager feedbackManager = new FeedbackManager();
        feedbackManager.registerUserConfiguration(fileOCE, fileICE, listConnections);

        //Print the list of connection
        System.out.println(" " + listConnections);
    }
}
