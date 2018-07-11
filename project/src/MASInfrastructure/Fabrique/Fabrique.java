/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Fabrique;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Annuaire.IAnnuaire;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Ordonnanceur.Ordonnanceur;

public class Fabrique implements ICreationAgent, ISuicideService {
    private final IAnnuaire annuaire;
    private final Ordonnanceur ordonnanceur;

    public Fabrique(IAnnuaire annuaire, Ordonnanceur ordonnanceur) {
        this.annuaire = annuaire;
        this.ordonnanceur = ordonnanceur;
    }

    @Override
    public Agent creer(OCService attachedService, LifeCycle lifeCycle) {
        Agent agent = new Agent(attachedService, lifeCycle);
        annuaire.addAgent(agent);
        ordonnanceur.ordagentAjoute(agent);
        return agent;
    }

    @Override
    public void seSuicider(AgentReference agentReference) {
        Agent agentSuicide = annuaire.getAgentByRef(agentReference); // walid : On récupère l'agent correspondant pour le supprimer de l'ordonnanceur
        annuaire.removeAgent(agentReference);
        ordonnanceur.OrdagentRetire(agentSuicide); //le paramètre agent?? --> c'est regler -walid
    }

}
