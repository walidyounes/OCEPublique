/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.State.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StateByStateStrategy implements ISchedulingStrategies {

    private List<InfrastructureAgent> listAgentsToSchedule; // liste d'ordonnancement des agents
    private List<SchedulerListener> listListenerPourOrdonnanceur;
    private Map<InfrastructureAgent, IState> listEtatAgent;
    private int speed;
    private boolean run = true;
    private int currentAgentCycle;
    private int maxCycleAgent;


    public StateByStateStrategy(List<InfrastructureAgent> listInfrastructureAgent, List<SchedulerListener> listListenerActuels) {
        listAgentsToSchedule = listInfrastructureAgent;
        listListenerPourOrdonnanceur = listListenerActuels;
        listEtatAgent = new HashMap<>();
        this.currentAgentCycle = 0;
        this.maxCycleAgent = 300;
        this.run = true;
        // listAgentsToSchedule.forEach(agent -> listEtatAgent.put(agent, agent.getEtatInitial())); // todo : Walid
        changeSpeed(EnumSpeed.CENT);
    }

    @Override
    public void startScheduling() {
        run = true;
        InfrastructureAgent infrastructureAgentCourant;
        IState etatCourant;
        while (run) {
            infrastructureAgentCourant = listAgentsToSchedule.get(0);
            etatCourant = listEtatAgent.get(infrastructureAgentCourant); //TOdo delete this line
            try {
                TimeUnit.MICROSECONDS.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // changerEtatAgent(infrastructureAgentCourant, etatCourant.executer().orElseGet(infrastructureAgentCourant::getEtatInitial)); - todo Walid
            listAgentsToSchedule.remove(infrastructureAgentCourant);
            listAgentsToSchedule.add(infrastructureAgentCourant);
            System.out.println("listAgentsToSchedule" + getListAgentsToSchedule());
            System.out.println("ListEtatAgent" + getListEtatAgent());

        }
    }

    public Map<InfrastructureAgent, IState> getListEtatAgent() {
        return listEtatAgent;
    }

    public List<InfrastructureAgent> getListAgentsToSchedule() {
        return listAgentsToSchedule;
    }

    private void changerEtatAgent(InfrastructureAgent infrastructureAgentCourant, IState etatAbstract) {
        listListenerPourOrdonnanceur.forEach(schedulerListener -> schedulerListener
                .changementEtat(infrastructureAgentCourant.getInfraAgentReference(), etatAbstract));
        listEtatAgent.put(infrastructureAgentCourant, etatAbstract);
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
        run = false;
    }

    @Override
    public void addSchedulingListener(SchedulerListener schedulerListener) {
        listListenerPourOrdonnanceur.add(schedulerListener);
    }

    @Override
    public void addAgent(InfrastructureAgent infrastructureAgent) {
        listAgentsToSchedule.add(infrastructureAgent);
        listEtatAgent.put(infrastructureAgent, infrastructureAgent.getState());
    }

    @Override
    public void deleteAgent(InfrastructureAgent infrastructureAgent) {
        listAgentsToSchedule.remove(infrastructureAgent);
        listEtatAgent.remove(infrastructureAgent);
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
