
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof MockupService)) return false;
//
//        MockupService that = (MockupService) o;
//
//        if (!name.equals(that.name)) return false;
//        return myWay == that.myWay;
//    }

    /**
     *  Compare two MockupService (the comparison is compute on thr combination "NameOwnerWay")
     * @param o the object to compare this to
     * @return true if the two object are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockupService)) return false;

        MockupService that = (MockupService) o;

        if (!name.equals(that.name) && !owner.equals(that.owner)) return false;
        return myWay == that.myWay;
    }


    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + myWay.hashCode();
        return result;
    }

    /**
     *  Compare two MockupService (the comparison is compute on the combination of "NameOwnerWay")
     * @param o the object to compare this to
     * @return 0 if the two object are equal
     */
    @Override
    public int compareTo(Object o) {
        MockupService that = (MockupService) o;
        String strThis = this.name+this.owner+this.myWay;
        String strThat = that.name+that.owner+that.myWay;
        int result = strThis.compareTo(strThat);
        if (result == 0) {
            assert this.equals(that) :
                    this.getClass().getSimpleName() + ": compareTo inconsistent with equals.";
        }
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
}
