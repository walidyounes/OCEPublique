/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import Logger.OCELogger;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Directory.IReferenceAgentListener;

import java.util.List;
import java.util.logging.Level;

public class ClassicStrategy implements ISchedulingStrategies {

    private List<InfrastructureAgent> listAgentsToSchedule; // list of observed agents
    private List<SchedulerListener> schedulerListeners; // list of observers
    private int speed;
    private int currentAgentCycle;
    private int maxCycleAgent;
    private boolean isRunning;
    private final int defaultMaxCycleAgent = 400;

    /**
     *
     * @param listInfrastructureAgents
     * @param listListenerActuels
     */
    public ClassicStrategy(List<InfrastructureAgent> listInfrastructureAgents, List<SchedulerListener> listListenerActuels) {
        listAgentsToSchedule = listInfrastructureAgents;
        this.currentAgentCycle = 0;
        this.maxCycleAgent = defaultMaxCycleAgent;
        this.isRunning = true;
        schedulerListeners = listListenerActuels;
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
                        currentInfrastructureAgent.run(); // change the state of the agent
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
        //We calculate the bound of cycles by multiplying the number of cycles by 3 cause each agents will do "Perception, Decision, Action"
        int numberCyclesBound = numberCycles * 3;

        OCELogger.log(Level.INFO, "----------------------------------------------------------------------------------------- STARTING SPECIAL SCHEDULING -------------------------------------------------------------------------------------------- \n");
        InfrastructureAgent currentInfrastructureAgent;

        while (currentCycle < numberCyclesBound && listAgentsToSchedule.size()>0 ) {
            currentInfrastructureAgent = listAgentsToSchedule.get(0);
            OCELogger.log(Level.INFO, " -----------------------------------  Cycle of the Agent = " + currentInfrastructureAgent.getInfraAgentReference() + " ----------------------------------------  ");
            currentInfrastructureAgent.run(); //Launch the behavior of the agent in its current state
            listAgentsToSchedule.remove(currentInfrastructureAgent);
            listAgentsToSchedule.add(currentInfrastructureAgent);

            currentCycle++;
        }
    }

    public List<InfrastructureAgent> getListAgentsToSchedule() {
        return listAgentsToSchedule;
    }

    /*
        private void LifeCycle(InfraAgentReference agentCourantReference, EtatAbstract etatAbstract) {
            schedulerListeners.forEach(
                    ordonnanceurListener -> ordonnanceurListener.changementEtat(agentCourantReference, etatAbstract));
            try {
                TimeUnit.MICROSECONDS.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            etatAbstract.executer().ifPresent(iEtat -> LifeCycle(agentCourantReference, iEtat));
        }
    */

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

    public List<SchedulerListener> getSchedulerListeners() {
        return schedulerListeners;
    }

    @Override
    public void stopScheduling() {
        synchronized (this) {
            this.isRunning = false;
        }
    }

    @Override
    public void addSchedulingListener(SchedulerListener schedulerListener) {
        schedulerListeners.add(schedulerListener);
    }

    @Override
    public void addAgent(InfrastructureAgent infrastructureAgent) {
        listAgentsToSchedule.add(infrastructureAgent);
       // System.out.println("listAgentsToSchedule****" + getListAgentsToSchedule());
    }

    public List<IReferenceAgentListener> getReferenceAgentListeners() {
        return null;
    }

    @Override
    public void deleteAgent(InfrastructureAgent infrastructureAgent) {
        System.out.println(" Deleting from the scheduling strategy the agent = " + infrastructureAgent.toString());
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
        OCELogger.log(Level.INFO,"Changement du nombre de cycles agent par cycle moteur, nouvelle valeur = "+ maxCycleAgent);
        this.maxCycleAgent = maxCycleAgent;
    }

    /**
     * restart the OCE Cycle (initialize the current cycle to 0)
     */
    @Override
    public void resetCurrentCycleAgent() {
        synchronized (this){
            this.currentAgentCycle = 0;
        }
    }
}
