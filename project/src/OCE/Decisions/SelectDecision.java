/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Decisions;

import OCE.BinderAgent;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceAgent;

import java.util.ArrayList;

/**
 * This class represent a selection decision (i.e. send a selection message to an agent to inform hi that it has been choosed as suitable connexion)
 * @author Walid YOUNES
 * @version 1.0
 */
public class SelectDecision extends AbstractDecision {
    private BinderAgent myBinderAgent;

    /**
     * Create a selection decision
     * @param emitter    reference of the selecting agent
     * @param recievers the references of the recievers of the ad, if null == Broadcast
     */
    public SelectDecision(ServiceAgent emitter, ArrayList<ServiceAgent> recievers) {
        this.emitter= emitter;
        this.recievers = recievers;
    }

    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {

    }
}
