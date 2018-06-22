/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.OCPlateforme;

import MockupCompo.AddLinkException;
import MockupCompo.RemoveLinkException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class OCService {
    protected ArrayList<OCService> linkedServices;
    protected JSONObject proprieties; // Properties of the services

    public ArrayList<OCService> getLinkedServices() {
        return linkedServices;
    }

    public abstract void addLink(OCService s) throws AddLinkException;

    public abstract void removeLink(OCService s) throws RemoveLinkException;
}
