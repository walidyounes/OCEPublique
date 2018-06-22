
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupCompo;

import Environment.OCPlateforme.OCComponent;
import Environment.OCPlateforme.OCService;

import java.util.ArrayList;

public class MockupComponent extends OCComponent {
    protected String name;

    public MockupComponent(String name, ArrayList<OCService> providedservices, ArrayList<OCService> requiredServices) {
        this.name = name;
        this.providedServices = providedservices;
        this.requiredServices = requiredServices;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Component " + name + " Provides " + providedServices + " & Requires " + requiredServices + "]";

    }
}
