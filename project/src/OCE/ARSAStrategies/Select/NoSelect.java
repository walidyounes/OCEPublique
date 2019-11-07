/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Select;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * <b>SN (SSL)</b> - The agent does not take on any action regardless of
 * the communication protocol's state/stage
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class NoSelect implements ISelectStrategy {

    @Override
    public void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> recipients) {
        System.out.println("This is No Select strategy");
        OCELogger.log(Level.INFO, "Strategy{ Name= no_Select, Phase= SELECT}");
    }
}
