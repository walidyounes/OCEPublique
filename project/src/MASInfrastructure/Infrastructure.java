/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Annuaire.Annuaire;
import MASInfrastructure.Annuaire.IAnnuaire;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Fabrique.Fabrique;
import MASInfrastructure.Fabrique.ICreationAgent;
import MASInfrastructure.Fabrique.ISuicideService;
import MASInfrastructure.Ordonnanceur.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * MASInfrastructure fournit le service ICreationAgent(création d'un agent)
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
    public void seSuicider(AgentReference agent) {
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

    /*	public void sendMessage(AgentReference expediteur, AgentReference destinataire, IMessage IMessage) {
            annuaire.sendMessage(expediteur, destinataire, IMessage);
        }

        public void sendMessageBroadcast(AgentReference expediteur, IMessage IMessage) {
            annuaire.sendMessageBroadcast(expediteur, IMessage);
        }
    */
    @Override
    public void sendMessageBroadcast(IMessage message) {
        annuaire.sendMessageBroadcast(message);
    }

    @Override
    public void sendMessage(IMessage message) {
        annuaire.sendMessage(message);
    }

    @Override
    public Optional<IMessage> receiveMessage(AgentReference reciever) {
        return annuaire.receiveMessage(reciever);
    }

    @Override
    public ArrayList<IMessage> receiveMessages(AgentReference reciever) {
        return annuaire.receiveMessages(reciever);
    }

    @Override
    public Agent creer(OCService attachedService, LifeCycle lifeCycle) {
        Agent agent = fabrique.creer(attachedService, lifeCycle);
        // ordonnanceur.OrdagentAjoute(agent); //walid : ToDo Pourquoi avoir supprimer cette ligne ?? --> car l'ajout dans l'ordonnanceur se fait par fabrique
        return agent;
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
