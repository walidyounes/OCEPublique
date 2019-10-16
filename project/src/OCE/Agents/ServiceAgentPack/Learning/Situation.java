/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.OCEMessages.OCEMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

/**
 * Class implementing the situation (current or reference) of a service agent
 */
public class Situation < T extends SituationEntry> {

    private Map<IDAgent, T> agentSituationEntries; // the situation representing the current environment

    /**
     * Create a new situation
     */
    public Situation() {
        this.agentSituationEntries = new TreeMap<>();
    }

    /**
     * Create the situation from a list of messages, here we create a current situation only
     * @param listMessages : the list of received messages
     */
    public  Situation(List<OCEMessage> listMessages){
        this.agentSituationEntries = new TreeMap<>();
        //Transform the list of messages (OCEMessages) to a list of Situation Entries
        ArrayList<SituationEntry> myListSituationEntries = new ArrayList<>(listMessages.stream()
                .map(m -> m.toEntrySituation())
                .collect(Collectors.toList())
        );

        System.out.println("Situation Entries" + myListSituationEntries);

//        for (OCEMessage message : listMessages){
//            //Convert the message (perception) to a situationEntry
//            SituationEntry situationEntry = message.toEntrySituation();
//            //Add the situation entry to the list
//            myListSituationEntries.add(situationEntry);
//        }

//        //Filtering to omit EmptyInfraMessage
//        List<SituationEntry> myListSituationEntriesFiltered = myListSituationEntries.stream()
//                .filter(m -> m.getMessageType() != MessageTypes.EMPTY)
//                .collect(Collectors.toList());

//        System.out.println("Filtred Situation Entries" + myListSituationEntriesFiltered);

        //Transform the filtered list of situation entries to a Situation
        //The third parameter of Collector.toMap function is used when a duplicate key is detected, i.e: a service agent exist before with 'value 'x' : we keep the new value 'y' of the one just added
        this.agentSituationEntries = myListSituationEntries.stream().collect(Collectors.toMap(SituationEntry::getAgent, s->(T)s, (x, y) ->  y));
//        for(SituationEntry sitE : myListSituationEntries){
//            this.addElement(sitE.getAgent(), (T) sitE);
//        }
    }

    /**
     * Get the list of situation entries representing the current situation
     * @return the set of situation entries
     */
    public Map<IDAgent, T> getAgentSituationEntries() {
        return agentSituationEntries;
    }

    /**
     * Set the list of situation entries representing the current situation
     * @param agentSituationEntries : the new list of situation entries
     */
    public void setAgentSituationEntries(Map<IDAgent, T> agentSituationEntries) {
        this.agentSituationEntries = agentSituationEntries;
    }

    /**
     * Add a situationEntry to the situation
     * @param key   : the reference of the service agent
     * @param value : the situation entry corresponding the the service agent with the reference "key"
     */
    public void addSituationEntry(IDAgent key, T value){
        this.agentSituationEntries.put(key,value);
    }

    /**
     * Test if the agent is in the situation
     * @param idServiceAgent : the id of the service agent to test its existence
     * @return true if the service agent exists in the situation, else return false
     */
    public boolean containServiceAgent(IDAgent idServiceAgent){
        return this.agentSituationEntries.containsKey(idServiceAgent);
    }

    /**
     * Get the situation Entry corresponding of the specified agent
     * @param idAgent : the id of the agent
     * @return the situation entry corresponding to the agent, if the agent does not exist, the function will return null
     */
    public T getSituationEntryByIDAgent(IDAgent idAgent){
        if (this.containServiceAgent(idAgent)){
            return this.agentSituationEntries.get(idAgent);
        }else return null;
    }


    @Override
    public String toString() {
        final Class<?> thisClass = getClass();
        final TreeMap<String,Object> values = new TreeMap<>();
        for (final Field f : thisClass.getDeclaredFields()) {
            if (ReadWriteLock.class.isAssignableFrom(f.getClass())) continue;
            if ((f.getModifiers() & Modifier.STATIC) != 0) continue;
            try {
                values.put(f.getName(), f.get(this));
            } catch (final IllegalAccessException e) {
                values.put(f.getName(), "<THIS SHOULD NOT HAPPEN>");
            }
        }
        return values.toString();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if(obj==null || getClass() != obj.getClass())
            return false;
        if (this == obj)
            return true;

        Situation<T> that = (Situation<T>) obj;
        return this.agentSituationEntries.equals(that.getAgentSituationEntries());
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return this.agentSituationEntries.hashCode();
    }
}
