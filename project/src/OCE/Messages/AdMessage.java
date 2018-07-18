/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;

import java.util.ArrayList;
import java.util.logging.Level;

public class AdMessage extends Message {

    /**
     * Create an advertise message
     * @param emitter    reference of the advertising agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public AdMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    @Override
    public AbstractDecision toSelfTreat() {
        MyLogger.log(Level.INFO, "Treating an advertisement message ! ");
        return null;
    }
}
