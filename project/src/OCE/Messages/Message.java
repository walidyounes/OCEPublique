/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Communication.IMessage;
import OCE.ServiceAgent;

import java.util.ArrayList;

public abstract class Message implements IMessage {

    protected ServiceAgent emitter;
    protected ArrayList<ServiceAgent> receivers; // La liste des déstinataires du message --> il peut être un seul agent ou plusieurs, si = null -> en diffusiion broadcast

    // public abstract AbstractPerception toPerception();

    public abstract OCService getService();
}
