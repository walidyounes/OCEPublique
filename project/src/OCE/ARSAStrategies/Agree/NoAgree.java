/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Agree;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

import java.util.logging.Level;


/**
 * <b>SN (SAG)</b> - The agent does not take on any action regardless of
 * the communication protocol's state/stage
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class NoAgree implements IAgreeStrategy {

    @Override
    public void executer(ICommunication comm, OCService service, InfraAgentReference agentBinder, InfraAgentReference recipient) {
        System.out.println("This is a No Agree strategy");
        OCELogger.log(Level.INFO, "Strategy{ Name= no_Agree, Phase= AGREE}");
    }
}
