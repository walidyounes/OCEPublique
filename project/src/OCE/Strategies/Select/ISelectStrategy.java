/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Strategies.Select;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

import java.util.ArrayList;

/**
 * <b>SSL</b> - Two Select strategies are defined depending on whether or not
 * the selection is immediate or differed
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public interface ISelectStrategy {
    void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> recipients);
}
