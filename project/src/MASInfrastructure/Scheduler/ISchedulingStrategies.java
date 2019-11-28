/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Directory.IAgentListener;

import java.util.List;

public interface ISchedulingStrategies extends IAgentListener {
    void startScheduling();

    void changeSpeed(EnumSpeed speed);

    List<InfrastructureAgent> stopScheduling();

    void pauseScheduling();

    void rerunScheduling();

    void setMaxCycleAgent(int maxCycleAgent);

    void resetCurrentCycleAgent();

    void addSchedulingListener(SchedulerListener schedulerListener);

    void addAgent(InfrastructureAgent infrastructureAgent);

    void deleteAgent(InfrastructureAgent infrastructureAgent);
}
