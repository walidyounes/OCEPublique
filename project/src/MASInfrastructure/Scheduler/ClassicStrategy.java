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
    private boolean run;
    private boolean stop;
    private final int defaultMaxCycleAgent = 400;

    /**
     *
     * @param listInfrastructureAgents
     * @param listListenerActuels
     */
    public ClassicStrategy(List<InfrastructureAgent> listInfrastructureAgents, List<SchedulerListener> listListenerActuels) {
        listAgentsToSchedule = listInfrastructureAgents;
        this.run= true;
        this.currentAgentCycle = 0;
        this.maxCycleAgent = defaultMaxCycleAgent;
        this.stop = false;
        schedulerListeners = listListenerActuels;
        changeSpeed(EnumSpeed.CENT);
    }

/*	@Override
	public void startScheduling() {
		run = true;
		OCE.InfrastructureAgent agentCourant;
		while (run) {
			agentCourant = listAgentsToSchedule.get(0);
			LifeCycle(agentCourant.getInfraAgentReference(), agentCourant.getEtatInitial());
			listAgentsToSchedule.remove(agentCourant);
			listAgentsToSchedule.add(agentCourant);

			System.out.println("listAgentsToSchedule" + getListAgentsToSchedule());
		}
	}
*/

    @Override
    public void startScheduling() {
        //Initialize the parameters for the execution
        this.run = true;
        this.stop = false;
        this.currentAgentCycle = 0;

        InfrastructureAgent currentInfrastructureAgent;
        while(!stop) {
            synchronized (this) {
                while (this.currentAgentCycle < this.maxCycleAgent) {
                    currentInfrastructureAgent = listAgentsToSchedule.get(0);
                    OCELogger.log(Level.INFO, " *********************************** Cycle of the Agent = " + currentInfrastructureAgent.getInfraAgentReference() + " ***********************************");
                    //LifeCycle(currentInfrastructureAgent.getInfraAgentReference(), currentInfrastructureAgent.getEtatInitial()); - todo walid : pour le moement je ne sais pas c'est qui les listeners pour les avertir du changement d'état
                    currentInfrastructureAgent.run(); // change the state of the agent
                    listAgentsToSchedule.remove(currentInfrastructureAgent);
                    listAgentsToSchedule.add(currentInfrastructureAgent);

                    this.currentAgentCycle++;
                }
            }
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
    public List<InfrastructureAgent> stopScheduling() {
        this.stop = true;
        return listAgentsToSchedule;
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

    /**
     * Put pause to the scheduling process of the agents
     */
    @Override
    public void pauseScheduling() {
        this.run = false;
    }

    /**
     * Resume the execution of the scheduling process of the agents
     */
    @Override
    public void rerunScheduling() {
        this.run = true;
    }

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
