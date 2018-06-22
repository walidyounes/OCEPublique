/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Agent;


import sma.infrastructure.etat.CycleDeVie;
import sma.infrastructure.etat.IEtat;

public class Agent {
    private final ReferenceAgent referenceAgent;
    // private final EtatAbstract etatInitial; --> Walid : Pas besoin de cet attribut il est substitué par la classe cycleDeVie
    //private OCService serviceGere; // Le service géré par l'agent
    private CycleDeVie cycleDeVie;

    public Agent(IEtat etatInit) {
        //this.etatInitial = etatInitial;
        this.referenceAgent = new ReferenceAgent();
        //this.etatInitial.setReferenceAgent(this.referenceAgent);
        //this.serviceGere = serviceGere;
        this.cycleDeVie = new CycleDeVie(etatInit, this.referenceAgent);
    }

    public ReferenceAgent getReferenceAgent() {
        return referenceAgent;
    }

    public IEtat getEtat() {
        return cycleDeVie.getEtatCourant();
    }

    public void changerEtat() {
        this.cycleDeVie.changerEtat();
    }

}
