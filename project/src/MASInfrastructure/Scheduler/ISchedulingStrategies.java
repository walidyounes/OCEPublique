/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Directory.IAgentListener;

import java.util.List;

public interface ISchedulingStrategies extends IAgentListener {
    void startScheduling();

    /**
     * Start a special scheduling cycle with a set of agents and for a certain number of cycles.
     * An example of a use case for this method, is to treat the feedback : scheduling the agents which are supposed to treat the feedback.
     * @param listAgentsToSchedule  :   the list of agents to schedule
     * @param numberCycles          :   the number of agent cycle to run (One cycle = Perception, Decision, Action)
     */
    void startSpecialScheduling(List<InfrastructureAgent> listAgentsToSchedule, int numberCycles);

    void changeSpeed(EnumSpeed speed);

    void stopScheduling();

    // void pauseScheduling();

    // void rerunScheduling();

    void setMaxCycleAgent(int maxCycleAgent);

    void resetCurrentCycleAgent();

    void addSchedulingListener(SchedulerListener schedulerListener);

    void addAgent(InfrastructureAgent infrastructureAgent);

    void deleteAgent(InfrastructureAgent infrastructureAgent);
}
