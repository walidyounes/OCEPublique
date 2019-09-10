
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCService;

public class MultiLinkMockupService extends MockupService {

    public MultiLinkMockupService(String name, String type, String owner, Way myWay) {
        super(name, type, owner, myWay);
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

}
