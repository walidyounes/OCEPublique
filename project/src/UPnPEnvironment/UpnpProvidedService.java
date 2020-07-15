/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UPnPEnvironment;

import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.MockupCompo.Way;

public class UpnpProvidedService extends SingleLinkMockupService {

    private String udn;

    public UpnpProvidedService(String name, String matchingID, String owner, String udn) {
        super(name, matchingID, owner, Way.PROVIDED);
        this.udn = udn;
    }

    public String getUdn(){
        return udn;
    }
}
