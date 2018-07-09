/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium.composants;

import Environment.OCPlateforme.OCService;
import Infrastructure.Agent.ReferenceAgent;
import OCE.Medium.services.IEnregistrement;
import OCE.Medium.services.IListeAgents;

import java.util.HashMap;

public class Record implements IListeAgents, IEnregistrement {
    /**
     * Liste des agents disponibles associées à leurs services.
     */
    private HashMap<ReferenceAgent, OCService> agents = new HashMap<>();


    /**
     * Ajoute un agent disponible à la liste.
     *
     * @param agent   nouvel agent
     * @param service service de l'agent
     */
    @Override
    public void addAgent(ReferenceAgent agent, OCService service) {
        this.agents.put(agent, service);

    }

    /**
     * Enlève l'agent "agent" de la liste des agents disponibles.
     *
     * @param agent agent disparu
     */
    @Override
    public void removeAgent(ReferenceAgent agent) {

        this.agents.remove(agent);
    }

    @Override
    public HashMap<ReferenceAgent, OCService> getListAgents() {
        return this.agents;
    } // Todo : a supprimer ça ne sert à rien c'est pas comme ça qu'elle devrait fonctionner
}
