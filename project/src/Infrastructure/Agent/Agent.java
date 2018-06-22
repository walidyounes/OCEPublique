/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Agent;


import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

public class Agent {
    private final ReferenceAgent referenceAgent;
    // private final EtatAbstract etatInitial; --> Walid : Pas besoin de cet attribut il est substitué par la classe LifeCyrcle
    //private OCService serviceGere; // Le service géré par l'agent
    private LifeCyrcle lifeCyrcle;

    public Agent(IEtat etatInit) {
        //this.etatInitial = etatInitial;
        this.referenceAgent = new ReferenceAgent();
        //this.etatInitial.setReferenceAgent(this.referenceAgent);
        //this.serviceGere = serviceGere;
        this.lifeCyrcle = new LifeCyrcle(etatInit, this.referenceAgent);
    }

    public ReferenceAgent getReferenceAgent() {
        return referenceAgent;
    }

    public IEtat getEtat() {
        return lifeCyrcle.getCurrentState();
    }

    public void changerEtat() {
        this.lifeCyrcle.run();
    }

}
