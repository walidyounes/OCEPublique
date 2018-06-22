
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupCompo;

import Environment.OCPlateforme.OCService;

public class MultiLinkMockupService extends MockupService {

    public MultiLinkMockupService(String name, String owner, Way myWay) {
        super(name, owner, myWay);
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
