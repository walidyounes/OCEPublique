/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.probe;

import AmbientEnvironment.FacadeAdapter.AcquisitionFailure;
import AmbientEnvironment.FacadeAdapter.IAcquisition;
import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MOICE.MOICE;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ComponentManager {

    private INotification serviceManager;
    private IAcquisition acquisition;

    public ComponentManager(INotification serviceManager, IAcquisition acquisition) {
        this.serviceManager = serviceManager;
        this.acquisition = acquisition;
    }

    /**
     * Get, store the list of appearing components
     * and send the list of their services to the ServiceManager
     * @throws AcquisitionFailure raised when there is a failure in the acquisition of the components from the environment
     */
    public void appearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> componentsList = acquisition.getNewComponents();

        // TODO walid 30-09-2019 : Delete later this is just for test
            ArrayList<MockupComponent> MOICEComponentsList = new ArrayList<>(componentsList.stream().map( e -> (MockupComponent) e).collect(Collectors.toList()));
            MOICE middlewareMOICE = MOICE.getInstance();
            MOICEComponentsList.forEach( c -> middlewareMOICE.registerComponent(c));
        // fin Todo

        for (OCComponent component : componentsList) {
            // log in the appearing components
            String log = String.format("Provided=%s Required=%s - Appearing", component.getProvidedServices(), component.getRequiredServices());
            OCELogger.log(Level.INFO, log);
            // Get both required and provided services for the component
            ArrayList<OCService> providedServices = component.getProvidedServices();
            ArrayList<OCService> requiredServices = component.getRequiredServices();
            //Log the services
            OCELogger.log(Level.INFO,"Provided Services = " + providedServices );
            OCELogger.log(Level.INFO,"Required Services = " + requiredServices );

            //Send the list of services to the ServiceManager for further processing
            if (providedServices != null) {
                serviceManager.appearingServices(providedServices);
            }
            if (requiredServices != null) {
                serviceManager.appearingServices(requiredServices);
            }
        }
    }

    /**
     * Fetch and store the disappearing components and send the list of there services to the serviceManager
     * @throws AcquisitionFailure raised when there is a failure in the acquisition of the components from the environment
     */
    public void disappearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> componentsList = acquisition.getDisappearedComponents();

        // TODO walid 06-11-2019 : Delete later this is just for test
        ArrayList<MockupComponent> MOICEComponentsList = new ArrayList<>(componentsList.stream().map( e -> (MockupComponent) e).collect(Collectors.toList()));
        MOICE middlewareMOICE = MOICE.getInstance();
        MOICEComponentsList.forEach( c -> middlewareMOICE.unRegisterComponent(c));

        // fin Todo

        // System.out.println(" Disappearing components = " + componentsList + "size = " + componentsList.size());
        for (OCComponent component : componentsList) {
            // Log in the disappearance of the component
            String log = String.format("Provided = %s Required = %s - Disappearing", component.getProvidedServices(), component.getRequiredServices());
            OCELogger.log(Level.INFO, log);

            ArrayList<OCService> providedServices = component.getProvidedServices();
            ArrayList<OCService> requiredServices = component.getRequiredServices();

            System.out.println("provided services = " + providedServices);
            System.out.println("required services = " + requiredServices);
            System.out.println("Notify serviceManager =" + serviceManager);

            if (providedServices != null) {
                serviceManager.disappearingServices(providedServices);
            }

            if (requiredServices != null) {
                serviceManager.disappearingServices(requiredServices);
            }
        }
    }

}
