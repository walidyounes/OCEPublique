/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Agree;


import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

/**
 * <b>SAG</b> - The Agreement strategies depend on whether or not the agreement
 * is immediate or differed, explicit or implicit
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public interface IAgreeStrategy {
    void executer(ICommunication comm, OCService service, InfraAgentReference agentBinder, InfraAgentReference recipient);
}
