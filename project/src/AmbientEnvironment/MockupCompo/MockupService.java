
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;

public abstract class MockupService extends OCService {
    protected String name;
    private String owner;
    private Way myWay;

    public MockupService(String name, String ownerComponentName, Way myWay) {
        this.name = name;
        this.owner = ownerComponentName;
        this.myWay = myWay;
        this.linkedServices = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockupService)) return false;

        MockupService that = (MockupService) o;

        if (!name.equals(that.name)) return false;
        return myWay == that.myWay;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + myWay.hashCode();
        return result;
    }
}
