
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;
import java.util.HashMap;

public class MockupComponent extends OCComponent {
    protected String name;

    public MockupComponent(String name, ArrayList<OCService> providedServices, ArrayList<OCService> requiredServices) {
        this.name = name;
        this.providedServices = new ArrayList<>(providedServices);
        this.requiredServices = new ArrayList<>(requiredServices);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Component " + name + " Provides " + providedServices + " & Requires " + requiredServices + "]";

    }

    /**
     *  Compare two MockupComponent (the comparison is compute on the combination "Name")
     * @param o the object to compare this to
     * @return true if the two object are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;

        MockupComponent that = (MockupComponent) o;

        return this.name.equals(that.getName());
    }


    /**
     * Get the hashCode of this component
     * @return the hashCode computed
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
