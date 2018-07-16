/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.ServiceAgent;

import java.util.ArrayList;

public interface IRecord {

    /**
     * Register in the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     * @param infraAgentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    void registerServiceAgent(ServiceAgent serviceAgent, InfraAgentReference infraAgentReference);

    /**
     * Unregister from the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent ( we use only this parameter because it's enaugh)
     *
     */
    void unregisterServiceAgent(ServiceAgent serviceAgent);

    /**
     * Resolve the physical adresse (InfraAgentReference) of ONE ServiceAgent
     * @param serviceAgent : the service InfraAgent in question
     * @return his physical reference
     */
    InfraAgentReference resolveAgentReference(ServiceAgent serviceAgent) throws ReferenceResolutionFailure;

    /**
     * Resolve the physical adresse (InfraAgentReference) of a list of ServiceAgents (usually used in the case of more thant one recipient)
     * @param serviceAgents : the list of the serviceAgents
     * @return the list of corresponding physical references
     */
    ArrayList<InfraAgentReference> resolveAgentsReferences(ArrayList<ServiceAgent> serviceAgents) throws ReferenceResolutionFailure;

    /**
     * Retrieve and return the ServiceAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    ServiceAgent retrieveSAgentByPService(OCService attachedService);
}
