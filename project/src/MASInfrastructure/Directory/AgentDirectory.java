/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Directory;

import MASInfrastructure.Agent.InfrastructureAgent;
import MASInfrastructure.Agent.InfraAgentReference;
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

public class AgentDirectory implements IAgentDirectory {

    private List<IAgentListener> agentListeners;
    private List<IReferenceAgentListener> referenceAgentListeners;
    private List<IMessageAgentListener> messageAgentListeners;
    private ConcurrentMap<InfraAgentReference, InfrastructureAgent> agents; // contient les references des agents à l'instant t
    private ConcurrentMap<InfraAgentReference, ConcurrentLinkedQueue<IMessage>> agentsMessagesQueues; // contient les references des agents associés aux messages reçus
    private ConcurrentMap<InfraAgentReference, ReadWriteLock> agentsLocks;

    /**
     * Private constructor for implementing the pattern "singleton"
     */
    private AgentDirectory() {
        referenceAgentListeners = Collections.synchronizedList(new ArrayList<>());
        agentListeners = Collections.synchronizedList(new ArrayList<>());
        messageAgentListeners = Collections.synchronizedList(new ArrayList<>());
        agents = new ConcurrentHashMap<>();
        agentsMessagesQueues = new ConcurrentHashMap<>();
        agentsLocks = new ConcurrentHashMap<>();
    }

    /**
     * Internal class for implementing the HOLDER technique
     */
    private static class AgentDirectoryHolder {
        /**
         * Unique instance pre initialised
         */
        private final static AgentDirectory instance = new AgentDirectory();
    }

    /**
     * Point d'accès pour l'instance unique du singleton "Directory"
     */
    public static AgentDirectory getInstance() {
        return AgentDirectoryHolder.instance;
    }

    public ConcurrentMap<InfraAgentReference, InfrastructureAgent> getAgents() {
        return agents;
    }

    @Override
    public void addAgent(InfrastructureAgent infrastructureAgent) {
        agentsLocks.put(infrastructureAgent.getInfraAgentReference(), new ReentrantReadWriteLock());
        lockAgentEcriture(infrastructureAgent.getInfraAgentReference());
        agents.put(infrastructureAgent.getInfraAgentReference(), infrastructureAgent);
        agentsMessagesQueues.put(infrastructureAgent.getInfraAgentReference(), new ConcurrentLinkedQueue<>());
        unlockAgentEcriture(infrastructureAgent.getInfraAgentReference());
        referenceAgentListeners.forEach(agentListener -> agentListener.agentAjoute(infrastructureAgent.getInfraAgentReference()));
        agentListeners.forEach(agentListener -> agentListener.addAgent(infrastructureAgent));
    }

    @Override
    public void removeAgent(InfraAgentReference infraAgentReference) {

        agentListeners.forEach(agentListener -> agentListener.deleteAgent(agents.get(infraAgentReference)));
        lockAgentEcriture(infraAgentReference);
        agents.remove(infraAgentReference);
        agentsMessagesQueues.remove(infraAgentReference);
        unlockAgentEcriture(infraAgentReference);
        referenceAgentListeners.forEach(agentListener -> agentListener.agentRetire(infraAgentReference));
        System.out.println("La liste de references " + getAgents()); // Trace
    }

    /*	@Override
        public void sendMessage(InfraAgentReference expediteur, InfraAgentReference destinataire, IMessage IMessage) {

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
    public ConcurrentMap<InfraAgentReference, ConcurrentLinkedQueue<IMessage>> getAgentsMessagesQueues() {
        return agentsMessagesQueues;
    }

    @Override
    public void sendMessage(IMessage message) {
        int index;
        for(index=0; index < message.getReceivers().size(); index++){
            lockAgentLecture(message.getReceivers().get(index));
            if (agentsMessagesQueues.containsKey(message.getReceivers().get(index))) {
                agentsMessagesQueues.get(message.getReceivers().get(index)).add(message);
                int finalIndex = index;
                messageAgentListeners.forEach(messageAgentListener -> messageAgentListener.messageEnvoye(message.getEmitter(),
                        message.getReceivers().get(finalIndex), message));
            }
            unlockAgentLecture(message.getReceivers().get(index));
        }

        //System.out.println("liste sendMessage" + getAgentsMessagesQueues()); // Trace
    }

    /*@Override
    public void sendMessageBroadcast(InfraAgentReference expediteur, IMessage IMessage) {

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
        // The message should not be sended to the same agent
        agentsMessagesQueues.entrySet().forEach(referenceAgentEntry -> {
            if(referenceAgentEntry.getKey()!= message.getEmitter()){ referenceAgentEntry.getValue().add(message);
            notifierMessageAgentListeners(message.getEmitter(), message, referenceAgentEntry.getKey());
        }});
        agentsMessagesQueues.keySet().forEach(this::unlockAgentLecture);

        // System.out.println("liste sendMessageBroadcast" + getAgentsMessagesQueues()); // tarce
    }

    private void notifierMessageAgentListeners(InfraAgentReference expediteur, IMessage IMessage,
                                               InfraAgentReference infraAgentReference) {
        messageAgentListeners.forEach(
                messageAgentListener -> messageAgentListener.messageEnvoye(expediteur, infraAgentReference, IMessage));
    }


    @Override
    public Optional<IMessage> receiveMessage(InfraAgentReference reciever) {
        lockAgentLecture(reciever);
        Optional<IMessage> message = Optional.ofNullable(agentsMessagesQueues.get(reciever))
                .map(ConcurrentLinkedQueue::poll);
        message.ifPresent(messageAgent -> notifierMessageAgentListeners(messageAgent.getEmitter(), messageAgent,
                reciever));
        unlockAgentLecture(reciever);
        return message;
    }

    @Override
    public ArrayList<IMessage> receiveMessages(InfraAgentReference reciever) {
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

    private void lockAgentEcriture(InfraAgentReference infraAgentReference) {
        executeIfPresent(agentsLocks.get(infraAgentReference), readWriteLock -> readWriteLock.writeLock().lock());
    }

    private void lockAgentLecture(InfraAgentReference infraAgentReference) {
        executeIfPresent(agentsLocks.get(infraAgentReference), readWriteLock -> readWriteLock.readLock().lock());
    }

    private void unlockAgentEcriture(InfraAgentReference infraAgentReference) {
        executeIfPresent(agentsLocks.get(infraAgentReference), readWriteLock -> readWriteLock.writeLock().unlock());
    }

    private void unlockAgentLecture(InfraAgentReference infraAgentReference) {
        executeIfPresent(agentsLocks.get(infraAgentReference), readWriteLock -> readWriteLock.readLock().unlock());
    }

    private <T> void executeIfPresent(T object, Consumer<T> objectConsumer) {
        if (object != null) {
            objectConsumer.accept(object);
        }
    }

    public List<IMessageAgentListener> getMessageAgentListeners() {
        return messageAgentListeners;
    }

    public InfrastructureAgent getAgentByRef(InfraAgentReference infraAgentReference) {
        return this.agents.get(infraAgentReference);
    }
}