/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Reply;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * <b>SN (SRP)</b> - The agent does not take on any action regardless of
 * the communication protocol's state/stage
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class NoReply implements IReplyStrategy {
    @Override
    public void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> destinataires) {
        System.out.println("This is No Reply Strategy!");
        MyLogger.log(Level.INFO, "Strategy{ Name= no_Reply, Phase= Reply}");
    }
}
