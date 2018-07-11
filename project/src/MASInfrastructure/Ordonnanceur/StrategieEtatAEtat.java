/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Etat.IEtat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StrategieEtatAEtat implements IStratOrdonnanceur {

    private List<Agent> listOrdonnancement; // liste d'ordonnancement des agents
    private List<OrdonnanceurListener> listListenerPourOrdonnanceur;
    private Map<Agent, IEtat> listEtatAgent;
    private int vitesse;
    private boolean run = true;
    /*Etat1 etatInitial = new Etat1();
	OCE.Agent agent1 = new OCE.Agent(etatInitial);
	OCE.Agent agent2 = new OCE.Agent(etatInitial);
	OCE.Agent agent3 = new OCE.Agent(etatInitial);*/

    public StrategieEtatAEtat(List<Agent> listAgent, List<OrdonnanceurListener> listListenerActuels) {
        listOrdonnancement = listAgent;
		/*listAgent.add(agent1);
		listAgent.add(agent2);
		listAgent.add(agent3);*/
        listListenerPourOrdonnanceur = listListenerActuels;
        listEtatAgent = new HashMap<>();
        // listOrdonnancement.forEach(agent -> listEtatAgent.put(agent, agent.getEtatInitial())); // todo : Walid
        changerVitesse(EnumVitesse.CENT);

    }

    @Override
    public void ordonnancer() {
        run = true;
        Agent agentCourant;
        IEtat etatCourant;
        while (run) {
            agentCourant = listOrdonnancement.get(0);
            etatCourant = listEtatAgent.get(agentCourant); //TOdo delete this line
            try {
                TimeUnit.MICROSECONDS.sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // changerEtatAgent(agentCourant, etatCourant.executer().orElseGet(agentCourant::getEtatInitial)); - todo Walid
            listOrdonnancement.remove(agentCourant);
            listOrdonnancement.add(agentCourant);
            System.out.println("listOrdonnancement" + getListOrdonnancement());
            System.out.println("ListEtatAgent" + getListEtatAgent());

        }
    }

    public Map<Agent, IEtat> getListEtatAgent() {
        return listEtatAgent;
    }

    public List<Agent> getListOrdonnancement() {
        return listOrdonnancement;
    }

    private void changerEtatAgent(Agent agentCourant, IEtat etatAbstract) {
        listListenerPourOrdonnanceur.forEach(ordonnanceurListener -> ordonnanceurListener
                .changementEtat(agentCourant.getAgentReference(), etatAbstract));
        listEtatAgent.put(agentCourant, etatAbstract);
    }

    @Override
    public void changerVitesse(EnumVitesse vitesse) {
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
    public List<Agent> arreterOrdonnancement() {
        run = false;
        return listOrdonnancement;
    }

    @Override
    public void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener) {
        listListenerPourOrdonnanceur.add(ordonnanceurListener);
    }

    @Override
    public void agentAjoute(Agent agent) {
        listOrdonnancement.add(agent);
        listEtatAgent.put(agent, agent.getState());
    }

    @Override
    public void agentRetire(Agent agent) {
        listOrdonnancement.remove(agent);
        listEtatAgent.remove(agent);
    }

}
