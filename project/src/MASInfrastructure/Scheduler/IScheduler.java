/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfrastructureAgent;


import java.util.List;

public interface IScheduler {

    void startScheduling();

    void changeSpeed(EnumSpeed newSpeed);

    void changeSchedulingStrategy(ISchedulingStrategies schedulingStrategy);

    List<InfrastructureAgent> stopScheduling();

    void addAgentToScheduler(InfrastructureAgent infrastructureAgent);

    void deleteAgentFromScheduler(InfrastructureAgent infrastructureAgent);

    void pauseScheduling();

    void restartScheduling();

    void setMaxCycleAgent(int maxCycleAgent);

    void resetCurrentCycleAgent();

}
