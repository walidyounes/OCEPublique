/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import MOICE.MOICEProbe;
import MOICE.feedbackManager.FeedbackManager;
import Midlleware.AgentFactory.OCEServiceAgentFactory;
import OCE.Agents.AgentPerception;
import OCE.Agents.BinderAgentPack.BinderAgentDecision;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentAction;
import OCE.Agents.ServiceAgentPack.ServiceAgentDecision;
import OCE.FeedbackDispatcher.OCEFeedbackDispatcher;
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
        ServiceAgent serviceAgent = new ServiceAgent(new IDAgent(), firstService, new AgentPerception(), new ServiceAgentDecision(null, null, null), new ServiceAgentAction());
        List<Connection> listConnections = new ArrayList<>();
        Connection myConnection = new Connection(serviceAgent, serviceAgent, firstService, secondService, null);

        listConnections.add(myConnection);


        //Create an instance for the feedback manager
        FeedbackManager feedbackManager = new FeedbackManager();
        //Create the instance of the feedback Dispatcher
        OCEFeedbackDispatcher oceFeedbackDispatcher = new OCEFeedbackDispatcher();

        //Add the dispatcher as a listener
        feedbackManager.addPropertyChangeListener(oceFeedbackDispatcher);

        //Calculate feedback
        feedbackManager.registerUserConfiguration("MyLogFiles\\oldFile.xml", "MyLogFiles\\newFile.xml");


        //Print the list of connection
        System.out.println(" " + listConnections);

        MOICEProbe moiceProbe = new MOICEProbe();

        moiceProbe.run();
    }
}
