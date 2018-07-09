/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;
import Logger.MyLogger;
import Midlleware.AgentFactory.IAFactory;
import OCE.Medium.services.IEnregistrement;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public class ServiceManager implements INotification {

    private IAFactory agentFactory;
    private IEnregistrement iEnregistrement;

    private Map<OCService, ServiceAgent> listRefAgent = new HashMap<>(); // TODO walid : ça sert à quoi ???
    private Map<ReferenceAgent, OCService> listAssocAgentServiceDisparus = new HashMap<>(); //  TODO walid : ça sert à quoi ???

    public ServiceManager(IAFactory fabriqueMAgents, IEnregistrement enregistrement) {
        this.agentFactory = fabriqueMAgents;
        this.iEnregistrement = enregistrement;
    }

    public void appearingServices(ArrayList<OCService> appearingServicesList) {
        for (OCService service : appearingServicesList) {

            MyLogger.log(Level.INFO, " Creating the service agent associated to the service = " + service.toString());
            ServiceAgent serviceAgent = agentFactory.createServiceAgent(service);

            listRefAgent.put(service, serviceAgent);
            // Enregistrement de l'association agent/service dans le composant Record de Medium
            iEnregistrement.addAgent(serviceAgent.getMyAssociatedAgent().getReferenceAgent(), service);
        }
    }

    public void disappearingServices(ArrayList<OCService> disappearingServicesList) {
        // for each disappearing service
        for (OCService service : disappearingServicesList) {
            // tget the serviceagent associated to this service
            ServiceAgent serviceAgent = listRefAgent.get(service);
            // get the physical reference of the agent
            ReferenceAgent refAgent = serviceAgent.getMyAssociatedAgent().getReferenceAgent();
            // Remove the agent from the record of the componenent "Medium"
            iEnregistrement.removeAgent(refAgent);
            //Todo à voir ça sert à quoi
            listAssocAgentServiceDisparus.put(refAgent, service);
        }
    }

    public Map<OCService, ServiceAgent> getListRefAgent() {
        return listRefAgent;
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
