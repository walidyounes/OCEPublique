/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;
import Logger.MyLogger;
import Midlleware.AgentFactory.IAFactory;
import OCE.Medium.Recorder.IRecord;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public class ServiceManager implements INotification {

    private IAFactory agentFactory;
    private IRecord agentRecorder;

    private Map<ReferenceAgent, OCService> listAssocAgentServiceDisparus = new HashMap<>(); //  TODO walid : ça sert à quoi ???

    public ServiceManager(IAFactory agentFactory, IRecord agentRecorder) {
        this.agentFactory = agentFactory;
        this.agentRecorder = agentRecorder;
    }

    public void appearingServices(ArrayList<OCService> appearingServicesList) {
        for (OCService service : appearingServicesList) {

            MyLogger.log(Level.INFO, " Creating the service agent associated to the service = " + service.toString());
            Map.Entry<ServiceAgent, ReferenceAgent> agentS_referenceAgent_Association = agentFactory.createServiceAgent(service);

            // Register the association between the ServiceAgent and it's reference in the infrastructure into the recording component of the medium
            agentRecorder.registerServiceAgent(agentS_referenceAgent_Association.getKey(), agentS_referenceAgent_Association.getValue());
        }
    }

    public void disappearingServices(ArrayList<OCService> disappearingServicesList) {
        // for each disappearing service
        for (OCService service : disappearingServicesList) {
            MyLogger.log(Level.INFO, " The service = " + service.toString()+ " has disappeared !");
            // tget the serviceAgent associated to this service
            ServiceAgent serviceAgent = this.agentRecorder.retrieveSAgentByPService(service);
            // Unregister the association between the ServiceAgent and it's reference in the infrastructure from the recording component of the medium
            agentRecorder.unregisterServiceAgent(serviceAgent);

            // get the physical reference of the agent
            ReferenceAgent refAgent = serviceAgent.getMyAssociatedAgent().getReferenceAgent(); // Todo : check if it's useful now
            //Todo à voir ça sert à quoi
            listAssocAgentServiceDisparus.put(refAgent, service);
        }
    }

   /* // voir à quoi sert cette méthode qui l'appelle --> walid : c'est la fonction lire message sonde de percevoir !!!!
    public boolean verifierServiceDisparu(ReferenceAgent ref) {

        OCService service = listAssocAgentServiceDisparus.get(ref);
        if (service == null) {

            return false;

        } else {

            listAssocAgentServiceDisparus.remove(ref);
            listRefAgent.remove(service);

            return true;
        }
    }*/
}
