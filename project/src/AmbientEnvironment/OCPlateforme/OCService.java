/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.OCPlateforme;

import AmbientEnvironment.MockupCompo.AddLinkException;
import AmbientEnvironment.MockupCompo.RemoveLinkException;


import java.io.Serializable;
import java.util.ArrayList;

public abstract class OCService implements Serializable {
    protected ArrayList<OCService> linkedServices;
    // protected JSONObject proprieties; // Properties of the services

    public ArrayList<OCService> getLinkedServices() {
        return linkedServices;
    }

    public abstract void addLink(OCService s) throws  AddLinkException;

    public abstract void removeLink(OCService s) throws RemoveLinkException;
}
