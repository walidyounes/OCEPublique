/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Annuaire;

import sma.infrastructure.agent.Agent;
import sma.infrastructure.agent.ReferenceAgent;
import Infrastructure.Communication.IMessageAgent;

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
    private ConcurrentMap<ReferenceAgent, Agent> agents; // contient les references des agents à l'instant t
    private ConcurrentMap<ReferenceAgent, ConcurrentLinkedQueue<IMessageAgent>> agentsMessagesQueues; // contient les references des agents associés aux messages reçus
    private ConcurrentMap<ReferenceAgent, ReadWriteLock> agentsLocks;

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

    public ConcurrentMap<ReferenceAgent, Agent> getAgents() {
        return agents;
    }

    @Override
    public void addAgent(Agent agent) {
        agentsLocks.put(agent.getReferenceAgent(), new ReentrantReadWriteLock());
        lockAgentEcriture(agent.getReferenceAgent());
        agents.put(agent.getReferenceAgent(), agent);
        agentsMessagesQueues.put(agent.getReferenceAgent(), new ConcurrentLinkedQueue<>());
        unlockAgentEcriture(agent.getReferenceAgent());
        referenceAgentListeners.forEach(agentListener -> agentListener.agentAjoute(agent.getReferenceAgent()));
        agentListeners.forEach(agentListener -> agentListener.agentAjoute(agent));
    }

    @Override
    public void removeAgent(ReferenceAgent referenceAgent) {

        agentListeners.forEach(agentListener -> agentListener.agentRetire(agents.get(referenceAgent)));
        lockAgentEcriture(referenceAgent);
        agents.remove(referenceAgent);
        agentsMessagesQueues.remove(referenceAgent);
        unlockAgentEcriture(referenceAgent);
        referenceAgentListeners.forEach(agentListener -> agentListener.agentRetire(referenceAgent));
        System.out.println("La liste de references " + getAgents()); // Trace
    }

    /*	@Override
        public void envoyerMessage(ReferenceAgent expediteur, ReferenceAgent destinataire, IMessageAgent IMessageAgent) {

            lockAgentLecture(destinataire);
            if (agentsMessagesQueues.containsKey(destinataire)) {
                agentsMessagesQueues.get(destinataire).add(IMessageAgent);
                messageAgentListeners.forEach(messageAgentListener -> messageAgentListener.messageEnvoye(expediteur,
                        destinataire, IMessageAgent));
            }
            unlockAgentLecture(destinataire);

            System.out.println("liste envoyerMessage" + getAgentsMessagesQueues()); // Trace
        }
    */
    public ConcurrentMap<ReferenceAgent, ConcurrentLinkedQueue<IMessageAgent>> getAgentsMessagesQueues() {
        return agentsMessagesQueues;
    }

    @Override
    public void envoyerMessage(IMessageAgent message) {

        lockAgentLecture(message.getDestinataires().get(0));
        if (agentsMessagesQueues.containsKey(message.getDestinataires().get(0))) {
            agentsMessagesQueues.get(message.getDestinataires().get(0)).add(message);
            messageAgentListeners.forEach(messageAgentListener -> messageAgentListener.messageEnvoye(message.getExpediteur(),
                    message.getDestinataires().get(0), message));
        }
        unlockAgentLecture(message.getDestinataires().get(0));

        //System.out.println("liste envoyerMessage" + getAgentsMessagesQueues()); // Trace
    }

    /*@Override
    public void diffuserMessage(ReferenceAgent expediteur, IMessageAgent IMessageAgent) {

        agentsMessagesQueues.keySet().forEach(this::lockAgentLecture);
        agentsMessagesQueues.entrySet().forEach(referenceAgentEntry -> {
            referenceAgentEntry.getValue().add(IMessageAgent);
            notifierMessageAgentListeners(expediteur, IMessageAgent, referenceAgentEntry.getKey());
        });
        agentsMessagesQueues.keySet().forEach(this::unlockAgentLecture);

        System.out.println("liste diffuserMessage" + getAgentsMessagesQueues()); // tarce
    }
*/
    @Override
    public void diffuserMessage(IMessageAgent message) {
        agentsMessagesQueues.keySet().forEach(this::lockAgentLecture);
        agentsMessagesQueues.entrySet().forEach(referenceAgentEntry -> {
            referenceAgentEntry.getValue().add(message);
            notifierMessageAgentListeners(message.getExpediteur(), message, referenceAgentEntry.getKey());
        });
        agentsMessagesQueues.keySet().forEach(this::unlockAgentLecture);

        // System.out.println("liste diffuserMessage" + getAgentsMessagesQueues()); // tarce
    }

    private void notifierMessageAgentListeners(ReferenceAgent expediteur, IMessageAgent IMessageAgent,
                                               ReferenceAgent referenceAgent) {
        messageAgentListeners.forEach(
                messageAgentListener -> messageAgentListener.messageEnvoye(expediteur, referenceAgent, IMessageAgent));
    }


    @Override
    public Optional<IMessageAgent> recevoirMessage(ReferenceAgent destinataire) {
        lockAgentLecture(destinataire);
        Optional<IMessageAgent> message = Optional.ofNullable(agentsMessagesQueues.get(destinataire))
                .map(ConcurrentLinkedQueue::poll);
        message.ifPresent(messageAgent -> notifierMessageAgentListeners(messageAgent.getExpediteur(), messageAgent,
                destinataire));
        unlockAgentLecture(destinataire);
        return message;
    }

    @Override
    public List<IMessageAgent> recevoirMessages(ReferenceAgent destinataire) {
        //lockAgentLecture(destinataire);
        //Optional<IMessageAgent> message = Optional.ofNullable()
        //		.map(ConcurrentLinkedQueue::poll);
        //message.ifPresent(messageAgent -> notifierMessageAgentListeners(messageAgent.getExpediteur(), messageAgent,
        //		destinataire));
        // unlockAgentLecture(destinataire);
        //walid pour tester
       // System.out.println(destinataire);
       //  System.out.println(agentsMessagesQueues.get(destinataire));
        // System.out.println(agentsMessagesQueues.containsKey(destinataire));
        ArrayList messages = new ArrayList<>(agentsMessagesQueues.get(destinataire));
        // vider la boite de message de l'agent destinataire
        agentsMessagesQueues.get(destinataire).clear();
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

    private void lockAgentEcriture(ReferenceAgent referenceAgent) {
        executeIfPresent(agentsLocks.get(referenceAgent), readWriteLock -> readWriteLock.writeLock().lock());
    }

    private void lockAgentLecture(ReferenceAgent referenceAgent) {
        executeIfPresent(agentsLocks.get(referenceAgent), readWriteLock -> readWriteLock.readLock().lock());
    }

    private void unlockAgentEcriture(ReferenceAgent referenceAgent) {
        executeIfPresent(agentsLocks.get(referenceAgent), readWriteLock -> readWriteLock.writeLock().unlock());
    }

    private void unlockAgentLecture(ReferenceAgent referenceAgent) {
        executeIfPresent(agentsLocks.get(referenceAgent), readWriteLock -> readWriteLock.readLock().unlock());
    }

    private <T> void executeIfPresent(T object, Consumer<T> objectConsumer) {
        if (object != null) {
            objectConsumer.accept(object);
        }
    }

    public List<IMessageAgentListener> getMessageAgentListeners() {
        return messageAgentListeners;
    }

    public Agent getAgentByRef(ReferenceAgent referenceAgent) {
        return this.agents.get(referenceAgent);
    }
}