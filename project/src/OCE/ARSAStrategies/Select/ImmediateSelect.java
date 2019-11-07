/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Select;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.InfrastructureMessages.InfraARSAMessages.SelectInfraMessage;

import java.util.ArrayList;
import java.util.logging.Level;


/**
 * <b>SSlc1</b> - Agent "X" selects a response that it considers of interest,
 * sends a "select" message to the agent in question and triggers the binding
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class ImmediateSelect implements ISelectStrategy {

    private InfraAgentReference agent;
    private ArrayList<IMessage> responses;

    public ImmediateSelect(InfraAgentReference agent) {
        this.agent = agent;
        // this.responses = responses;
    }

    public void setResponses(ArrayList<IMessage> responses) {
        this.responses = responses;
    }

    @Override
    public void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> recipients) {
        System.out.println("immediate-Select");
        OCELogger.log(Level.INFO, "Strategy{ Name= immediate-Select, Phase= SELECT}");

//		InfraMessage bestReply = best(replies);
     //   MessageAgent bestReply = responses.get(0); // to remove
    //    ReferenceAgent bestTransmitter = bestReply.getExpediteur();

        InfraAgentReference refBinder = new InfraAgentReference(); // ToDO walid générer la référence de l'agent Binder --  to remove

//		the binder will be created and initialized with the service of the advertising agent
        //MessageAgent binding = new BindingMessage("", "", "", "", 0);

    //    ArrayList<ReferenceAgent> recipient = new ArrayList<>();
    //    recipient.add(bestTransmitter);
        InfraMessage selection = new SelectInfraMessage(agent, recipients, refBinder);

        // comm.envoyerMessage(binding);
        comm.sendMessage(selection);

        // S <- SN, SWA
    }
}
