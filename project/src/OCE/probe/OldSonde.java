/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.probe;

import AmbientEnvironment.FacadeAdapter.AcquisitionFailure;
import AmbientEnvironment.FacadeAdapter.IAcquisition;
import Logger.OCELogger;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Medium.Recorder.IRecord;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 * This class is used to probe the environment for new components that appear and components that have disappeared
 * @author Walid YOUNES
 * @version 1.0
 */
public class OldSonde {
    private ComponentManager componentsManager;
    private long periodicity; // the periodicity of the task of sensing the environment


    public OldSonde(IAcquisition acquisition, IRecord agentRecorder, IOCEServiceAgentFactory agentFactory, long periodicity) {
        this.periodicity = periodicity;
        // Instantiation of the serviceManager
        ServiceManager serviceManager = new ServiceManager(agentFactory, agentRecorder);
        //Instantiation of the componentManager
        this.componentsManager = new ComponentManager(serviceManager, acquisition);
    }

    class ScheduledSounding extends TimerTask {

        @Override
        public void run() {
            try {
                componentsManager.appearingComponentsAcquisition();
                componentsManager.disappearingComponentsAcquisition();

            } catch (AcquisitionFailure acquisitionFailure) {
                acquisitionFailure.printStackTrace();
            }
        }
    }
    /**
     * This thread call the ScheduledSounding task periodically to probe the ambient environment for new appearing and disappearing components and their services
     */
    private Thread t = new Thread() {
        @Override
        public void run() {
            Timer time = new Timer(); // Instantiate Timer Object
            // Instantiate ScheduledTask class
            OldSonde.ScheduledSounding st = new OldSonde.ScheduledSounding();
            // Create Repetitively task for every "Periodicity" secs with a delay 0 secs between two executions
            time.schedule(st, 0, periodicity);
            OCELogger.log(Level.INFO, "*************************** Probing *******************");
        }
    };

    /**
     * Change the value of the periodicity of the sounding
     * @param periodicity : the new periodicity value
     */
    public void setPeriodicity(long periodicity) {
        this.periodicity = periodicity;
    }

    /**
     * Run the thread responsible of probing the environment
     */
    public void run() {
        t.start();
    }
}
