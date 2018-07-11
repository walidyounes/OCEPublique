/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;


import AmbientEnvironment.OCPlateforme.OCService;

public class SingleLinkMockupService extends MockupService {

    public SingleLinkMockupService(String name, String owner, Way myWay) {
        super(name, owner, myWay);
    }

    public void addLink(OCService s) throws AddLinkException {
        if (linkedServices.isEmpty()) {
            linkedServices.add(s);
        } else {
            throw new AddLinkException();
        }
    }

    public void removeLink(OCService s) throws RemoveLinkException {
        if (linkedServices.contains(s)) {
            linkedServices.remove(s);
        } else {
            throw new RemoveLinkException();
        }
    }

}

