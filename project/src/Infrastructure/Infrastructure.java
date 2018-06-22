/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure;

import Infrastructure.Agent.Agent;
import Infrastructure.Agent.ReferenceAgent;
import Infrastructure.Annuaire.Annuaire;
import Infrastructure.Annuaire.IAnnuaire;
import Infrastructure.Communication.ICommunication;
import Infrastructure.Communication.IMessageAgent;
import Infrastructure.Etat.IEtat;
import Infrastructure.Fabrique.Fabrique;
import Infrastructure.Fabrique.ICreationAgent;
import Infrastructure.Fabrique.ISuicideService;
import Infrastructure.Ordonnanceur.IOrdonnanceur;
import Infrastructure.Ordonnanceur.IStratOrdonnanceur;
import Infrastructure.Ordonnanceur.Ordonnanceur;
import Infrastructure.Ordonnanceur.StrategieClassique;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Modified by Sondes
 *
 * @author Lenovo
 * <p>
 * Infrastructure fournit le service ICreationAgent(création d'un agent)
 */
public class Infrastructure implements ICreationAgent, ISuicideService, ICommunication, IOrdonnanceur {

    private Fabrique fabrique;
    private Ordonnanceur ordonnanceur;// pourquoi pas IOrdonnanceur ?
    private IAnnuaire annuaire;

    public Infrastructure() {
        ordonnanceur = new Ordonnanceur(new StrategieClassique(new ArrayList<>(), new ArrayList<>()));
        annuaire = Annuaire.getInstance();
        fabrique = new Fabrique(annuaire, ordonnanceur);
    }

    @Override
    public void seSuicider(ReferenceAgent agent) {
        fabrique.seSuicider(agent);
    }

    @Override
    public void ordonnancer() {
        ordonnanceur.ordonnancer();
    }

    @Override
    public void changerVitesse(EnumVitesse vitesse) {
        ordonnanceur.changerVitesse(vitesse);
    }

    @Override
    public void changerOrdonnancement(IStratOrdonnanceur stratOrdonnanceur) {
        ordonnanceur.changerOrdonnancement(stratOrdonnanceur);
    }

    @Override
    public List<Agent> arreterOrdonnancement() {
        return ordonnanceur.arreterOrdonnancement();
    }

    /*	public void envoyerMessage(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessageAgent IMessageAgent) {
            annuaire.envoyerMessage(expediteur, destinataire, IMessageAgent);
        }

        public void diffuserMessage(ReferenceAgent expediteur, IMessageAgent IMessageAgent) {
            annuaire.diffuserMessage(expediteur, IMessageAgent);
        }
    */
    @Override
    public void diffuserMessage(IMessageAgent message) {
        //ToDo : check if this work; --> it's working
        annuaire.diffuserMessage(message);
    }

    @Override
    public void envoyerMessage(IMessageAgent message) {
        //ToDo : check if this work; --> it's working
        annuaire.envoyerMessage(message);
    }

    @Override
    public Optional<IMessageAgent> recevoirMessage(ReferenceAgent destinataire) {
        return annuaire.recevoirMessage(destinataire);
    }

    @Override
    public List<IMessageAgent> recevoirMessages(ReferenceAgent destinataire) {
        return annuaire.recevoirMessages(destinataire);
    }

    @Override
    public ReferenceAgent creer(IEtat etatInit) {
        ReferenceAgent ref = fabrique.creer(etatInit);
        // ordonnanceur.OrdagentAjoute(agent); //walid : ToDo Pourquoi avoir supprimer cette ligne ?? --> car l'ajout dans l'ordonnanceur se fait par fabrique
        return (ref);
    }

    public IAnnuaire getAnnuaire() {
        return annuaire;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

    @Override
    public void ordagentAjoute(Agent agent) {

        ordonnanceur.ordagentAjoute(agent);
    }

    @Override
    public void OrdagentRetire(Agent agent) {

        ordonnanceur.OrdagentRetire(agent);
    }

}
