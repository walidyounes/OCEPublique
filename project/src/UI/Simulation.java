/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import Logger.MyLogger;
import MASInfrastructure.Infrastructure;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import Midlleware.AgentFactory.OCEServiceAgentFactory;
import OCE.Medium.Medium;
import OCE.sonde.Probe;

import java.util.logging.Level;

public class Simulation implements Runnable {
    private MockupFacadeAdapter mockupFacadeAdapter;
    private Infrastructure infrastructure;

    public Simulation(MockupFacadeAdapter mockupFacadeAdapter) {
        this.infrastructure = new Infrastructure();
        this.mockupFacadeAdapter = mockupFacadeAdapter;
    }

    public Simulation(MockupFacadeAdapter mockupFacadeAdapter, Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
        this.mockupFacadeAdapter = mockupFacadeAdapter;
    }

    @Override
    public void run() {
        //Initialize the medium component
        Medium medium = new Medium(infrastructure);
        //Create the agent Factory
        IOCEServiceAgentFactory agentFactory = new OCEServiceAgentFactory(infrastructure, medium);
        // Create the Probe component to probe the environment
        Probe probe = new Probe(mockupFacadeAdapter,medium, agentFactory, 1000);

        // probe.run(); // deleted for test 16/10/2019

        MyLogger.log(Level.INFO, "");
        pause(3000);
        //Start the scheduling process
        infrastructure.startScheduling();

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
