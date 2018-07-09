/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Main;

import Environment.MockupFacadeAdapter.MockupFacadeAdapter;
import Infrastructure.Infrastructure;
import Logger.MyLogger;
import Midlleware.AgentFactory.AFactory;
import Midlleware.AgentFactory.IAFactory;
import OCE.ServiceAgent;

import javax.xml.ws.Service;

public class Tests {

    public static void main(){

        Infrastructure infrastructure = new Infrastructure();
        IAFactory factoryAgent = new AFactory(infrastructure);

        MockupFacadeAdapter mockupFacadeAdapter = new MockupFacadeAdapter();

        Matching alwaysTrueMatching = new Matching();
        Medium medium = new Medium(infrastructure, alwaysTrueMatching);
        // LogComp logger = new LogComp(); // Lot Log
        //Walid

        //Create and configure the logger service
        MyLogger logger = new MyLogger(); // Pas la peine d'instancier
        logger.init();

        ServiceAgent myServiceAgent = factoryAgent.createServiceAgent();
    }
}
