/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This compoenent is used to record each new ServiceAgent and it's associated reference in the infrastructure
 * It's used in the communicationAdapter class to seek for the physical reference associated to a serviceAgent when a certain agent wants to send a message
 * @author Walid YOUNES
 * @version 1.0
 */
public class Record implements IRecord{

    private Map<OCEAgent, InfraAgentReference> agentsReferenceMap;

    public Record() {
        this.agentsReferenceMap =  new HashMap<>();
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
        }
    }

    /**
     * Unregister from the recording list the mapping between an OCEAgent and it's associated referenceAgent
     * @param oceAgent : the agent
     *
     */
    @Override
    public void unregisterOCEAgent(OCEAgent oceAgent) {
        //If the serviceAgent exist we delete it
        if(this.agentsReferenceMap.containsKey(oceAgent)){
            this.agentsReferenceMap.remove(oceAgent);
        }
    }

    /**
     * Resolve the physical adresse (InfraAgentReference) of ONE OCEAgent
     * @param oceAgent : the agent InfraAgent in question
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
     * Resolve the physical adresse (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
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
     * Resolve the logical adresse (OCEAgent) of the InfraAgentReference
     * @param infraAgent the refrence of the infrastructure agent
     * @return the coresponding OCEAgent
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
     * Resolve the logical adresse (OCEAgent) of a list of InfraAgentReference
     * @param infraAgents the liste of the refrence of the infrastructure agent
     * @return the coresponding list of OCEAgents
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
     * Retrieve and return the OCEAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    @Override
    public ServiceAgent retrieveSAgentByPService(OCService attachedService) {
        return null;
    }

    /**
     * private fonction used to get the corresponding keys from a value from a map
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
