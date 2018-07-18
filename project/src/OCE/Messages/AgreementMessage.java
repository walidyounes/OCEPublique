/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Messages;


import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Decisions.AbstractDecision;

import java.util.ArrayList;
import java.util.logging.Level;

public class AgreementMessage extends Message {

    /**
     * Creer un message d'acceptation
     *
     */
    public AgreementMessage(InfraAgentReference emitter, ArrayList<InfraAgentReference> recievers) {
        this.emitter = emitter;
        this.recievers = recievers;
    }


    @Override
    public AbstractDecision toSelfTreat() {
        MyLogger.log(Level.INFO, "Treating a agreement message ! ");
        return null;
    }
}
