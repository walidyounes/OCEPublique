/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.probe;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import Midlleware.AgentFactory.IOCEServiceAgentFactory;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Medium.Recorder.IRecord;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;


public class ServiceManager implements INotification {

    private IOCEServiceAgentFactory agentFactory;
    private IRecord agentRecorder;

    private Map<OCService, ServiceAgent> disappearedServiceAgent; // This list represent the set of disappeared service agent, it's used when a service is detected we check if it a reappearance and in that case we don't create a new service but we reactivate the old service agent

    public ServiceManager(IOCEServiceAgentFactory agentFactory, IRecord agentRecorder) {
        this.agentFactory = agentFactory;
        this.agentRecorder = agentRecorder;
        this.disappearedServiceAgent = new TreeMap<>();
    }

    public void appearingServices(ArrayList<OCService> appearingServicesList) {
        for (OCService service : appearingServicesList) {
            // Check if it's a reappearance of an past existed service
            if(this.disappearedServiceAgent.containsKey(service)){
                System.out.println("The service reappeared = "+service.toString());
                //Before ordering the creation of a new service agent, we check if it's existed before and in that case we only wake it up
                OCELogger.log(Level.INFO, "Reactivation the appearing service agent = "+ this.disappearedServiceAgent.get(service).toString());
                this.disappearedServiceAgent.get(service).setMyConnexionState(ServiceAgentConnexionState.NotConnected);
                this.agentRecorder.getAllAgents().add(this.disappearedServiceAgent.get(service));
            }else{
                //The service is a new service, thus we create his attached serviceAgent
                Map.Entry<ServiceAgent, InfraAgentReference> agentS_referenceAgent_Association = agentFactory.createServiceAgent(service);
                OCELogger.log(Level.INFO, " Creating the service agent = " + agentS_referenceAgent_Association.getKey().toString() +  " associated to the service = " + service.toString());
                // Register the association between the ServiceAgent and it's reference in the infrastructure into the recording component of the medium
                agentRecorder.registerOCEAgent(agentS_referenceAgent_Association.getKey(), agentS_referenceAgent_Association.getValue());
            }
        }


    }

    public void disappearingServices(ArrayList<OCService> disappearingServicesList) {
        // For each disappearing service
        for (OCService service : disappearingServicesList) {
            OCELogger.log(Level.INFO, " The service = " + service.toString()+ " has disappeared !");
            // Get the serviceAgent associated to this service
            Optional<ServiceAgent> serviceAgent = this.agentRecorder.retrieveSAgentByPService(service);
            //Check if the service agent exist
            if(serviceAgent.isPresent()){
                //Launch the suicide mechanism of the service agent
                serviceAgent.get().suicide();
                this.agentRecorder.getAllAgents().remove(serviceAgent.get());
                //Save the reference of the service agent and it's attached service in the specified association table
                this.disappearedServiceAgent.put(service, serviceAgent.get());

                System.out.println("Recording the reference of the agent = " + serviceAgent.get().toString() + " and it's attached service = "+ service.toString());
                OCELogger.log(Level.INFO, "Recording the reference of the agent = " + serviceAgent.get().toString() + " and it's attached service = "+ service.toString());

                //Todo : check if this is necessary -> i think i should keep the association and don't unregister it -> walid 24/10/2019 : i deleted it :)
                // Unregister the association between the ServiceAgent and it's reference in the infrastructure from the recording component of the medium
                // agentRecorder.unregisterOCEAgent(serviceAgent.get());

            }else{
                OCELogger.log(Level.INFO, " No Agent is attached to the service = " + service.toString() + " !");
            }

        }
    }

   /* // voir à quoi sert cette méthode qui l'appelle --> walid : c'est la fonction lire message probe de percevoir !!!!
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
