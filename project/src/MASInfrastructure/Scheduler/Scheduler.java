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
        schedulingStrategy.startScheduling();
    }

    @Override
    public void changeSpeed(EnumSpeed newSpeed) {
        schedulingStrategy.changeSpeed(newSpeed);
    }

    @Override
    public void changeSchedulingStrategy(ISchedulingStrategies schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    @Override
    public void stopScheduling() {
        schedulingStrategy.stopScheduling();
    }

    @Override
    public void addAgentToScheduler(InfrastructureAgent infrastructureAgent) {
        schedulingStrategy.addAgent(infrastructureAgent);
    }

    public ISchedulingStrategies getSchedulingStrategy() {
        return schedulingStrategy;
    }

    @Override
    public void deleteAgentFromScheduler(InfrastructureAgent infrastructureAgent) {
        schedulingStrategy.deleteAgent(infrastructureAgent);
    }

    /**
     * Start a special scheduling cycle with a set of agents and for a certain number of cycles.
     * An example of a use case for this method, is to treat the feedback : scheduling the agents which are supposed to treat the feedback.
     * @param listAgentsToSchedule :   the list of agents to schedule
     * @param numberCycles         :   the number of agent cycle to run (One cycle = Perception, Decision, Action)
     **/
    @Override
    public void startSpecialScheduling(List<InfrastructureAgent> listAgentsToSchedule, int numberCycles) {
        this.schedulingStrategy.startSpecialScheduling(listAgentsToSchedule,numberCycles);
    }

//    /**
//     * Put pause to the scheduling process of the agents
//     */
//    @Override
//    public void pauseScheduling() {
//        this.schedulingStrategy.pauseScheduling();
//    }
//
//    /**
//     * Resume the execution of the scheduling process of the agents
//     */
//    @Override
//    public void restartScheduling() {
//        this.schedulingStrategy.rerunScheduling();
//    }

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
