/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;


import AmbientEnvironment.FacadeAdapter.AcquisitionFailure;
import AmbientEnvironment.FacadeAdapter.IAcquisition;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Medium.Recorder.IRecord;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is used to probe the environment for new components that appear and components that have disappeared
 * @author Walid YOUNES
 * @version 1.0
 */
public class Sonde {

    private ComponentManager componentsManager;
    private long periodicity; // the periodicity of the task of souding the environement


    public Sonde(IAcquisition acquisition, IRecord agentRecorder, IOCEServiceAgentFactory agentFactory, long periodicity) {
        this.periodicity = periodicity;
        // Instanciation of the serviceManager
        ServiceManager serviceManager = new ServiceManager(agentFactory, agentRecorder);
        //Instanciation of the ccomponentManager
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
     * This thread call the ScheduledSounding task periodicly to probe the ambient environement for new appearing and disapearing services
     */
    private Thread t = new Thread() {
        @Override
        public void run() {
            Timer time = new Timer(); // Instantiate Timer Object
            // Instantiate SheduledTask class
            ScheduledSounding st = new ScheduledSounding();
            // Create Repetitively task for every "Periodicity" secs with a delay 0 secs between two executions
            time.schedule(st, 0, periodicity);
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
     * Run the thread responsible of sounding the environement
     */
    public void run() {
        t.start();
    }
}
