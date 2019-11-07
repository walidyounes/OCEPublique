/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEDecisions;

import Logger.OCELogger;
import OCE.Agents.OCEAgent;
import OCE.InfrastructureMessages.DisconnectInfraMessage;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;
import java.util.logging.Level;

public class DisconnectDecision extends OCEDecision {

    /**
     * Create a disconnect decision
     * @param emitter    reference of the agent that sends the disconnect message
     * @param receivers the references of the receivers of the disconnect message
     */
    public DisconnectDecision(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }

    /**
     * Execute the decision that has been made by the engine
     *
     * @param communicationAdapter : the communication component of the engine
     */
    @Override
    public void toSelfTreat(ICommunicationAdapter communicationAdapter) {
        OCELogger.log(Level.INFO, "Treating a Disconnection decision ! ");
        //Create the message to send
        DisconnectInfraMessage disconnectInfraMessage = new DisconnectInfraMessage(null, null);
        //Send the message
        communicationAdapter.sendMessage(disconnectInfraMessage, this.emitter, this.receivers);
    }
}
