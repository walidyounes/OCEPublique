/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import Logger.MyLogger;
import MASInfrastructure.Infrastructure;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import Midlleware.AgentFactory.OCEServiceAgentFactory;
import OCE.Agents.OCEAgent;
import OCE.Medium.Medium;
import OCE.sonde.Probe;
import javafx.collections.ObservableList;

import java.util.Set;

public class OCE implements Runnable{
    private MockupFacadeAdapter mockupFacadeAdapter;
    private Infrastructure infrastructure;
    private Probe probe;
    private Medium medium;
    private IOCEServiceAgentFactory serviceAgentFactory;


    public OCE(MockupFacadeAdapter mockupFacadeAdapter, Infrastructure infrastructure) {
        this.mockupFacadeAdapter = mockupFacadeAdapter;
        this.infrastructure = infrastructure;
        //Initialize the medium component
        this.medium = new Medium(infrastructure);
        //Create the agent Factory
        this.serviceAgentFactory = new OCEServiceAgentFactory(infrastructure, medium);
        // Create the Probe component to probe the environment
        this.probe = new Probe(mockupFacadeAdapter,medium, this.serviceAgentFactory, 1000);
    }

    @Override
    public void run() {
        //launch the probing of the environment
        this.probe.probeEnvironment();
        pause(2000);
        //Start the scheduling process
        infrastructure.startScheduling();
        MyLogger.close();
    }

    public ObservableList<OCEAgent> gteAllAgents(){
        synchronized (this){
            return this.medium.getAllAgents();
        }
    }

    public void oneStepExecution(){
        //Restart probing the environment
        this.probe.probeEnvironment();
        //Pause for 1s
        pause(1000);

        //Restart the scheduling in the infrastructure
        this.infrastructure.resetCurrentCycleAgent();
    }

    private void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
