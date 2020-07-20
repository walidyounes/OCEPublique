/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.OCPlateforme;

import AmbientEnvironment.MockupCompo.AddLinkException;
import AmbientEnvironment.MockupCompo.RemoveLinkException;
//import scala.Int;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class OCService implements Serializable {



    protected ArrayList<OCService> linkedServices;
    protected CrowdednessLevel Crowdedness;
   // protected ServiceQuality Crowdness;
    //protected List<ServiceQuality> serviceQualities;
    // protected JSONObject proprieties; // Properties of the services

    public ArrayList<OCService> getLinkedServices() {
        return linkedServices;
    }

    public String getCrowdedness(){ return Crowdedness.toString(); }

   // public  List<ServiceQuality> getServiceQalities(){return serviceQualities;}

    public abstract void addLink(OCService s) throws  AddLinkException;

    public abstract void removeLink(OCService s) throws RemoveLinkException;
}
