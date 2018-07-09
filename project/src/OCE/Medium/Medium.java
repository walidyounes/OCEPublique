/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium;


import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Communication.ICommunication;
import OCE.Medium.composants.Mediator;
import OCE.Medium.composants.MessageReceiver;
import OCE.Medium.composants.MessageSender;
import OCE.Medium.composants.Record;
import OCE.Medium.services.IAcheminement;
import OCE.Medium.services.IEnregistrement;
import OCE.Unifieur.services.IMatching;

public class Medium implements IEnregistrement, IAcheminement {

    private MessageSender messageSender;
    private MessageReceiver messageReceiver;
    private Mediator mediator;
    private Record record;

    public Medium(ICommunication communication, IMatching matching) {
        record = new Record();
        messageSender = new MessageSender();
        mediator = new Mediator(record, messageSender);
        messageReceiver = new MessageReceiver(mediator);
        messageSender.setMatching(matching);
        messageSender.setCommunication(communication); // binding requis de messageSender avec fourni d'infrastructure
    }


    public void addAgent(ReferenceAgent agent, OCService service) {
        record.addAgent(agent, service);
    }

    public void removeAgent(ReferenceAgent agent) {
        record.removeAgent(agent);
    }

    public Record getRecord() {

        return record;
    }

}
