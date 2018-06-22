/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupCompo;

import Environment.OCPlateforme.OCComponent;
import Environment.OCPlateforme.OCService;

import java.util.ArrayList;

public interface Container {
    public ArrayList<OCComponent> getComponents();

    public void addComponent(OCComponent c);

    public void removeComponent(OCComponent c);

    public void bind(OCService requiredService, OCService providedService);

    public void unbind(OCService requiredService, OCService providedService);
}
