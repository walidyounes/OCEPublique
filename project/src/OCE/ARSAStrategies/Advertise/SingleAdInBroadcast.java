/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Advertise;

import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import OCE.InfrastructureMessages.InfraARSAMessages.AdvertiseInfraMessage;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.logging.Level;


/**
 * <b>SA1.1</b> - The agent sends an "Ad" message to all the agents present in
 * the system
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class SingleAdInBroadcast implements IAdvertiseStrategy {

    private InfraAgentReference agent;

    public SingleAdInBroadcast(InfraAgentReference agent) {
        this.agent = agent;
    }

    @Override
    public void executer(ICommunication communicationManager) {
        System.out.println("single-Ad-In-Broadcast");
        MyLogger.log(Level.INFO, "Strategy{ Name= single-Ad-In-Broadcast, Phase= ADVERTISE}");
        InfraMessage ad = new AdvertiseInfraMessage(agent, null); // la liste des déstinataires est == null car en braodcast
        communicationManager.sendMessageBroadcast(ad);
    }
}
