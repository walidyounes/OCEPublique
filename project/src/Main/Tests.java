/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Main;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Infrastructure;
import Logger.MyLogger;
import Midlleware.AgentFactory.AgentFactory;
import Midlleware.AgentFactory.IAgentFactory;
import OCE.Medium.Medium;
import OCE.Unifieur.Matching;
import OCE.sonde.Sonde;

import java.util.ArrayList;

public class Tests {

    public static void main(String[] args){

        MockupFacadeAdapter mockupFacadeAdapter = new MockupFacadeAdapter();
        Infrastructure infrastructure = new Infrastructure();

        //Create and configure the logger service
        MyLogger logger = new MyLogger(); // Pas la peine d'instancier
        logger.init();

        IAgentFactory agentFactory = new AgentFactory(infrastructure);
        Matching alwaysTrueMatching = new Matching();
        Medium medium = new Medium(infrastructure);

        Sonde sonde = new Sonde(mockupFacadeAdapter,medium, agentFactory, 2000);
        sonde.run();

        // Construction du composant "C1"
        ArrayList<OCService> providedByC1 = new ArrayList<OCService>();
        MockupService JdeC1 = new MultiLinkMockupService("J", "C1", Way.PROVIDED);
        providedByC1.add(JdeC1);
        ArrayList<OCService> requiredByC1 = new ArrayList<OCService>();
        MockupComponent C1 = new MockupComponent("C1", providedByC1, requiredByC1);
        // Ajout de C1 dans le container
        mockupFacadeAdapter.addComponent(C1);

        // Construction du composant "C2"
        ArrayList<OCService> requiredByC2 = new ArrayList<OCService>();
        MockupService JdeC2 = new MultiLinkMockupService("J", "C2", Way.REQUIRED);
        requiredByC2.add(JdeC2);
        ArrayList<OCService> providedByC2 = new ArrayList<OCService>();
        MockupComponent C2 = new MockupComponent("C2", providedByC2, requiredByC2);
        // Ajout de C2 dans le container
        mockupFacadeAdapter.addComponent(C2);
        infrastructure.ordonnancer();

        logger.close();
    }
}
