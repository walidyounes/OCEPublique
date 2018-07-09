/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Fabrique;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.Agent;
import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Annuaire.IAnnuaire;
import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;
import Infrastructure.Ordonnanceur.Ordonnanceur;

public class Fabrique implements ICreationAgent, ISuicideService {
    private final IAnnuaire annuaire;
    private final Ordonnanceur ordonnanceur;

    public Fabrique(IAnnuaire annuaire, Ordonnanceur ordonnanceur) {
        this.annuaire = annuaire;
        this.ordonnanceur = ordonnanceur;
    }

    @Override
    public Agent creer(OCService attachedService, LifeCyrcle lifeCyrcle) {
        Agent agent = new Agent(attachedService, lifeCyrcle);
        annuaire.addAgent(agent);
        ordonnanceur.ordagentAjoute(agent);
        return agent;
    }

    @Override
    public void seSuicider(ReferenceAgent referenceAgent) {
        Agent agentSuicide = annuaire.getAgentByRef(referenceAgent); // walid : On récupère l'agent correspondant pour le supprimer de l'ordonnanceur
        annuaire.removeAgent(referenceAgent);
        ordonnanceur.OrdagentRetire(agentSuicide); //le paramètre agent?? --> c'est regler -walid
    }

}
