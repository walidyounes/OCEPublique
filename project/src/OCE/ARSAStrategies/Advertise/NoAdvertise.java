/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Advertise;

import Logger.OCELogger;
import MASInfrastructure.Communication.ICommunication;

import java.util.logging.Level;

/**
 * <b>SN (SAD)</b> - The agent does not take on any action regardless of
 * the communication protocol's state/stage
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class NoAdvertise implements IAdvertiseStrategy {

    @Override
    public void executer(ICommunication comm) {
        System.out.println("This is No Advertise Strategy!");
        OCELogger.log(Level.INFO, "Strategy{ Name= no_Advertise, Phase=  ADVERTISE}");
    }
}
