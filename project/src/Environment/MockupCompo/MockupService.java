
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupCompo;

import Environment.OCPlateforme.OCService;

import java.util.ArrayList;

public abstract class MockupService extends OCService {
    protected String name;
    private String owner;
    private Way myWay;

    public MockupService(String name, String ownerComponentName, Way myWay) {
        this.name = name;
        this.owner = ownerComponentName;
        this.myWay = myWay;
        this.linkedServices = new ArrayList<OCService>();
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Way getWay() {
        return myWay;
    }

    public String toString() {
        // return "Service "+name+" of Component "+ownerComponentName+" Links :
        // "+linkedServices;
        return "Service " + name + " of Component " + owner;
    }
}
