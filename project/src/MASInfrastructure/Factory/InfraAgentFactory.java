/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Factory;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Directory.IAgentDirectory;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.State.LifeCycle;
import MASInfrastructure.Scheduler.IScheduler;

public class InfraAgentFactory implements IInfraAgentFactory, ISuicideService {
    private final IAgentDirectory annuaire;
    private final IScheduler scheduler;

    public InfraAgentFactory(IAgentDirectory annuaire, IScheduler scheduler) {
        this.annuaire = annuaire;
        this.scheduler = scheduler;
    }

    @Override
    public InfrastructureAgent createInfrastructureAgent(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager) {
        InfrastructureAgent infrastructureAgent = new InfrastructureAgent(attachedService, lifeCycle, myMailBoxManager);
        annuaire.addAgent(infrastructureAgent);
        scheduler.addAgentToScheduler(infrastructureAgent);
        return infrastructureAgent;
    }

    @Override
    public void suicide(InfraAgentReference infraAgentReference) {
        InfrastructureAgent infrastructureAgentSuicide = annuaire.getAgentByRef(infraAgentReference); // walid : Get the corresponding agent to delete from the scheduler
        annuaire.removeAgent(infraAgentReference);
        scheduler.deleteAgentFromScheduler(infrastructureAgentSuicide);
    }

}
