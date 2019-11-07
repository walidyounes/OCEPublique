/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.probe;


import AmbientEnvironment.FacadeAdapter.AcquisitionFailure;
import AmbientEnvironment.FacadeAdapter.IAcquisition;
import Logger.OCELogger;
import MOICE.MOICE;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Medium.Recorder.IRecord;

import java.util.logging.Level;


/**
 * This class is used to probe the environment for new components that appear and components that have disappeared
 * @author Walid YOUNES
 * @version 1.0
 */
public class Probe {

    private ComponentManager componentsManager;
    private long periodicity; // the periodicity of the task of sensing the environment
    private boolean probe; // Boolean value to control the probing mechanism of the environment

    public Probe(IAcquisition acquisition, IRecord agentRecorder, IOCEServiceAgentFactory agentFactory, long periodicity) {
        this.periodicity = periodicity;
        // Instantiation of the serviceManager
        ServiceManager serviceManager = new ServiceManager(agentFactory, agentRecorder);
        //Instantiation of the componentManager
        this.componentsManager = new ComponentManager(serviceManager, acquisition);
        this.probe = true;
    }

//    class ScheduledSounding extends TimerTask {
//
//        @Override
//        public void run() {
//            try {
//                componentsManager.appearingComponentsAcquisition();
//                componentsManager.disappearingComponentsAcquisition();
//
//            } catch (AcquisitionFailure acquisitionFailure) {
//                acquisitionFailure.printStackTrace();
//            }
//        }
//    }

    /**
     * Probe the environment and detect the appearing and disappearing services and components
     */
    public void probeEnvironment(){

            synchronized (this) {
                try {
                    OCELogger.log(Level.INFO, "************************ Probing *******************");
                    componentsManager.appearingComponentsAcquisition();
                    componentsManager.disappearingComponentsAcquisition();
                    //Todo Walid  06/11/2019 : To delete, this is just for test in the Midlleware MOICE
                    MOICE.getInstance().collectOCEProposedConfiguration();
                } catch (AcquisitionFailure acquisitionFailure) {
                    acquisitionFailure.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
    }


//    /**
//     * This thread call the ScheduledSounding task periodically to probe the ambient environment for new appearing and disappearing components and their services
//     */
//    private Thread t = new Thread() {
//        @Override
//        public void run() {
//
//            probeEnvironment();
//        }
//    };
//
//    /**
//     * Change the value of the periodicity of the sounding
//     * @param periodicity : the new periodicity value
//     */
//    public void setPeriodicity(long periodicity) {
//        this.periodicity = periodicity;
//    }
//
//    /**
//     * Run the thread responsible of probing the environment
//     */
//    public void run() {
//        t.start();
//    }
}
