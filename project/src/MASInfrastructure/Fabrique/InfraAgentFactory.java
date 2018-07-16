/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Fabrique;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Annuaire.IAnnuaire;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Ordonnanceur.IOrdonnanceur;

public class InfraAgentFactory implements IInfraAgentFactory, ISuicideService {
    private final IAnnuaire annuaire;
    private final IOrdonnanceur ordonnanceur;

    public InfraAgentFactory(IAnnuaire annuaire, IOrdonnanceur ordonnanceur) {
        this.annuaire = annuaire;
        this.ordonnanceur = ordonnanceur;
    }

    @Override
    public InfraAgent creer(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager) {
        InfraAgent infraAgent = new InfraAgent(attachedService, lifeCycle, myMailBoxManager);
        annuaire.addAgent(infraAgent);
        ordonnanceur.ordagentAjoute(infraAgent);
        return infraAgent;
    }

    @Override
    public void seSuicider(InfraAgentReference infraAgentReference) {
        InfraAgent infraAgentSuicide = annuaire.getAgentByRef(infraAgentReference); // walid : On récupère l'agent correspondant pour le supprimer de l'ordonnanceur
        annuaire.removeAgent(infraAgentReference);
        ordonnanceur.OrdagentRetire(infraAgentSuicide); //le paramètre agent?? --> c'est regler -walid
    }

}
