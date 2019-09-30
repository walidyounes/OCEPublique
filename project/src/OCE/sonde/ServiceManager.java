/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import Logger.MyLogger;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Medium.Recorder.IRecord;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.*;
import java.util.logging.Level;


public class ServiceManager implements INotification {

    private IOCEServiceAgentFactory agentFactory;
    private IRecord agentRecorder;

    // private Map<InfraAgentReference, OCService> listAssocAgentServiceDisparus = new HashMap<>(); //  TODO walid : ça sert à quoi ???

    public ServiceManager(IOCEServiceAgentFactory agentFactory, IRecord agentRecorder) {
        this.agentFactory = agentFactory;
        this.agentRecorder = agentRecorder;
    }

    public void appearingServices(ArrayList<OCService> appearingServicesList) {
        for (OCService service : appearingServicesList) {
            Map.Entry<ServiceAgent, InfraAgentReference> agentS_referenceAgent_Association = agentFactory.createServiceAgent(service);
            MyLogger.log(Level.INFO, " Creating the service agent = " + agentS_referenceAgent_Association.getKey().toString() +  " associated to the service = " + service.toString());
            // Register the association between the ServiceAgent and it's reference in the infrastructure into the recording component of the medium
            agentRecorder.registerOCEAgent(agentS_referenceAgent_Association.getKey(), agentS_referenceAgent_Association.getValue());
        }
    }

    public void disappearingServices(ArrayList<OCService> disappearingServicesList) {
        // For each disappearing service
        for (OCService service : disappearingServicesList) {
            MyLogger.log(Level.INFO, " The service = " + service.toString()+ " has disappeared !");
            // Get the serviceAgent associated to this service
            Optional<ServiceAgent> serviceAgent = this.agentRecorder.retrieveSAgentByPService(service);
            //Check if the service agent exist
            if(serviceAgent.isPresent()){
                //Put the serviceAgent tto sleep
                System.out.println(" The agent = " + serviceAgent.toString() + " is put to SLEEP");
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                MyLogger.log(Level.INFO, " The agent = " + serviceAgent.toString() + " is put to SLEEP !");
                serviceAgent.get().setMyConnexionState(ServiceAgentConnexionState.Sleep);

                //Todo : check if this is necessary -> i think i should keep the association and don't unregister it
                // Unregister the association between the ServiceAgent and it's reference in the infrastructure from the recording component of the medium
                agentRecorder.unregisterOCEAgent(serviceAgent.get());
                // Todo walid : delete the agent from the infrastructure

                //Todo à voir ça sert à quoi et c'est utile
                // get the physical reference of the agent
                // InfraAgentReference refAgent = serviceAgent.getMyInfraAgent().getInfraAgentReference();

                // listAssocAgentServiceDisparus.put(refAgent, service);
            }else{
                MyLogger.log(Level.INFO, " No Agent is attached to the service = " + service.toString() + " !");
            }

        }
    }

   /* // voir à quoi sert cette méthode qui l'appelle --> walid : c'est la fonction lire message sonde de percevoir !!!!
    public boolean verifierServiceDisparu(InfraAgentReference ref) {

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
