/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.Messages.AdMessage;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represent an advertisement decision (i.e. send an advertisement message -usualy in boradcast-)
 * @author Walid YOUNES
 * @version 1.0
 */
public class AdvertiseDecision extends AbstractDecision {
    private OCService myService;
    /**
     * Create an advertise decision
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AdvertiseDecision(ServiceAgent emitter, ArrayList<ServiceAgent> recievers, OCService myService) {
        this.emitter= emitter;
        this.recievers = recievers;
        this.myService = myService;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        MyLogger.log(Level.INFO, "Treating an advertisement decision ! ");
        AdMessage adMessage = new AdMessage(null, null, this.myService);
        communicationAdapter.sendMessageBroadcast(adMessage, this.emitter, this.recievers);
    }

    @Override
    public String toString() {
        return "AdvertiseDecision{" +
                "myService=" + myService +
                ", emitter=" + emitter +
                ", recievers=" + recievers +
                '}';
    }
}
