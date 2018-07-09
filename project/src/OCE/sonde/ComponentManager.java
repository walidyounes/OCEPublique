/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import Environment.FacadeAdapter.AcquisitionFailure;
import Environment.FacadeAdapter.IAcquisition;
import Environment.OCPlateforme.OCComponent;
import Environment.OCPlateforme.OCService;
import Logger.MyLogger;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;

public class ComponentManager {

    private INotification serviceManager;
    private IAcquisition acquisition;

    public ComponentManager(INotification serviceManager, IAcquisition acquisition) {
        this.serviceManager = serviceManager;
        this.acquisition = acquisition;
    }

    /**
     * Get, store the list of appearing componentns
     * and send the list of the services of these components to the ServiceManager
     * @throws AcquisitionFailure
     */
    public void appearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> componentsList = acquisition.getNewComponents();
        for (OCComponent component : componentsList) {

            // logg the appearing components
            String log = String.format("Provided=%s Required=%s - Apparition", component.getProvidedServices(), component.getRequiredServices());
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
     * R�cuperer et Sauvgarder les composants disparus Envoyer la listes de ces
     * services � ServiceManager
     *
     * @throws AcquisitionFailure
     */
    public void disappearingComponentsAcquisition() throws AcquisitionFailure {

        Set<OCComponent> listComposants = acquisition.getDisappearedComponents();
        //System.out.println("listComposants = " + listComposants);
        for (OCComponent composant : listComposants) {

            // Logger L'apparition des composants
            String log = String.format("Provided=%s Required=%s - Dispparition", composant.getProvidedServices(), composant.getRequiredServices());
            MyLogger.log(Level.INFO, log);

            ArrayList<OCService> servicesFournis = composant.getProvidedServices();
            ArrayList<OCService> servicesRequis = composant.getRequiredServices();

            System.out.println("servicesFournis = " + servicesFournis);
            System.out.println("servicesRequis = " + servicesRequis);
            System.out.println("Notif  =" + serviceManager);

            if (servicesFournis != null) {
                serviceManager.disappearingServices(servicesFournis);
            }

            if (servicesRequis != null) {
                serviceManager.disappearingServices(servicesRequis);
            }
        }
    }

}
