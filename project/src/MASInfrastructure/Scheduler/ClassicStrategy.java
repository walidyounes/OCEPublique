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

    private List<InfrastructureAgent> listOrdonnancement; // list of observed agents
    private List<SchedulerListener> listListenerPourOrdonnanceur; // list of observers
    private int vitesse;
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
        listOrdonnancement = listInfrastructureAgents;
        this.run= true;
        this.currentAgentCycle = 0;
        this.maxCycleAgent = defaultMaxCycleAgent;
        this.stop = false;
        listListenerPourOrdonnanceur = listListenerActuels;
        changerVitesse(EnumSpeed.CENT);
    }

/*	@Override
	public void startScheduling() {
		run = true;
		OCE.InfrastructureAgent agentCourant;
		while (run) {
			agentCourant = listOrdonnancement.get(0);
			LifeCycle(agentCourant.getInfraAgentReference(), agentCourant.getEtatInitial());
			listOrdonnancement.remove(agentCourant);
			listOrdonnancement.add(agentCourant);

			System.out.println("listOrdonnancement" + getListOrdonnancement());
		}
	}
*/

    @Override
    public void ordonnancer() {
        //Initialize the parameters for the execution
        this.run = true;
        this.stop = false;
        this.currentAgentCycle = 0;

        InfrastructureAgent currentInfrastructureAgent;
        while(!stop) {
            synchronized (this) {
                while (this.currentAgentCycle < this.maxCycleAgent) {
                    currentInfrastructureAgent = listOrdonnancement.get(0);
                    OCELogger.log(Level.INFO, " *********************************** Cycle of the Agent = " + currentInfrastructureAgent.getInfraAgentReference() + " ***********************************");
                    //LifeCycle(currentInfrastructureAgent.getInfraAgentReference(), currentInfrastructureAgent.getEtatInitial()); - todo walid : pour le moement je ne sais pas c'est qui les listeners pour les avertir du changement d'état
                    currentInfrastructureAgent.run(); // change the state of the agent
                    listOrdonnancement.remove(currentInfrastructureAgent);
                    listOrdonnancement.add(currentInfrastructureAgent);

                    this.currentAgentCycle++;
                }
            }
        }
    }

    public List<InfrastructureAgent> getListOrdonnancement() {
        return listOrdonnancement;
    }

    /*
        private void LifeCycle(InfraAgentReference agentCourantReference, EtatAbstract etatAbstract) {
            listListenerPourOrdonnanceur.forEach(
                    ordonnanceurListener -> ordonnanceurListener.changementEtat(agentCourantReference, etatAbstract));
            try {
                TimeUnit.MICROSECONDS.sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            etatAbstract.executer().ifPresent(iEtat -> LifeCycle(agentCourantReference, iEtat));
        }
    */
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

    public List<SchedulerListener> getListListenerPourOrdonnanceur() {
        return listListenerPourOrdonnanceur;
    }

    @Override
    public List<InfrastructureAgent> arreterOrdonnancement() {
        this.stop = true;
        return listOrdonnancement;
    }

    @Override
    public void addOrdonnaceurListener(SchedulerListener schedulerListener) {
        listListenerPourOrdonnanceur.add(schedulerListener);
    }

    @Override
    public void agentAjoute(InfrastructureAgent infrastructureAgent) {
        listOrdonnancement.add(infrastructureAgent);
       // System.out.println("listOrdonnancement****" + getListOrdonnancement());
    }

    public List<IReferenceAgentListener> getReferenceAgentListeners() {
        return null;
    }

    @Override
    public void agentRetire(InfrastructureAgent infrastructureAgent) {
        listOrdonnancement.remove(infrastructureAgent);
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
