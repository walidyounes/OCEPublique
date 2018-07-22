/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Strategies.Reply;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;

import java.util.ArrayList;

/**
 * <b>SRP</b> - The Reply strategies depend on whether or not the reply is
 * immediate or not, if it is to an individual or to a group, non-blocking
 * or not, and if not whether or not replies are memorized or not
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public interface IReplyStrategy {
    void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> destinataires);
}
