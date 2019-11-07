/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Infrastructure;
import Midlleware.AgentFactory.OCEServiceAgentFactory;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Medium.Medium;
import OCE.Unifieur.Matching;
import OCE.probe.Probe;

import java.util.ArrayList;

public class Tests {

    public static void main(String[] args){

        MockupFacadeAdapter mockupFacadeAdapter = new MockupFacadeAdapter();
        Infrastructure infrastructure = new Infrastructure();

        //Create and configure the logger service
        OCELogger logger = new OCELogger(); // Pas la peine d'instancier
        OCELogger.init();

        Matching alwaysTrueMatching = new Matching();
        Medium medium = new Medium(infrastructure);

        IOCEServiceAgentFactory agentFactory = new OCEServiceAgentFactory(infrastructure, medium);


        Probe probe = new Probe(mockupFacadeAdapter,medium, agentFactory, 1000);
        // probe.run(); //deleted for test 16/10/2019

        // Construction du composant "C1"
        ArrayList<OCService> providedByC1 = new ArrayList<OCService>();
        MockupService JdeC1 = new MultiLinkMockupService("J","J" ,"C1", Way.PROVIDED);
        providedByC1.add(JdeC1);
        ArrayList<OCService> requiredByC1 = new ArrayList<OCService>();
        MockupComponent C1 = new MockupComponent("C1", providedByC1, requiredByC1);
        // Ajout de C1 dans le container
        mockupFacadeAdapter.addComponent(C1);

        // Construction du composant "C2"
        ArrayList<OCService> requiredByC2 = new ArrayList<OCService>();
        MockupService JdeC2 = new MultiLinkMockupService("J", "J","C2", Way.REQUIRED);
        requiredByC2.add(JdeC2);
        ArrayList<OCService> providedByC2 = new ArrayList<OCService>();
        MockupComponent C2 = new MockupComponent("C2", providedByC2, requiredByC2);
        // Ajout de C2 dans le container
        mockupFacadeAdapter.addComponent(C2);

        pause(5000);
        infrastructure.startScheduling();

        OCELogger.close();
    }
    public static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
