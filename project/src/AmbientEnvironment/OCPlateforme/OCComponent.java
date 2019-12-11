/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.OCPlateforme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OCComponent implements Serializable {
    protected ArrayList<OCService> providedServices;
    protected ArrayList<OCService> requiredServices;

    public ArrayList<OCService> getProvidedServices() {
        return providedServices;
    }

    public ArrayList<OCService> getRequiredServices() {
        return requiredServices;
    }

    public ArrayList<OCService> getAllServices(){
        List<OCService> allServicesList = Stream.of(providedServices, requiredServices)
                                                .flatMap(x -> x.stream())
                                                .collect(Collectors.toList());
        return new ArrayList<>(allServicesList);
    }
}
