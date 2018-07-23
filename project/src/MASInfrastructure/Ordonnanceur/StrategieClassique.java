/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Ordonnanceur;

import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Annuaire.IReferenceAgentListener;

import java.util.List;

public class StrategieClassique implements IStratOrdonnanceur {

    private List<InfraAgent> listOrdonnancement; // les agents observés
    private List<OrdonnanceurListener> listListenerPourOrdonnanceur; // Liste
    // des
    // observateurs
    private int vitesse;
    private boolean run = true;
    // Etat1 etatInitial = new Etat1();
    // OCE.InfraAgent agent1 = new OCE.InfraAgent(etatInitial);
    // OCE.InfraAgent agent2 = new OCE.InfraAgent(etatInitial);
    // OCE.InfraAgent agent3 = new OCE.InfraAgent(etatInitial);

    public StrategieClassique(List<InfraAgent> listInfraAgents, List<OrdonnanceurListener> listListenerActuels) {
        listOrdonnancement = listInfraAgents;
        /*
		 * listInfraAgents.add(agent1); listInfraAgents.add(agent2);
		 * listInfraAgents.add(agent3);
		 */
        listListenerPourOrdonnanceur = listListenerActuels;
        changerVitesse(EnumVitesse.CENT);
    }

/*	@Override
	public void ordonnancer() {
		run = true;
		OCE.InfraAgent agentCourant;
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
        // Todo a compléter
        run = true;
        InfraAgent infraAgentCourant;
        /// while (run) {
        int i = 0; // Walid : fixer le itérations
        while (i < 18) {
            infraAgentCourant = listOrdonnancement.get(0);
            //LifeCycle(infraAgentCourant.getInfraAgentReference(), infraAgentCourant.getEtatInitial()); - todo walid : pour le moement je ne sais pas c'est qui les listeners pour les avertir du changement d'état
            infraAgentCourant.run(); // walid : On actionne le changment d'etat de l'agent
            listOrdonnancement.remove(infraAgentCourant);
            listOrdonnancement.add(infraAgentCourant);

            //System.out.println("listOrdonnancement" + getListOrdonnancement());
            i++;
        }
    }

    public List<InfraAgent> getListOrdonnancement() {
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
    public List<InfraAgent> arreterOrdonnancement() {
        run = false;
        return listOrdonnancement;
    }

    @Override
    public void addOrdonnaceurListener(OrdonnanceurListener ordonnanceurListener) {
        listListenerPourOrdonnanceur.add(ordonnanceurListener);
    }

    @Override
    public void agentAjoute(InfraAgent infraAgent) {
        listOrdonnancement.add(infraAgent);
    }

    public List<IReferenceAgentListener> getReferenceAgentListeners() {
        return null;
    }

    @Override
    public void agentRetire(InfraAgent infraAgent) {
        listOrdonnancement.remove(infraAgent);
    }
}
