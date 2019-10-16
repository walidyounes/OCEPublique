/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfraAgent;


import java.util.List;

public interface IScheduler {

    void startScheduling();

    void changeSpeed(EnumSpeed newSpeed);

    void changeSchedulingStrategy(ISchedulingStrategies schedulingStrategy);

    List<InfraAgent> stopScheduling();

    void addAgentToScheduler(InfraAgent infraAgent);

    void deleteAgentFromScheduler(InfraAgent infraAgent);

    void pauseScheduling();

    void restartScheduling();

    void setMaxCycleAgent(int maxCycleAgent);

    void resetCurrentCycleAgent();

}
