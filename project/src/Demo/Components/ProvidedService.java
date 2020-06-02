/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components;

import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.MockupCompo.Way;

public class ProvidedService extends SingleLinkMockupService {

    private Object implementation;

    public ProvidedService(String name, String matchingID, String owner, Object implementation) {
        super(name, matchingID, owner, Way.PROVIDED);
        this.implementation = implementation;
    }

    public Object getImplementation() {
        return implementation;
    }

    public void setImplementation(Object implementation) {
        this.implementation = implementation;
    }
}
