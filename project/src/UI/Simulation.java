/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import Logger.MyLogger;
import MASInfrastructure.Infrastructure;
import Midlleware.AgentFactory.IOCEAgentFactory;
import Midlleware.AgentFactory.OCEAgentFactory;
import OCE.Medium.Medium;
import OCE.Unifieur.Matching;
import OCE.sonde.Sonde;

public class Simulation implements Runnable {
    private MockupFacadeAdapter mockupFacadeAdapter;

    public Simulation(MockupFacadeAdapter mockupFacadeAdapter) {
        this.mockupFacadeAdapter = mockupFacadeAdapter;
    }

    @Override
    public void run() {
        Infrastructure infrastructure = new Infrastructure();

        Matching alwaysTrueMatching = new Matching();
        Medium medium = new Medium(infrastructure);

        IOCEAgentFactory agentFactory = new OCEAgentFactory(infrastructure, medium);


        Sonde sonde = new Sonde(mockupFacadeAdapter,medium, agentFactory, 1000);
        sonde.run();

        pause(5000);
        infrastructure.ordonnancer();

        MyLogger.close();
    }



    private void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
