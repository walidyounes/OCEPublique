/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.ServiceAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This compoenent is used to record each new ServiceAgent and it's associated reference in the infrastructure
 * It's used in the communicationAdapter class to seek for the physical reference associated to a serviceAgent when a certain agent wants to send a message
 * @author Walid YOUNES
 * @version 1.0
 */
public class Record implements IRecord{

    private Map<ServiceAgent, InfraAgentReference> agentsReferenceMap;

    public Record() {
        this.agentsReferenceMap =  new HashMap<>();
    }

    /**
     * Register in the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     * @param infraAgentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    @Override
    public void registerServiceAgent(ServiceAgent serviceAgent, InfraAgentReference infraAgentReference) {
        //If the serviceAgent doesn't exist we add it to the list
        if(!this.agentsReferenceMap.containsKey(serviceAgent)){
            this.agentsReferenceMap.put(serviceAgent, infraAgentReference);
        }
    }

    /**
     * Unregister from the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     *
     */
    @Override
    public void unregisterServiceAgent(ServiceAgent serviceAgent) {
        //If the serviceAgent exist we delete it
        if(this.agentsReferenceMap.containsKey(serviceAgent)){
            this.agentsReferenceMap.remove(serviceAgent);
        }
    }

    /**
     * Resolve the physical adresse (InfraAgentReference) of ONE ServiceAgent
     * @param serviceAgent : the service InfraAgent in question
     * @return his physical reference
     * @throws ReferenceResolutionFailure when the serviceAgent doesn't exist
     */
    @Override
    public InfraAgentReference resolveAgentReference(ServiceAgent serviceAgent) throws ReferenceResolutionFailure {
        if(this.agentsReferenceMap.containsKey(serviceAgent)){
            // if the serviceAgent exist we return it
            return this.agentsReferenceMap.get(serviceAgent);
        }else throw new ReferenceResolutionFailure("The serviceAgent * "+ serviceAgent.getMyID() + " * doesn't exist ! "); // Else we throw the exception
    }

    /**
     * Resolve the physical adresse (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
     * @param serviceAgents : the list of the serviceAgents
     * @return the list of corresponding physical references
     * @throws ReferenceResolutionFailure when a serviceAgent doesn't exist
     */
    @Override
    public ArrayList<InfraAgentReference> resolveAgentsReferences(ArrayList<ServiceAgent> serviceAgents) throws ReferenceResolutionFailure {
        ArrayList<InfraAgentReference> infraAgentReferenceList = new ArrayList<>();
        for (ServiceAgent serviceAgent : serviceAgents) {
            if(this.agentsReferenceMap.containsKey(serviceAgent)){
                // Add the physical reference of the serviceAgent if it exists
                infraAgentReferenceList.add(this.agentsReferenceMap.get(serviceAgent));
            }else{
                // Throw an exception
                throw new ReferenceResolutionFailure("The serviceAgent * "+ serviceAgent.getMyID() + " * doesn't exist ! ");
            }
        }
        return infraAgentReferenceList;
    }

    /**
     * Retrieve and return the ServiceAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    @Override
    public ServiceAgent retrieveSAgentByPService(OCService attachedService) {
        return null;
    }
}
