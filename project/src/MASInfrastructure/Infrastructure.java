/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfraAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Annuaire.Annuaire;
import MASInfrastructure.Annuaire.IAnnuaire;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import MASInfrastructure.Etat.LifeCycle;
import MASInfrastructure.Fabrique.IInfraAgentFactory;
import MASInfrastructure.Fabrique.ISuicideService;
import MASInfrastructure.Fabrique.InfraAgentFactory;
import MASInfrastructure.Ordonnanceur.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * MASInfrastructure fournit le service IInfraAgentFactory(création d'un agent) et de communication
 */
public class Infrastructure implements IInfraAgentFactory, ISuicideService, ICommunication, IOrdonnanceur {

    private InfraAgentFactory infraAgentFactory;
    private IOrdonnanceur ordonnanceur;
    private IAnnuaire annuaire;

    public Infrastructure() {
        ordonnanceur = new Ordonnanceur(new StrategieClassique(new ArrayList<>(), new ArrayList<>()));
        annuaire = Annuaire.getInstance();
        infraAgentFactory = new InfraAgentFactory(annuaire, ordonnanceur);
    }

    @Override
    public void seSuicider(InfraAgentReference agent) {
        infraAgentFactory.seSuicider(agent);
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
    public List<InfraAgent> arreterOrdonnancement() {
        return ordonnanceur.arreterOrdonnancement();
    }

    /*	public void sendMessage(InfraAgentReference expediteur, InfraAgentReference destinataire, IMessage IMessage) {
            annuaire.sendMessage(expediteur, destinataire, IMessage);
        }

        public void sendMessageBroadcast(InfraAgentReference expediteur, IMessage IMessage) {
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
    public Optional<IMessage> receiveMessage(InfraAgentReference reciever) {
        return annuaire.receiveMessage(reciever);
    }

    @Override
    public ArrayList<IMessage> receiveMessages(InfraAgentReference reciever) {
        return annuaire.receiveMessages(reciever);
    }

    @Override
    public InfraAgent creer(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager) {
        InfraAgent infraAgent = infraAgentFactory.creer(attachedService, lifeCycle, myMailBoxManager);
        // ordonnanceur.OrdagentAjoute(infraAgent); //walid : ToDo Pourquoi avoir supprimer cette ligne ?? --> car l'ajout dans l'ordonnanceur se fait par infraAgentFactory
        return infraAgent;
    }

    public IAnnuaire getAnnuaire() {
        return annuaire;
    }

    public IOrdonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

    @Override
    public void ordagentAjoute(InfraAgent infraAgent) {

        ordonnanceur.ordagentAjoute(infraAgent);
    }

    @Override
    public void OrdagentRetire(InfraAgent infraAgent) {

        ordonnanceur.OrdagentRetire(infraAgent);
    }

}
