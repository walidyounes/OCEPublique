/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Annuaire;

import MASInfrastructure.Agent.Agent;
import MASInfrastructure.Agent.AgentReference;
import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Annuaire implements IAnnuaire {

    private List<IAgentListener> agentListeners;
    private List<IReferenceAgentListener> referenceAgentListeners;
    private List<IMessageAgentListener> messageAgentListeners;
    private ConcurrentMap<AgentReference, Agent> agents; // contient les references des agents à l'instant t
    private ConcurrentMap<AgentReference, ConcurrentLinkedQueue<IMessage>> agentsMessagesQueues; // contient les references des agents associés aux messages reçus
    private ConcurrentMap<AgentReference, ReadWriteLock> agentsLocks;

    /**
     * Constructeur privé pour implémenter le pattern "singleton"
     */
    private Annuaire() {
        referenceAgentListeners = Collections.synchronizedList(new ArrayList<>());
        agentListeners = Collections.synchronizedList(new ArrayList<>());
        messageAgentListeners = Collections.synchronizedList(new ArrayList<>());
        agents = new ConcurrentHashMap<>();
        agentsMessagesQueues = new ConcurrentHashMap<>();
        agentsLocks = new ConcurrentHashMap<>();
    }

    /**
     * Classe interne pour incarner la téchnique du holder
     */
    private static class AnnuaireHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static Annuaire instance = new Annuaire();
    }

    /**
     * Point d'accès pour l'instance unique du singleton "Annuiare"
     */
    public static Annuaire getInstance() {
        return AnnuaireHolder.instance;
    }

    public ConcurrentMap<AgentReference, Agent> getAgents() {
        return agents;
    }

    @Override
    public void addAgent(Agent agent) {
        agentsLocks.put(agent.getAgentReference(), new ReentrantReadWriteLock());
        lockAgentEcriture(agent.getAgentReference());
        agents.put(agent.getAgentReference(), agent);
        agentsMessagesQueues.put(agent.getAgentReference(), new ConcurrentLinkedQueue<>());
        unlockAgentEcriture(agent.getAgentReference());
        referenceAgentListeners.forEach(agentListener -> agentListener.agentAjoute(agent.getAgentReference()));
        agentListeners.forEach(agentListener -> agentListener.agentAjoute(agent));
    }

    @Override
    public void removeAgent(AgentReference agentReference) {

        agentListeners.forEach(agentListener -> agentListener.agentRetire(agents.get(agentReference)));
        lockAgentEcriture(agentReference);
        agents.remove(agentReference);
        agentsMessagesQueues.remove(agentReference);
        unlockAgentEcriture(agentReference);
        referenceAgentListeners.forEach(agentListener -> agentListener.agentRetire(agentReference));
        System.out.println("La liste de references " + getAgents()); // Trace
    }

    /*	@Override
        public void sendMessage(AgentReference expediteur, AgentReference destinataire, IMessage IMessage) {

            lockAgentLecture(destinataire);
            if (agentsMessagesQueues.containsKey(destinataire)) {
                agentsMessagesQueues.get(destinataire).add(IMessage);
                messageAgentListeners.forEach(messageAgentListener -> messageAgentListener.messageEnvoye(expediteur,
                        destinataire, IMessage));
            }
            unlockAgentLecture(destinataire);

            System.out.println("liste sendMessage" + getAgentsMessagesQueues()); // Trace
        }
    */
    public ConcurrentMap<AgentReference, ConcurrentLinkedQueue<IMessage>> getAgentsMessagesQueues() {
        return agentsMessagesQueues;
    }

    @Override
    public void sendMessage(IMessage message) {

        lockAgentLecture(message.getRecievers().get(0));
        if (agentsMessagesQueues.containsKey(message.getRecievers().get(0))) {
            agentsMessagesQueues.get(message.getRecievers().get(0)).add(message);
            messageAgentListeners.forEach(messageAgentListener -> messageAgentListener.messageEnvoye(message.getEmitter(),
                    message.getRecievers().get(0), message));
        }
        unlockAgentLecture(message.getRecievers().get(0));

        //System.out.println("liste sendMessage" + getAgentsMessagesQueues()); // Trace
    }

    /*@Override
    public void sendMessageBroadcast(AgentReference expediteur, IMessage IMessage) {

        agentsMessagesQueues.keySet().forEach(this::lockAgentLecture);
        agentsMessagesQueues.entrySet().forEach(referenceAgentEntry -> {
            referenceAgentEntry.getValue().add(IMessage);
            notifierMessageAgentListeners(expediteur, IMessage, referenceAgentEntry.getKey());
        });
        agentsMessagesQueues.keySet().forEach(this::unlockAgentLecture);

        System.out.println("liste sendMessageBroadcast" + getAgentsMessagesQueues()); // tarce
    }
*/
    @Override
    public void sendMessageBroadcast(IMessage message) {
        agentsMessagesQueues.keySet().forEach(this::lockAgentLecture);
        agentsMessagesQueues.entrySet().forEach(referenceAgentEntry -> {
            referenceAgentEntry.getValue().add(message);
            notifierMessageAgentListeners(message.getEmitter(), message, referenceAgentEntry.getKey());
        });
        agentsMessagesQueues.keySet().forEach(this::unlockAgentLecture);

        // System.out.println("liste sendMessageBroadcast" + getAgentsMessagesQueues()); // tarce
    }

    private void notifierMessageAgentListeners(AgentReference expediteur, IMessage IMessage,
                                               AgentReference agentReference) {
        messageAgentListeners.forEach(
                messageAgentListener -> messageAgentListener.messageEnvoye(expediteur, agentReference, IMessage));
    }


    @Override
    public Optional<IMessage> receiveMessage(AgentReference reciever) {
        lockAgentLecture(reciever);
        Optional<IMessage> message = Optional.ofNullable(agentsMessagesQueues.get(reciever))
                .map(ConcurrentLinkedQueue::poll);
        message.ifPresent(messageAgent -> notifierMessageAgentListeners(messageAgent.getEmitter(), messageAgent,
                reciever));
        unlockAgentLecture(reciever);
        return message;
    }

    @Override
    public ArrayList<IMessage> receiveMessages(AgentReference reciever) {
        //lockAgentLecture(destinataire);
        //Optional<IMessage> message = Optional.ofNullable()
        //		.map(ConcurrentLinkedQueue::poll);
        //message.ifPresent(messageAgent -> notifierMessageAgentListeners(messageAgent.getEmitter(), messageAgent,
        //		destinataire));
        // unlockAgentLecture(destinataire);
        //walid pour tester
       // System.out.println(destinataire);
       //  System.out.println(agentsMessagesQueues.get(destinataire));
        // System.out.println(agentsMessagesQueues.containsKey(destinataire));
        ArrayList messages = new ArrayList<>(agentsMessagesQueues.get(reciever));
        // vider la boite de message de l'agent destinataire
        agentsMessagesQueues.get(reciever).clear();
        return messages;
    }

    public void ajouterAgentListener(IAgentListener agentListener) {
        agentListeners.add(agentListener);
    }

    public void retirerAgentListener(IAgentListener agentListener) {
        agentListeners.remove(agentListener);
    }

    public void ajouterReferenceAgentListener(IReferenceAgentListener referenceAgentListener) {
        referenceAgentListeners.add(referenceAgentListener);
    }

    public void retirerReferenceAgentListener(IReferenceAgentListener referenceAgentListener) {
        referenceAgentListeners.remove(referenceAgentListener);
    }

    @Override
    public void ajouterMessageAgentListener(IMessageAgentListener messageAgentListener) {
        messageAgentListeners.add(messageAgentListener);
    }

    @Override
    public void retirerMessageAgentListener(IMessageAgentListener messageAgentListener) {
        messageAgentListeners.remove(messageAgentListener);
    }

    private void lockAgentEcriture(AgentReference agentReference) {
        executeIfPresent(agentsLocks.get(agentReference), readWriteLock -> readWriteLock.writeLock().lock());
    }

    private void lockAgentLecture(AgentReference agentReference) {
        executeIfPresent(agentsLocks.get(agentReference), readWriteLock -> readWriteLock.readLock().lock());
    }

    private void unlockAgentEcriture(AgentReference agentReference) {
        executeIfPresent(agentsLocks.get(agentReference), readWriteLock -> readWriteLock.writeLock().unlock());
    }

    private void unlockAgentLecture(AgentReference agentReference) {
        executeIfPresent(agentsLocks.get(agentReference), readWriteLock -> readWriteLock.readLock().unlock());
    }

    private <T> void executeIfPresent(T object, Consumer<T> objectConsumer) {
        if (object != null) {
            objectConsumer.accept(object);
        }
    }

    public List<IMessageAgentListener> getMessageAgentListeners() {
        return messageAgentListeners;
    }

    public Agent getAgentByRef(AgentReference agentReference) {
        return this.agents.get(agentReference);
    }
}