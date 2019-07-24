/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.ARSAStrategies.Agree;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import OCE.InfrastructureMessages.InfraARSAMessages.AgreeInfraMessage;
import OCE.InfrastructureMessages.InfraARSAMessages.SelectInfraMessage;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * <b>SAG1.2</b> - Agent "Y" accepts the binding request made by agent "X".
 * "Y" contacts the binder agent in order for the the physical binding to be done
 * and sends an "Agree" message to "X" and "Not agree" messages to the others
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class ImmediateAgreeExplicitReply implements IAgreeStrategy {

    private InfraAgentReference agent;
    private ArrayList<IMessage> selections;

    public ImmediateAgreeExplicitReply(InfraAgentReference agent) {
        this.agent = agent;
        //this.selections = selections;
    }

    public void setSelections(ArrayList<IMessage> selections) {
        this.selections = selections;
    }

    /**
     * @param selectedTransmitter the transmitter whose message was selected
     * @return the list of rejected selection transmitters
     */
    private ArrayList<InfraAgentReference> getRejectedSelectionTransmitters(InfraAgentReference selectedTransmitter) {
        ArrayList<InfraAgentReference> rejectedTransmitters = new ArrayList<>();
        for (IMessage m : selections) {
            InfraAgentReference transmitter =m.getEmitter();
            if (!transmitter.equals(selectedTransmitter))
                rejectedTransmitters.add(transmitter);
        }
        return rejectedTransmitters;
    }

    @Override
    public void executer(ICommunication comm, OCService service, InfraAgentReference agentBinder, InfraAgentReference recipient) {
        System.out.println("immediate-Agreement-Explicit-Response");
        MyLogger.log(Level.INFO, "Strategy{ Name= immediate-Agreement-Explicit-Response, Phase= AGREE}");

//		MessageAgent bestSelection = best(selections)
        IMessage bestSelection = selections.get(0); // to remove
        InfraAgentReference refBinder = ((SelectInfraMessage) bestSelection).getBinderAgent();
        InfraAgentReference selectionTransmitter = bestSelection.getEmitter();
        ArrayList<InfraAgentReference> rejects = getRejectedSelectionTransmitters(selectionTransmitter);

        //MessageAgent binding = new BindingMessage("", "", "", "", 0);

        ArrayList<InfraAgentReference> recipients = new ArrayList<>();
        recipients.add(selectionTransmitter);
        IMessage agreement = new AgreeInfraMessage(selectionTransmitter, recipients);

//		No agree message sent to all rejects
        IMessage noAgreement = new AgreeInfraMessage(selectionTransmitter, rejects);
        comm.sendMessage(noAgreement);
        //comm.envoyerMessage(binding);
        comm.sendMessage(agreement);

        // S <- SN, SWA
    }
}
