/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Main;

import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import MASInfrastructure.Infrastructure;
import Logger.MyLogger;
import Midlleware.AgentFactory.AgentFactory;
import Midlleware.AgentFactory.IAgentFactory;
import OCE.Medium.Medium;
import OCE.ServiceAgent;
import OCE.Unifieur.Composants.Matching;

public class Tests {

    public static void main(){

        Infrastructure infrastructure = new Infrastructure();
        IAgentFactory factoryAgent = new AgentFactory(infrastructure);

        MockupFacadeAdapter mockupFacadeAdapter = new MockupFacadeAdapter();

        Matching alwaysTrueMatching = new Matching();
        Medium medium = new Medium(infrastructure);
        // LogComp logger = new LogComp(); // Lot Log
        //Walid

        //Create and configure the logger service
        MyLogger logger = new MyLogger(); // Pas la peine d'instancier
        logger.init();

         // ServiceAgent myServiceAgent = factoryAgent.createServiceAgent();
    }
}
