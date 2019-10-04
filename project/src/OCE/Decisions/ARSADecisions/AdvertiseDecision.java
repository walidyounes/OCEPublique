/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions.ARSADecisions;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Decisions.OCEDecision;
import OCE.InfrastructureMessages.InfraARSAMessages.AdvertiseInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Agents.OCEAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent an advertisement decision (i.e. send an advertisement message -usualy in boradcast-)
 * @author Walid YOUNES
 * @version 1.0
 */
public class AdvertiseDecision extends OCEDecision {
    private OCService myService;
    /**
     * Create an advertise decision
     * @param emitter    reference of the advertising agent
     * @param receivers the references of the receivers of the ad, if null == Broadcast
     */
    public AdvertiseDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers, OCService myService) {
        this.emitter= emitter;
        this.receivers = receivers;
        this.myService = myService;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an advertisement decision ! ");
        AdvertiseInfraMessage advertiseMessage = new AdvertiseInfraMessage(null, null, this.myService);
        communicationAdapter.sendMessageBroadcast(advertiseMessage, this.emitter, this.receivers);
    }

    @Override
    public String toString() {
        return "AdvertiseDecision{" +
                "myService=" + myService +
                ", emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
