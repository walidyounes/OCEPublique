/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;


import AmbientEnvironment.OCPlateforme.CrowdednessLevel;
import AmbientEnvironment.OCPlateforme.OCService;

public class SingleLinkMockupService extends MockupService {

    public SingleLinkMockupService(String name, String matchingID, String owner, Way myWay, String type) {
        super(name, matchingID, owner, myWay, type);
    }
    public SingleLinkMockupService(String name, String matchingID, String owner, Way myWay, CrowdednessLevel crowdedness) {
        super(name, matchingID, owner, myWay, crowdedness);
    }

    public SingleLinkMockupService(String name, String matchingID, String owner, Way myWay, String type, CrowdednessLevel crowdedness) {
        super(name, matchingID, owner, myWay, type, crowdedness);
    }

    public SingleLinkMockupService(String name, String matchingID, String owner, Way myWay) {
        super(name, matchingID, owner, myWay);
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

