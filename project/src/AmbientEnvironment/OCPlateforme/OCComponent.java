/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.OCPlateforme;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class OCComponent implements Serializable {
    protected ArrayList<OCService> providedServices;
    protected ArrayList<OCService> requiredServices;

    public ArrayList<OCService> getProvidedServices() {
        return providedServices;
    }

    public ArrayList<OCService> getRequiredServices() {
        return requiredServices;
    }
}
