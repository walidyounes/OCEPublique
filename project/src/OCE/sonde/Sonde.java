/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;


import Environment.FacadeAdapter.AcquisitionFailure;
import Environment.FacadeAdapter.IAcquisition;
import Midlleware.AgentFactory.IAFactory;
import OCE.Medium.services.IEnregistrement;

import java.util.Timer;
import java.util.TimerTask;

//ToDo 1 verifier l'imp de thread class Sonde implements runnable?
//ToDo 2 Revoir la construction du composant Sonde
public class Sonde {

    // Composants de Sonde
    private ComponentManager componentsManager;
    private long periodicity; // the periodicity of the souding of the environement

    // Services requis par la Sonde
    private IAcquisition acquisition;
    private IEnregistrement enregistrement;
    private IAFactory fabriqueMAgent;

    public Sonde(IAcquisition acquisition, IEnregistrement enregistrement, IAFactory agentFactory, long periodicity) {
        // super(); // Todo : walid : Pourquoi appeler le constructeur de la classe mère alors qu'il n'ya pas un extend !!!!
        this.periodicity = periodicity;
        this.enregistrement = enregistrement;
        this.acquisition = acquisition;
        this.fabriqueMAgent = agentFactory;
        ServiceManager serviceManager = new ServiceManager(agentFactory, enregistrement);
        componentsManager = new ComponentManager(serviceManager, acquisition);

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

    public void run() {
        t.start();
    }
}
