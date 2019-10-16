/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Scheduler;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.State.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StateByStateStrategy implements ISchedulingStrategies {

    private List<InfraAgent> listOrdonnancement; // liste d'ordonnancement des agents
    private List<SchedulerListener> listListenerPourOrdonnanceur;
    private Map<InfraAgent, IState> listEtatAgent;
    private int vitesse;
    private boolean run = true;
    private int currentAgentCycle;
    private int maxCycleAgent;


    public StateByStateStrategy(List<InfraAgent> listInfraAgent, List<SchedulerListener> listListenerActuels) {
        listOrdonnancement = listInfraAgent;
        listListenerPourOrdonnanceur = listListenerActuels;
        listEtatAgent = new HashMap<>();
        this.currentAgentCycle = 0;
        this.maxCycleAgent = 300;
        this.run = true;
        // listOrdonnancement.forEach(agent -> listEtatAgent.put(agent, agent.getEtatInitial())); // todo : Walid
        changerVitesse(EnumSpeed.CENT);
    }

    @Override
    public void ordonnancer() {
        run = true;
        InfraAgent infraAgentCourant;
        IState etatCourant;
        while (run) {
            infraAgentCourant = listOrdonnancement.get(0);
            etatCourant = listEtatAgent.get(infraAgentCourant); //TOdo delete this line
            try {
                TimeUnit.MICROSECONDS.sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // changerEtatAgent(infraAgentCourant, etatCourant.executer().orElseGet(infraAgentCourant::getEtatInitial)); - todo Walid
            listOrdonnancement.remove(infraAgentCourant);
            listOrdonnancement.add(infraAgentCourant);
            System.out.println("listOrdonnancement" + getListOrdonnancement());
            System.out.println("ListEtatAgent" + getListEtatAgent());

        }
    }

    public Map<InfraAgent, IState> getListEtatAgent() {
        return listEtatAgent;
    }

    public List<InfraAgent> getListOrdonnancement() {
        return listOrdonnancement;
    }

    private void changerEtatAgent(InfraAgent infraAgentCourant, IState etatAbstract) {
        listListenerPourOrdonnanceur.forEach(schedulerListener -> schedulerListener
                .changementEtat(infraAgentCourant.getInfraAgentReference(), etatAbstract));
        listEtatAgent.put(infraAgentCourant, etatAbstract);
    }

    @Override
    public void changerVitesse(EnumSpeed vitesse) {
        switch (vitesse) {
            case CENT:
                this.vitesse = 10;
                break;
            case SOIXANTE_QUINZE:
                this.vitesse = 15;
                break;
            case CINQUANTE:
                this.vitesse = 20;
                break;
            case VINGT_CINQ:
                this.vitesse = 50;
                break;
            case DIX:
                this.vitesse = 100;
                break;
        }
    }

    @Override
    public List<InfraAgent> arreterOrdonnancement() {
        run = false;
        return listOrdonnancement;
    }

    @Override
    public void addOrdonnaceurListener(SchedulerListener schedulerListener) {
        listListenerPourOrdonnanceur.add(schedulerListener);
    }

    @Override
    public void agentAjoute(InfraAgent infraAgent) {
        listOrdonnancement.add(infraAgent);
        listEtatAgent.put(infraAgent, infraAgent.getState());
    }

    @Override
    public void agentRetire(InfraAgent infraAgent) {
        listOrdonnancement.remove(infraAgent);
        listEtatAgent.remove(infraAgent);
    }

    /**
     * Put pause to the scheduling process of the agents
     */
    @Override
    public void pauseOrdonnancement() {
        this.run = false;
    }

    /**
     * Resume the execution of the scheduling process of the agents
     */
    @Override
    public void repriseOrdonnancement() {
        this.run = true;
    }

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
