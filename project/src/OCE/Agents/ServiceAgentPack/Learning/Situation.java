/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Messages.MessageTypes;
import OCE.Perceptions.AbstractPerception;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

/**
 * Class implementing the situation (current or reference) of a service agent
 */
public class Situation {

    private Map<ServiceAgent, SituationEntry> mySetAgents; // the situation representing the current environment

    /**
     * Create a new situation
     */
    public Situation() {
        this.mySetAgents = new TreeMap<>();
    }


    /**
     * Create the situation from a list of messages
     * @param listMessages : the list of received messages
     */
    public  Situation(ArrayList<AbstractPerception> listMessages){

        //Transform the list of messages (Perceptions) to a list of Situation Entries
        List<SituationEntry> myListSituationEntries = new ArrayList<>(listMessages.stream()
                .map(m -> m.toSituationEntry())
                .collect(Collectors.toList())
        );
        System.out.println("Situation Entries" + myListSituationEntries);

//        for (AbstractPerception message : listMessages){
//            //Convert the message (perception) to a situationEntry
//            SituationEntry situationEntry = message.toSituationEntry();
//            //Add the situation entry to the list
//            myListSituationEntries.add(situationEntry);
//        }

        //Filtering to omit EmptyMessage
        List<SituationEntry> myListSituationEntriesFiltered = myListSituationEntries.stream()
                .filter(m -> m.getMessageType() != MessageTypes.EMPTY)
                .collect(Collectors.toList());

        System.out.println("Filtred Situation Entries" + myListSituationEntriesFiltered);

        //Transform the filtered list of situation entries to a Situation
        //The third parameter of Collector.toMap function is used when a duplicate key is detected, i.e: a service agent exist before with 'value 'x' : we keep the new value 'y' of the one just added

        this.mySetAgents = myListSituationEntriesFiltered.stream().collect(Collectors.toMap(SituationEntry::getAgent, s->s, (x, y) ->  y));
    }

    /**
     * Get the list of situation entries representing the current situation
     * @return the set of situation entries
     */
    public Map<ServiceAgent, SituationEntry> getMySetAgents() {
        return mySetAgents;
    }

    /**
     * Set the list of situation entries representing the current situation
     * @param mySetAgents : the new list of situation entries
     */
    public void setMySetAgents(Map<ServiceAgent, SituationEntry> mySetAgents) {
        this.mySetAgents = mySetAgents;
    }


    /**
     * Compute the intersection set of this situation and the other situation given as parameter
     * @param otherSituation : the other situation
     * @return Situation representing the intersection between this situation and the one send as a parameter
     */
    public Map<ServiceAgent, SituationEntry> intersection(Map<ServiceAgent, SituationEntry> otherSituation){
        Map<ServiceAgent, SituationEntry> intersect = new TreeMap<>(this.mySetAgents);
        //this.getMySetAgents().
        // .filter(setB::contains)
        return null;
    }

    /**
     * Compute the union set of this situation and the other situation given as parameter
     * @param otherSituation : the other situation
     * @return Situation representing the union set of this situation and the one send as a parameter
     */
    public Map<ServiceAgent, SituationEntry> union(Map<ServiceAgent, SituationEntry> otherSituation){
        return null;
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
}
