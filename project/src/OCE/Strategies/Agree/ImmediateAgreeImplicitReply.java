/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Strategies.Agree;


import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * <b>SAG1.1</b> - Agent "Y" accepts the binding request made by agent "X" and
 * contacts the binder agent in order for the physical binding to be done
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class ImmediateAgreeImplicitReply implements IAgreeStrategy {

    private InfraAgentReference agent;
    private ArrayList<IMessage> selections;

    public ImmediateAgreeImplicitReply(InfraAgentReference agent) {
        this.agent = agent;
        // this.selections = selections;
    }

    public void setSelections(ArrayList<IMessage> selections) {
        this.selections = selections;
    }

    @Override
    public void executer(ICommunication comm, OCService service, InfraAgentReference agentBinder, InfraAgentReference recipient) {
        System.out.println("immediate-Agreement-Implicit-Response");
        MyLogger.log(Level.INFO, "Strategy{ Name= immediate-Agreement-Implicit-Response, Phase= AGREE}");

//		InfraMessage bestSelection = best(selections);
      //  MessageAgent bestSelection = selections.get(0); // to remove
      //  ReferenceAgent refBinder = ((SelectInfraMessage) bestSelection).getAgentBinder();

        //MessageAgent binding = new BindingMessage("", "", "", "", 0);
        // comm.envoyerMessage(binding);
        MyLogger.log(Level.INFO, "Contacting "+agentBinder.toString()+ "} to start the binding ");
        // S <- SN, SWA
    }
}
