/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Strategies.Reply;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import OCE.Messages.ReplyMessage;

import java.util.ArrayList;
import java.util.logging.Level;


/**
 * <b>SR1.1</b> - Agent "Y" sends a reply to agent "X", having sent
 * the advertisement of most interest
 *
 * @author Rain-Alexandra BEGG
 * @version 0.1
 */
public class NonBlockingIndvImmReply implements IReplyStrategy {

    private InfraAgentReference agent;
    private ArrayList<IMessage> ads;

    public NonBlockingIndvImmReply(InfraAgentReference agent) {
        this.agent = agent;
        //this.ads = ads;
    }


    public void setAds(ArrayList<IMessage> ads) {
        this.ads = ads;
    }

    @Override
    public void executer(ICommunication comm, OCService service, ArrayList<InfraAgentReference> destinataires) {
        System.out.println("non-Blocking-Targeted-Immediate-Response");
        MyLogger.log(Level.INFO, "Strategy{ Name= non-Blocking-Targeted-Immediate-Response, Phase= Reply}");

//		Message bestAd = best(ads);
        //	MessageAgent bestAd = ads.get(0); // to remove
        //	ArrayList<ReferenceAgent> bestTransmitter = new ArrayList<>();
        //	bestTransmitter.addAll(bestAd.getDestinataires());

        IMessage reply = new ReplyMessage(agent, destinataires);
        comm.sendMessage(reply);

        // S <- S
    }
}
