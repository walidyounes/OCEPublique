/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Medium.ReferenceResolutionFailure;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This component is used to record each new ServiceAgent and it's associated reference in the infrastructure
 * It's used in the communicationAdapter class to seek for the physical reference associated to a serviceAgent when a certain agent wants to send a message
 * @author Walid YOUNES
 * @version 1.0
 */
public class Record implements IRecord{

    private Map<OCEAgent, InfraAgentReference> agentsReferenceMap;
    private ObservableList listOCEAgents; // Observable list used for visualisation in the UI

    public Record() {
        this.agentsReferenceMap =  new HashMap<>();
        this.listOCEAgents = FXCollections.observableArrayList();
    }

    /**
     * Register in the recording list the mapping between an OCEAgent and it's associated referenceAgent
     * @param oceAgent : the agent
     * @param infraAgentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    @Override
    public void registerOCEAgent(OCEAgent oceAgent, InfraAgentReference infraAgentReference) {
        //If the serviceAgent doesn't exist we add it to the list
        if(!this.agentsReferenceMap.containsKey(oceAgent)){
            this.agentsReferenceMap.put(oceAgent, infraAgentReference);
            //Add the agent to the list for visualisation in the UI
            Platform.runLater(
                    () -> {
                        // Update UI here
                        this.listOCEAgents.add(oceAgent);
                    }
            );

        }
    }

    /**
     * Unregister from the recording list the mapping between an OCEAgent and it's associated referenceAgent
     * @param oceAgent : the agent
     */
    @Override
    public void unregisterOCEAgent(OCEAgent oceAgent) {
        //If the serviceAgent exist we delete it
        if(this.agentsReferenceMap.containsKey(oceAgent)){
            System.out.println("Deleting from OCE the agent = " + oceAgent.toString());
            this.agentsReferenceMap.remove(oceAgent);
            //Delete the agent from the list for visualisation in the UI
            //Add the agent to the list for visualisation in the UI
            Platform.runLater(
                    () -> {
                        // Update UI here
                        this.listOCEAgents.remove(oceAgent);
                    }
            );
        }
    }

    /**
     * Resolve the physical address (InfraAgentReference) of ONE OCEAgent
     * @param oceAgent : the agent InfrastructureAgent in question
     * @return his physical reference
     * @throws ReferenceResolutionFailure when the serviceAgent doesn't exist
     */
    @Override
    public InfraAgentReference resolveAgentReference(OCEAgent oceAgent) throws ReferenceResolutionFailure {
        if(this.agentsReferenceMap.containsKey(oceAgent)){
            // if the serviceAgent exist we return it's reference
            return this.agentsReferenceMap.get(oceAgent);
        }else throw new ReferenceResolutionFailure("The serviceAgent * "+ oceAgent.getMyID() + " * doesn't exist ! "); // Else we throw the exception
    }

    /**
     * Resolve the physical address (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
     * @param oceAgents : the list of the oceAgents
     * @return the list of corresponding physical references
     * @throws ReferenceResolutionFailure when a serviceAgent doesn't exist
     */
    @Override
    public ArrayList<InfraAgentReference> resolveAgentsReferences(ArrayList<OCEAgent> oceAgents) throws ReferenceResolutionFailure {
        ArrayList<InfraAgentReference> infraAgentReferenceList = new ArrayList<>();
        for (OCEAgent oceAgent : oceAgents) {
            if(this.agentsReferenceMap.containsKey(oceAgent)){
                // Add the physical reference of the serviceAgent if it exists
                infraAgentReferenceList.add(this.agentsReferenceMap.get(oceAgent));
            }else{
                // Throw an exception
                throw new ReferenceResolutionFailure("The serviceAgent * "+ oceAgent.getMyID() + " * doesn't exist ! ");
            }
        }
        return infraAgentReferenceList;
    }

    /**
     * Resolve the logical address (OCEAgent) of the InfraAgentReference
     * @param infraAgent the reference of the infrastructure agent
     * @return the corresponding OCEAgent
     * @throws ReferenceResolutionFailure if the agent doesn't exist
     */
    @Override
    public OCEAgent retrieveOCEAgentByInfraAgentReference(InfraAgentReference infraAgent) throws ReferenceResolutionFailure{
        if(this.agentsReferenceMap.containsValue(infraAgent)){
            // if the serviceAgent exist we return it
            return this.getKeysByValue(this.agentsReferenceMap, infraAgent).iterator().next();
        }else {
            throw new ReferenceResolutionFailure("The serviceAgent with the Infra-Reference* "+ infraAgent + " * doesn't exist ! ");
        }
    }

    /**
     * Resolve the logical address (OCEAgent) of a list of InfraAgentReference
     * @param infraAgents the list of the reference of the infrastructure agent
     * @return the corresponding list of OCEAgents
     * @throws ReferenceResolutionFailure if one of the agents doesn't exist
     */
    @Override
    public ArrayList<OCEAgent> retrieveOCEAgentsByInfraAgentReferences(ArrayList<InfraAgentReference> infraAgents) throws ReferenceResolutionFailure{
        ArrayList<OCEAgent> OCEAgents = new ArrayList<>();
        for (InfraAgentReference infraAgent : infraAgents ) {
            if (this.agentsReferenceMap.containsValue(infraAgent)){
                OCEAgents.addAll(this.getKeysByValue(this.agentsReferenceMap, infraAgent));
            }else{
                throw new ReferenceResolutionFailure("The serviceAgent with the Infra-Reference* "+ infraAgent + " * doesn't exist ! ");
            }
        }return OCEAgents;
    }

    /**
     * Retrieve and return the OCEAgent which is associated to the  service
     * @param attachedService : the associated service
     * @return the serviceAgent if it exists, null otherwise
     */
    @Override
    public Optional<ServiceAgent> retrieveSAgentByPService(OCService attachedService) {
        //Cast the matchingID of the object from OCEService to MockupService so we can check for equality
        MockupService attachedMockupService = (MockupService) attachedService;
        //The service agent returned by this function
        Optional<ServiceAgent> serviceAgent = Optional.empty();
        //Filter the list of agents to keep only Service Agent
        List<ServiceAgent> listServiceAgent = this.agentsReferenceMap.keySet().stream().filter(m -> m instanceof ServiceAgent).map(m-> (ServiceAgent) m).collect(Collectors.toList());

        Iterator<ServiceAgent> agentIterator = listServiceAgent.iterator() ; // for iterating over the set of serviceAgent
        boolean found=false;
        while(agentIterator.hasNext() && !found){
            ServiceAgent currentServiceAgent = agentIterator.next();
            //get the attached service of the agent "currentAgent" and cast th result to a mockup service
            MockupService currentService = (MockupService) currentServiceAgent.getHandledService();
            if(attachedMockupService.equals(currentService)){ // it's the service that we are looking for
                found = true;
                serviceAgent = Optional.ofNullable(currentServiceAgent);
                OCELogger.log(Level.INFO, " Found Agent = "+serviceAgent.toString());
            }
        }
        return serviceAgent;
    }

    /**
     * Get the set of all agents existing in the system
     * @return the list of agent present in the environment
     */
    @Override
    public ObservableList<OCEAgent> getAllAgents() {
        return this.listOCEAgents;
    }

    /**
     * private function used to get the corresponding keys from a value from a map
     * @param map : The map <OCEAgent, InfraAgentReference>
     * @param value : The InfraAgentReference that we are looking for the keys corresponding
     * @param <T> Objects
     * @param <E> Objects
     * @return the corresponding set of keys
     */
    private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
