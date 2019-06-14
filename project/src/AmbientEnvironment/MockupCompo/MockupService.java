
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;

public abstract class MockupService extends OCService implements Comparable {
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

    public void setOwner(String owner){
        this.owner = owner;
    }

    public String toString() {
        // return "Service "+name+" of Component "+ownerComponentName+" Links :
        // "+linkedServices;
        return "Service " + name + " of " + owner;
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



    @Override
    public void addLink(OCService s) throws AddLinkException {
        //TODO : Complete the code
    }

    @Override
    public void removeLink(OCService s) throws RemoveLinkException {
        //TODO : Complete the code
    }

    // TODO : recode "equal" function
    /**
     *  Compare two MockupService (the comparison is compute
     * @param o the object to compare this to
     * @return 0 if the two object are equal
     */
    @Override
    public int compareTo(Object o) {
        MockupService athat = (MockupService) o;
        String sThat = ""+athat.getName()+athat.getOwner()+athat.getWay();
        String sThis = ""+ this.getName()+this.getOwner()+this.getWay();
        return sThis.compareTo(sThat);
    }
}
