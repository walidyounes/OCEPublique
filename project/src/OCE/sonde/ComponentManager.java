/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import AmbientEnvironment.FacadeAdapter.AcquisitionFailure;
import AmbientEnvironment.FacadeAdapter.IAcquisition;
import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MOICE.MOICE;
import MOICE.connectionManager.ConnectionManager;
import MOICE.deploymentManager.DeploymentManager;
import MOICE.feedbackManager.FeedbackManager;

import java.util.ArrayList;
import java.util.List;
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
     * and send the list of the services of these components to the ServiceManager
     * @throws AcquisitionFailure raised when there is a failure in the acquisition of the components from the environment
     */
    public void appearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> componentsList = acquisition.getNewComponents();

        // TODO walid 30-09-2019 : Delete later this is just for test

            ArrayList<MockupComponent> MOICEComponentsList = new ArrayList<>(componentsList.stream().map( e -> (MockupComponent) e).collect(Collectors.toList()));
            MOICE middlewareMOICE = MOICE.getInstance();
            MOICEComponentsList.forEach( c -> middlewareMOICE.registerComponent(c));

        for (OCComponent component : componentsList) {

            // log in the appearing components
            String log = String.format("Provided=%s Required=%s - Appearing", component.getProvidedServices(), component.getRequiredServices());
            MyLogger.log(Level.INFO, log);
            // Get both required and provided services for the component
            ArrayList<OCService> providedServices = component.getProvidedServices();
            ArrayList<OCService> requiredServices = component.getRequiredServices();
            //Log the services
            MyLogger.log(Level.INFO,"Provided Services = " + providedServices );
            MyLogger.log(Level.INFO,"Required Services = " + requiredServices );

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
     *
     * Fetch and store the appearing components and send the list of there services to the serviceManager
     * @throws AcquisitionFailure raised when there is a failure in the acquisition of the components from the environement
     */
    public void disappearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> componentsList = acquisition.getDisappearedComponents();
        System.out.println(" Disappearing components = " + componentsList + "size = " + componentsList.size());
        for (OCComponent component : componentsList) {

            // Log in the disappearing of the component
            String log = String.format("Provided = %s Required = %s - Disappearing", component.getProvidedServices(), component.getRequiredServices());
            MyLogger.log(Level.INFO, log);

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
