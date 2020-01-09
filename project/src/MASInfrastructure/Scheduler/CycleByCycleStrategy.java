/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import Logger.OCELogger;
import MASInfrastructure.Agent.InfrastructureAgent;

import java.util.List;
import java.util.logging.Level;

public class CycleByCycleStrategy implements ISchedulingStrategies {

    private List<InfrastructureAgent> listAgentsToSchedule; // list of observed agents
    private List<SchedulerListener> schedulerListeners; // list of observers
    private int speed;
    private int currentAgentCycle;
    private int maxCycleAgent;
    private boolean isRunning;
    private final int defaultMaxCycleAgent = 100;

    public CycleByCycleStrategy(List<InfrastructureAgent> listInfrastructureAgent, List<SchedulerListener> listListenerActuels) {
        listAgentsToSchedule = listInfrastructureAgent;
        schedulerListeners = listListenerActuels;
        this.currentAgentCycle = 0;
        this.maxCycleAgent = defaultMaxCycleAgent;
        changeSpeed(EnumSpeed.CENT);
    }


    @Override
    public void startScheduling() {
        //Initialize the parameters for the execution
        this.isRunning = true;
        this.currentAgentCycle = 0;

        InfrastructureAgent currentInfrastructureAgent;
        while(isRunning) {
            synchronized (this) {

                while (this.currentAgentCycle < this.maxCycleAgent && listAgentsToSchedule.size()>0 ) {
                    currentInfrastructureAgent = listAgentsToSchedule.get(0);
                    OCELogger.log(Level.INFO, " *********************************** Cycle of the Agent = " + currentInfrastructureAgent.getInfraAgentReference() + " ***********************************");
                    currentInfrastructureAgent.run(); //Launch the behavior of the agent in its PERCEPTION State
                    currentInfrastructureAgent.run(); //Launch the behavior of the agent in its DECISION State
                    currentInfrastructureAgent.run(); //Launch the behavior of the agent in its ACTION State
                    listAgentsToSchedule.remove(currentInfrastructureAgent);
                    listAgentsToSchedule.add(currentInfrastructureAgent);
                    this.currentAgentCycle++;
                }
            }
        }
    }

    /**
     * Start a special scheduling cycle with a set of agents and for a certain number of cycles.
     * An example of a use case for this method, is to treat the feedback : scheduling the agents which are supposed to treat the feedback.
     * @param listAgentsToSchedule :   the list of agents to schedule
     * @param numberCycles         :   the number of agent cycle to run (One cycle = Perception, Decision, Action)
     */
    @Override
    public void startSpecialScheduling(List<InfrastructureAgent> listAgentsToSchedule, int numberCycles) {
        int currentCycle = 0;
        OCELogger.log(Level.INFO, "----------------------------------------------------------------------------------------- STARTING SPECIAL SCHEDULING -------------------------------------------------------------------------------------------- \n");
        InfrastructureAgent currentInfrastructureAgent;

        while (currentCycle < numberCycles && listAgentsToSchedule.size()>0 ) {
            currentInfrastructureAgent = listAgentsToSchedule.get(0);
            OCELogger.log(Level.INFO, " -----------------------------------  Cycle of the Agent = " + currentInfrastructureAgent.getInfraAgentReference() + " ----------------------------------------  ");
            currentInfrastructureAgent.run(); //Launch the behavior of the agent in its PERCEPTION State
            currentInfrastructureAgent.run(); //Launch the behavior of the agent in its DECISION State
            currentInfrastructureAgent.run(); //Launch the behavior of the agent in its ACTION State
            listAgentsToSchedule.remove(currentInfrastructureAgent);
            listAgentsToSchedule.add(currentInfrastructureAgent);
            currentCycle++;
        }
    }


    public List<InfrastructureAgent> getListAgentsToSchedule() {
        return listAgentsToSchedule;
    }


    @Override
    public void changeSpeed(EnumSpeed speed) {
        switch (speed) {
            case CENT:
                this.speed = 10;
                break;
            case SOIXANTE_QUINZE:
                this.speed = 15;
                break;
            case CINQUANTE:
                this.speed = 20;
                break;
            case VINGT_CINQ:
                this.speed = 50;
                break;
            case DIX:
                this.speed = 100;
                break;
        }
    }

    @Override
    public void stopScheduling() {
    }

    @Override
    public void addSchedulingListener(SchedulerListener schedulerListener) {
        schedulerListeners.add(schedulerListener);
    }

    @Override
    public void addAgent(InfrastructureAgent infrastructureAgent) {
        listAgentsToSchedule.add(infrastructureAgent);
    }

    @Override
    public void deleteAgent(InfrastructureAgent infrastructureAgent) {
        listAgentsToSchedule.remove(infrastructureAgent);
    }

//    /**
//     * Put pause to the scheduling process of the agents
//     */
//    @Override
//    public void pauseScheduling() {
//        this.run = false;
//    }
//
//    /**
//     * Resume the execution of the scheduling process of the agents
//     */
//    @Override
//    public void rerunScheduling() {
//        this.run = true;
//    }

    /**
     * Set the value of the number of agent cycle per OCE Cycle
     * @param maxCycleAgent
     */
    @Override
    public void setMaxCycleAgent(int maxCycleAgent) {
        this.maxCycleAgent = maxCycleAgent;
    }

    /**
     * restart the OCE Cycle (initialize the current cycle to 0)
     */
    @Override
    public void resetCurrentCycleAgent() {
        this.currentAgentCycle = 0;
    }
}
