/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Annuaire.IReferenceAgentListener;

import java.util.List;

public class StrategieClassique implements IStratOrdonnanceur {

    private List<Agent> listOrdonnancement; // les agents observés
    private List<OrdonnanceurListener> listListenerPourOrdonnanceur; // Liste
    // des
    // observateurs
    private int vitesse;
    private boolean run = true;
    // Etat1 etatInitial = new Etat1();
    // OCE.Agent agent1 = new OCE.Agent(etatInitial);
    // OCE.Agent agent2 = new OCE.Agent(etatInitial);
    // OCE.Agent agent3 = new OCE.Agent(etatInitial);

    public StrategieClassique(List<Agent> listAgents, List<OrdonnanceurListener> listListenerActuels) {
        listOrdonnancement = listAgents;
        /*
		 * listAgents.add(agent1); listAgents.add(agent2);
		 * listAgents.add(agent3);
		 */
        listListenerPourOrdonnanceur = listListenerActuels;
        changerVitesse(EnumVitesse.CENT);
    }

/*	@Override
	public void ordonnancer() {
		run = true;
		OCE.Agent agentCourant;
		while (run) {
			agentCourant = listOrdonnancement.get(0);
			LifeCycle(agentCourant.getAgentReference(), agentCourant.getEtatInitial());
			listOrdonnancement.remove(agentCourant);
			listOrdonnancement.add(agentCourant);

			System.out.println("listOrdonnancement" + getListOrdonnancement());
		}
	}
*/

    @Override
    public void ordonnancer() {
        // Todo a compléter
        run = true;
        Agent agentCourant;
        /// while (run) {
        int i = 0; // Walid : fixer le itérations
        while (i < 6) {
            agentCourant = listOrdonnancement.get(0);
            //LifeCycle(agentCourant.getAgentReference(), agentCourant.getEtatInitial()); - todo walid : pour le moement je ne sais pas c'est qui les listeners pour les avertir du changement d'état
            agentCourant.run(); // walid : On actionne le changment d'etat de l'agent
            listOrdonnancement.remove(agentCourant);
            listOrdonnancement.add(agentCourant);

            //System.out.println("listOrdonnancement" + getListOrdonnancement());
            i++;
        }
    }

    public List<Agent> getListOrdonnancement() {
        return listOrdonnancement;
    }

    /*
        private void LifeCycle(AgentReference agentCourantReference, EtatAbstract etatAbstract) {
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

    public List<OrdonnanceurListener> getListListenerPourOrdonnanceur() {
        return listListenerPourOrdonnanceur;
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
    }

    public List<IReferenceAgentListener> getReferenceAgentListeners() {
        return null;
    }

    @Override
    public void agentRetire(Agent agent) {
        listOrdonnancement.remove(agent);
    }
}
