/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupFacadeAdapter;

import Environment.FacadeAdapter.AcquisitionFailure;
import Environment.FacadeAdapter.IAcquisition;
import Environment.FacadeAdapter.IAddRemove;
import Environment.FacadeAdapter.IBinding;
import Environment.MockupCompo.MockupContainer;
import Environment.MockupCompo.MockupService;
import Environment.MockupCompo.Way;
import Environment.OCPlateforme.OCComponent;
import Environment.OCPlateforme.OCService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MockupFacadeAdapter extends MockupContainer implements IBinding, IAcquisition, IAddRemove {

    // protected ArrayList<OCComponent> componentList = new ArrayList<OCComponent>();
    protected ArrayList<OCService> serviceList = new ArrayList<>();
    protected Set<OCComponent> appearedComponentSet = new HashSet<>();
    protected Set<OCComponent> disappearedComponentSet = new HashSet<>();
    protected Set<OCComponent> existingComponentSet = new HashSet<>();
    protected Set<OCService> appearedServiceSet = new HashSet<>();
    protected Set<OCService> disappearedServiceSet = new HashSet<>();
    protected Set<OCService> existingServiceSet = new HashSet<>();

    /**
     *
     */
    public Set<OCComponent> getNewComponents() throws AcquisitionFailure {
        appearedComponentSet.removeAll(disappearedComponentSet); // pour les apparitions éphémères....
        Set<OCComponent> result = new HashSet<>();
        result.addAll(appearedComponentSet);
        appearedComponentSet.clear(); // clear à chaque appel
        return result;
    }

    public Set<OCComponent> getDisappearedComponents() throws AcquisitionFailure {
        Set<OCComponent> result = new HashSet<>();
        result.addAll(disappearedComponentSet);
        disappearedComponentSet.clear(); // clear à chaque appel
        return result;
    }

    public Set<OCComponent> getCurrentComponents() throws AcquisitionFailure {
        return existingComponentSet;
    }

    public Set<OCService> getNewServices() throws AcquisitionFailure {
        appearedServiceSet.removeAll(disappearedServiceSet); // pour les apparitions éphémères....
        Set<OCService> result = new HashSet<>();
        result.addAll(appearedServiceSet);
        appearedServiceSet.clear(); // clear à chaque appel
        return result;
    }

    public Set<OCService> getDisappearedServices() throws AcquisitionFailure {
        Set<OCService> result = new HashSet<>();
        result.addAll(disappearedServiceSet);
        disappearedServiceSet.clear(); // clear à chaque appel
        return result;
    }

    public Set<OCService> getCurrentServices() throws AcquisitionFailure {
        return existingServiceSet;
    }

    public void bind(OCService Service1, OCService Service2) {
        int index1 = serviceList.indexOf(Service1);
        MockupService service1 = (MockupService) serviceList.get(index1);
        int index2 = serviceList.indexOf(Service2);
        MockupService service2 = (MockupService) serviceList.get(index2);
        if ((service1.getWay() == Way.REQUIRED) && (service2.getWay() == Way.PROVIDED)) {
            super.bind(service1, service2);
        } else if ((service2.getWay() == Way.REQUIRED) && (service1.getWay() == Way.PROVIDED)) {
            super.bind(service2, service1);
        }
    }

    public void unbind(OCService Service1, OCService Service2) {

        int index1 = serviceList.indexOf(Service1);
        MockupService service1 = (MockupService) serviceList.get(index1);
        int index2 = serviceList.indexOf(Service2);
        MockupService service2 = (MockupService) serviceList.get(index2);
        if ((service1.getWay() == Way.REQUIRED) && (service2.getWay() == Way.PROVIDED)) {
            super.unbind(service1, service2);
        } else if ((service2.getWay() == Way.REQUIRED) && (service1.getWay() == Way.PROVIDED)) {
            super.unbind(service2, service1);
        }
    }

    /**
     * Modified by Walid
     */
    public void addComponent(OCComponent component) {
        super.addComponent(component);
        // Composants : enregistrer l'association puis l'apparition
        // componentList.add(component);
        appearedComponentSet.add(component);
        existingComponentSet.add(component);

        // Services : enregistrer les associations puis les apparitions

		/* for (int i = 0; i < component.getProvidedServices().size(); i++) {
            serviceList.add(component.getProvidedServices().get(i));
		} */

        serviceList.addAll(component.getProvidedServices()); // Code Walid
        serviceList.addAll(component.getRequiredServices()); // Code Walid

		/* for (int i = 0; i < component.getRequiredServices().size(); i++) {
			serviceList.add(component.getRequiredServices().get(i));
		} */

        //code Walid
        appearedServiceSet.addAll(serviceList);
        existingServiceSet.addAll(serviceList);

        //appearedServiceSet.addAll(component.getProvidedServices());
        //existingServiceSet.addAll(component.getProvidedServices());
        // appearedServiceSet.addAll(component.getRequiredServices());
        // existingServiceSet.addAll(component.getRequiredServices());
    }

    public void removeComponent(OCComponent component) {
		/* super.removeComponent(component);
		int index;
		index = componentList.indexOf(component);
		disappearedComponentSet.add(componentList.get(index));
		existingComponentSet.remove(componentList.get(index));
		componentList.remove(index);
		*/
        // Code Walid
        disappearedComponentSet.add(component);
        existingComponentSet.remove(component);
        super.removeComponent(component);

        disappearedServiceSet.removeAll(component.getProvidedServices());
        existingServiceSet.removeAll(component.getProvidedServices());
        serviceList.removeAll(component.getProvidedServices());

        disappearedServiceSet.removeAll(component.getRequiredServices());
        existingServiceSet.removeAll(component.getRequiredServices());
        serviceList.removeAll(component.getRequiredServices());

		/* for (int i = 0; i < component.getProvidedServices().size(); i++) {
			index = serviceList.indexOf(component.getProvidedServices().get(i));
			disappearedServiceSet.add(serviceList.get(index));
			existingServiceSet.remove(serviceList.get(index));
			serviceList.remove(index);
		} */
		/* for (int i = 0; i < component.getRequiredServices().size(); i++) {
			index = serviceList.indexOf(component.getRequiredServices().get(i));
			disappearedServiceSet.add(serviceList.get(index));
			existingServiceSet.remove(serviceList.get(index));
			serviceList.remove(index);
		}*/
    }
}
