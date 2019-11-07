
/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;

import AmbientEnvironment.OCPlateforme.OCService;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class MockupService extends OCService implements Comparable, Serializable{
    protected String name;
    protected String matchingID; // Used to check if two services match
    private String owner;
    private Way myWay;

    public MockupService(String name, String matchingID, String ownerComponentName, Way myWay) {
        this.name = name;
        this.matchingID = matchingID;
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
     * Get the matchingID (signature) of the service
     * @return the matchingID value
     */
    public String getMatchingID() {
        return matchingID;
    }

    /**
     * Set the value of matchingID of the service
     * @param matchingID  : the new value
     */
    public void setMatchingID(String matchingID) {
        this.matchingID = matchingID;
    }

    /**
     * Get a string unique representation of the service
     * @return the concatenation of the attributes of the service
     */
    public String getStringRepresentation(){
        return ""+this.name+"-" + this.matchingID +"-"+this.myWay+ "-"+this.owner;
    }

    /**
     *  Compare two MockupService (the comparison is compute on the combination "Name-Type-Owner-Way")
     * @param o the object to compare this to
     * @return true if the two object are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;

        MockupService that = (MockupService) o;

//        if (!name.equals(that.name) && !matchingID.equals(that.matchingID) && !owner.equals(that.owner)) return false;
//        return myWay == that.myWay;
        if (name.equals(that.name) && matchingID.equals(that.matchingID) && owner.equals(that.owner) &&  myWay == that.myWay) return true;
        return false;
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
        if(this == o) return 0;
        if(o==null || getClass() != o.getClass()) return -1;
        MockupService that = (MockupService) o;
        String strThis = this.name+this.matchingID +this.owner+this.myWay;
        String strThat = that.name+that.matchingID +that.owner+that.myWay;
        int result = strThis.compareTo(strThat);
        if (result == 0) {
            assert this.equals(that) :
                    this.getClass().getSimpleName() + ": compareTo inconsistent with equals.";
        }
        return result;
    }

    @Override
    public String toString() {
        return "Service " + name + "-"+ matchingID + " of " + owner;
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
