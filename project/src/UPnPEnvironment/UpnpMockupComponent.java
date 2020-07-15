/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UPnPEnvironment;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;

public class UpnpMockupComponent extends MockupComponent {

    private String udn;

    public UpnpMockupComponent(String name, ArrayList<OCService> providedServices, ArrayList<OCService> requiredServices, String udn) {
        super(name, providedServices, requiredServices);
        this.udn = udn;
    }

    public String getUdn() {
        return udn;
    }
}
