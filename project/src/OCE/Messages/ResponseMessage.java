/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the response message sent in the second step of the ARSA protocol (it tells the agent that send the ad that i'm interested)
 * @author Walid YOUNES
 * @version 1.0
 */
public class ResponseMessage extends Message {

    /**
     * Create a Response message
     *
     * @param emitter    reference of the responding agent
     * @param recievers the references of the recievers of the response, if null == Broadcast
     */
    public ResponseMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    /**
     * create a Response message (empty)
     */
    public ResponseMessage() {
        this.emitter= null;
        this.recievers = null;
    }

    @Override
    public AbstractDecision toSelfTreat() {
        MyLogger.log(Level.INFO, "Treating a response message ! ");
        return null;
    }
}
