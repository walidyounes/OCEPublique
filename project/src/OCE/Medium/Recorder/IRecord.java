/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgentReference;
import OCE.Medium.ReferenceResolutionFailure;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;

import java.util.ArrayList;

public interface IRecord {

    /**
     * Register in the recording list the mapping between an OCEAgent and it's associated referenceAgent
     * @param oceAgent : the agent
     * @param infraAgentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    void registerOCEAgent(OCEAgent oceAgent, InfraAgentReference infraAgentReference);

    /**
     * Unregister from the recording list the mapping between an OCEAgent and it's associated referenceAgent
     * @param oceAgent : the agent ( we use only this parameter because it's enaugh)
     *
     */
    void unregisterOCEAgent(OCEAgent oceAgent);

    /**
     * Resolve the physical adresse (InfraAgentReference) of ONE OCEAgent
     * @param oceAgent : the agent
     * @return his physical reference
     */
    InfraAgentReference resolveAgentReference(OCEAgent oceAgent) throws ReferenceResolutionFailure;

    /**
     * Resolve the physical adresse (InfraAgentReference) of a list of OCEAgents (usually used in the case of more thant one recipient)
     * @param oceAgents : the list of the oceAgents
     * @return the list of corresponding physical references
     */
    ArrayList<InfraAgentReference> resolveAgentsReferences(ArrayList<OCEAgent> oceAgents) throws ReferenceResolutionFailure;

    /**
     * Retrieve and return the ServiceAgent which is attached to the physical service
     * @param attachedService : the physical service
     * @return the agent which is attached to it
     */
    ServiceAgent retrieveSAgentByPService(OCService attachedService);

    /**
     * Resolve the logical adresse (OCEAgent) of a list of InfraAgentReference
     * @param infraAgents the liste of the refrence of the infrastructure agent
     * @return the coresponding list of OCEAgent
     * @throws ReferenceResolutionFailure if one of the agents doesn't exist
     */
    ArrayList<OCEAgent> retrieveOCEAgentsByInfraAgentReferences(ArrayList<InfraAgentReference> infraAgents) throws ReferenceResolutionFailure;

    /**
     * Resolve the logical adresse (ServiceAgent) of the InfraAgentReference
     * @param infraAgent the refrence of the infrastructure agent
     * @return the coresponding OCEAgent
     * @throws ReferenceResolutionFailure if the agent doesn't exist
     */
    OCEAgent retrieveOCEAgentByInfraAgentReference(InfraAgentReference infraAgent) throws ReferenceResolutionFailure;
}
