/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Directory.IAgentListener;

import java.util.List;

public interface ISchedulingStrategies extends IAgentListener {
    void ordonnancer();

    void changerVitesse(EnumSpeed vitesse);

    List<InfraAgent> arreterOrdonnancement();

    void pauseOrdonnancement();

    void repriseOrdonnancement();

    void setMaxCycleAgent(int maxCycleAgent);

    void resetCurrentCycleAgent();

    void addOrdonnaceurListener(SchedulerListener schedulerListener);

    void agentAjoute(InfraAgent infraAgent);

    void agentRetire(InfraAgent infraAgent);
}
