/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import Logger.OCELogger;
import MASInfrastructure.Infrastructure;
import MOICE.MOICE;
import MOICE.feedbackManager.FeedbackManager;
import Midlleware.AgentFactory.IOCEBinderAgentFactory;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import Midlleware.AgentFactory.OCEBinderAgentFactory;
import Midlleware.AgentFactory.OCEServiceAgentFactory;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.FeedbackDispatcher.OCEFeedbackDispatcher;
import OCE.Medium.Medium;
import OCE.probe.Probe;
import javafx.collections.ObservableList;

public class OCE implements Runnable{
    private MockupFacadeAdapter mockupFacadeAdapter;
    private Infrastructure infrastructure;
    private Probe probe;
    private Medium medium;
    private IOCEServiceAgentFactory serviceAgentFactory;
    private OCEFeedbackDispatcher oceFeedbackDispatcher;

    public OCE(MockupFacadeAdapter mockupFacadeAdapter, Infrastructure infrastructure) {
        this.mockupFacadeAdapter = mockupFacadeAdapter;
        this.infrastructure = infrastructure;
        //Initialize the medium component
        this.medium = new Medium(infrastructure);
        //Add the infrastructure as a listener for special event in the medium "like for example deleting agents'
        this.medium.addPropertyChangeListener(infrastructure);
        //Create the agent Factory
        this.serviceAgentFactory = new OCEServiceAgentFactory(infrastructure, medium);
        // Create the Probe component to probe the environment
        this.probe = new Probe(mockupFacadeAdapter,medium, this.serviceAgentFactory, 1000);
        //Create the instance to the component used to collect user feedback and dispatch them to the corresponding agent
        this.oceFeedbackDispatcher = OCEFeedbackDispatcher.getInstance();
        //Set the reference to the component used to transit messages to the agents
        this.oceFeedbackDispatcher.setCommunicationAdapter(this.medium);
        //Set the reference to the component used for reference resolving
        this.oceFeedbackDispatcher.setOceRecord(this.medium);
        //Set the reference to the component used for creating Binder agents
        IOCEBinderAgentFactory feedbackDispatcherBinderAgentFactory = new OCEBinderAgentFactory(this.infrastructure, this.medium);
        this.oceFeedbackDispatcher.setBinderAgentFactory(feedbackDispatcherBinderAgentFactory);
        //Set the reference of the MAS infrastructure
        this.oceFeedbackDispatcher.setInfrastructure(infrastructure);
        //Add the oceFeedbackDispatcher component as a listener in MOICE's Feedback manager
        MOICE.getInstance().addFeedbackComputedListener(this.oceFeedbackDispatcher);
        //Add MOICE's feedbackManager as a listener in MOICEProbe component
        MOICE.getInstance().getProbeFileStorage().addPropertyChangeListener((FeedbackManager)MOICE.getInstance().getMyFeedbackManager());
    }

    /**
     * Lunch the OCE engine
     */
    @Override
    public void run() {
        //Probe the environment for appearing or disappearing of components and services
        this.probe.probeEnvironment();
        pause(2000);
        //Start the scheduling process of the agents in the MAS infrastructure
        infrastructure.startScheduling();
        OCELogger.close();
    }

    /**
     * Get the list of all agents recorded in the system (ServiceAgent / Binder agent)
     * @return : the list of all the agents of the system
     */
    public ObservableList<OCEAgent> gteAllAgents(){
        synchronized (this){
            return this.medium.getAllAgents();
        }
    }

    /**
     * Launch an engine cycle
     */
    public void oneStepExecution(){
        //Probe the environment for appearing or disappearing of components and services
        this.probe.probeEnvironment();
        //Pause for 1s
        pause(1000);

        //Restart the scheduling of the agents in the infrastructure
        this.infrastructure.resetCurrentCycleAgent();

        //Inform the agent of the begining of the engine cycle
        this.medium.getAllAgents().stream().filter(agent -> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).activateStartingNewEngineCycle());
    }

    private void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public Infrastructure getInfrastructure() {
        return infrastructure;
    }

    public Medium getMedium() {
        return medium;
    }
}