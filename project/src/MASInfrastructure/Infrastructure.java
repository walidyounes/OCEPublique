/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure;

import AmbientEnvironment.OCPlateforme.OCService;
import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Agent.InfraAgentReference;
import MASInfrastructure.Directory.AgentDirectory;
import MASInfrastructure.Directory.IAgentDirectory;
import MASInfrastructure.Communication.ICommunication;
import MASInfrastructure.Communication.IMessage;
import MASInfrastructure.State.LifeCycle;
import MASInfrastructure.Factory.IInfraAgentFactory;
import MASInfrastructure.Factory.ISuicideService;
import MASInfrastructure.Factory.InfraAgentFactory;
import MASInfrastructure.Scheduler.*;
import OCE.Agents.OCEAgent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * MASInfrastructure
 */
public class Infrastructure implements IInfraAgentFactory, ISuicideService, ICommunication, IScheduler, PropertyChangeListener {

    private InfraAgentFactory infraAgentFactory;
    private IScheduler scheduler;
    private IAgentDirectory annuaire;

    public Infrastructure() {
        scheduler = new Scheduler(new ClassicStrategy(new ArrayList<>(), new ArrayList<>()));
        annuaire = AgentDirectory.getInstance();
        infraAgentFactory = new InfraAgentFactory(annuaire, scheduler);
    }

    @Override
    public void suicide(InfrastructureAgent agent) {
        infraAgentFactory.suicide(agent);
    }

    @Override
    public void startScheduling() {
        scheduler.startScheduling();
    }

    @Override
    public void changeSpeed(EnumSpeed newSpeed) {
        scheduler.changeSpeed(newSpeed);
    }

    @Override
    public void changeSchedulingStrategy(ISchedulingStrategies schedulingStrategy) {
        scheduler.changeSchedulingStrategy(schedulingStrategy);
    }

    @Override
    public List<InfrastructureAgent> stopScheduling() {
        return scheduler.stopScheduling();
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
    public InfrastructureAgent createInfrastructureAgent(OCService attachedService, LifeCycle lifeCycle, ICommunication myMailBoxManager) {
        InfrastructureAgent infrastructureAgent = infraAgentFactory.createInfrastructureAgent(attachedService, lifeCycle, myMailBoxManager);
        // scheduler.OrdagentAjoute(infrastructureAgent); //walid : ToDo Pourquoi avoir supprimer cette ligne ?? --> car l'ajout dans l'scheduler se fait par infraAgentFactory
        return infrastructureAgent;
    }

    public IAgentDirectory getAnnuaire() {
        return annuaire;
    }

    public IScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void addAgentToScheduler(InfrastructureAgent infrastructureAgent) {

        scheduler.addAgentToScheduler(infrastructureAgent);
    }

    @Override
    public void deleteAgentFromScheduler(InfrastructureAgent infrastructureAgent) {

        scheduler.deleteAgentFromScheduler(infrastructureAgent);
    }

    /**
     * Put pause to the scheduling process of the agents
     */
    @Override
    public void pauseScheduling() {
        this.scheduler.pauseScheduling();
    }

    /**
     * Resume the execution of the scheduling process of the agents
     */
    @Override
    public void restartScheduling() {
        this.scheduler.restartScheduling();
    }

    /**
     * Set the value of the number of agent cycle per OCE Cycle
     * @param maxCycleAgent
     */
    @Override
    public void setMaxCycleAgent(int maxCycleAgent) {
       this.scheduler.setMaxCycleAgent(maxCycleAgent);
    }

    /**
     * restart the OCE Cycle (initialize the current cycle to 0)
     */
    @Override
    public void resetCurrentCycleAgent() {
        this.scheduler.resetCurrentCycleAgent();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Depending on the name of the property we do the corresponding operation
//        switch (evt.getPropertyName()){
//            case "DeleteAgent" :
                //Get the reference of the deleted Agent
                InfrastructureAgent agentToDelete = ((OCEAgent)evt.getNewValue()).getMyInfrastructureAgent();
                System.out.println("Event detected : Deleting agent from the Infrastructure  " + agentToDelete);
                //Order the delete
                this.suicide(agentToDelete);
//                break;
//        }
    }
}
