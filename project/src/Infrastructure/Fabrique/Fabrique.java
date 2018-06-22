/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Fabrique;

import Infrastructure.Agent.Agent;
import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Annuaire.IAnnuaire;
import Infrastructure.Etat.IEtat;
import Infrastructure.Ordonnanceur.Ordonnanceur;

public class Fabrique implements ICreationAgent, ISuicideService {
    private final IAnnuaire annuaire;
    private final Ordonnanceur ordonnanceur;

    public Fabrique(IAnnuaire annuaire, Ordonnanceur ordonnanceur) {
        this.annuaire = annuaire;
        this.ordonnanceur = ordonnanceur;
    }

    @Override
    public ReferenceAgent creer(IEtat etatInit) {
        Agent agent = new Agent(etatInit);
        annuaire.addAgent(agent);
        ordonnanceur.ordagentAjoute(agent);
        return agent.getReferenceAgent();
    }

    @Override
    public void seSuicider(ReferenceAgent referenceAgent) {
        Agent agentSuicide = annuaire.getAgentByRef(referenceAgent); // walid : On récupère l'agent correspondant pour le supprimer de l'ordonnanceur
        annuaire.removeAgent(referenceAgent);
        ordonnanceur.OrdagentRetire(agentSuicide); //le paramètre agent?? --> c'est regler -walid
    }

}
