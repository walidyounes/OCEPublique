/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfrastructureAgent;


import java.util.List;

public class Scheduler implements IScheduler {

    private ISchedulingStrategies schedulingStrategy;

    public Scheduler(ISchedulingStrategies schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    @Override
    public void startScheduling() {
        schedulingStrategy.ordonnancer();
    }

    @Override
    public void changeSpeed(EnumSpeed newSpeed) {
        schedulingStrategy.changerVitesse(newSpeed);
    }

    @Override
    public void changeSchedulingStrategy(ISchedulingStrategies schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    @Override
    public List<InfrastructureAgent> stopScheduling() {
        return schedulingStrategy.arreterOrdonnancement();
    }

    @Override
    public void addAgentToScheduler(InfrastructureAgent infrastructureAgent) {
        schedulingStrategy.agentAjoute(infrastructureAgent);
    }

    public ISchedulingStrategies getSchedulingStrategy() {
        return schedulingStrategy;
    }

    @Override
    public void deleteAgentFromScheduler(InfrastructureAgent infrastructureAgent) {
        schedulingStrategy.agentRetire(infrastructureAgent);
    }


    /**
     * Put pause to the scheduling process of the agents
     */
    @Override
    public void pauseScheduling() {
        this.schedulingStrategy.pauseOrdonnancement();
    }

    /**
     * Resume the execution of the scheduling process of the agents
     */
    @Override
    public void restartScheduling() {
        this.schedulingStrategy.repriseOrdonnancement();
    }

    /**
     * Set the value of the number of agent cycle per OCE Cycle
     * @param maxCycleAgent
     */
    @Override
    public void setMaxCycleAgent(int maxCycleAgent) {
        this.schedulingStrategy.setMaxCycleAgent(maxCycleAgent);
    }

    /**
     * restart the OCE Cycle (initialize the current cycle to 0)
     */
    @Override
    public void resetCurrentCycleAgent() {
        this.schedulingStrategy.resetCurrentCycleAgent();
    }
}
