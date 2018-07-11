/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;

public interface Container {
     ArrayList<OCComponent> getComponents();

     void addComponent(OCComponent c);

     void removeComponent(OCComponent c);

     void bind(OCService requiredService, OCService providedService);

     void unbind(OCService requiredService, OCService providedService);
}
