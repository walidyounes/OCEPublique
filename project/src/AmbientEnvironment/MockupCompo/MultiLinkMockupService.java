
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.CrowdednessLevel;
import AmbientEnvironment.OCPlateforme.OCService;

public class MultiLinkMockupService extends MockupService {

    public MultiLinkMockupService(String name, String matchingID, String owner, Way myWay) {
        super(name, matchingID, owner, myWay);
    }
    public MultiLinkMockupService(String name, String matchingID, String owner, Way myWay, String crowdedness) {
        super(name, matchingID, owner, myWay, crowdedness);
    }
    public void addLink(OCService s) throws AddLinkException {
        linkedServices.add(s);
    }

    public void removeLink(OCService s) throws RemoveLinkException {
        if (linkedServices.contains(s)) {
            linkedServices.remove(s);
        } else {
            throw new RemoveLinkException();
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }
}
