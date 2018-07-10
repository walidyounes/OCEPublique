/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.Recorder;

import Infrastructure.Agent.ReferenceAgent;
import OCE.ServiceAgent;

public interface IRecord {

    /**
     * Register in the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent
     * @param agentReference : the agent's Reference in the infrastructure which is associated to the serviceAgent
     */
    void registerServiceAgent(ServiceAgent serviceAgent, ReferenceAgent agentReference);

    /**
     * Unregister from the recording list the mapping between a serviceAgent and it's associated referenceAgent
     * @param serviceAgent : the serviceAgent ( we use only this parameter because it's enaugh)
     *
     */
    void unregisterServiceAgent(ServiceAgent serviceAgent);

}
