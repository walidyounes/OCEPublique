/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;
import OCE.OCEMessages.OCEMessage;

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
public class Situation < T extends SituationEntry> {

    private Map<IDAgent, T> mySetAgents; // the situation representing the current environment

    /**
     * Create a new situation
     */
    public Situation() {
        this.mySetAgents = new TreeMap<>();
    }

    /**
     * Create the situation from a list of messages, here we create a current situation only
     * @param listMessages : the list of received messages
     */
    public  Situation(List<OCEMessage> listMessages){
        this.mySetAgents = new TreeMap<>();
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
        this.mySetAgents = myListSituationEntries.stream().collect(Collectors.toMap(SituationEntry::getAgent, s->(T)s, (x, y) ->  y));
//        for(SituationEntry sitE : myListSituationEntries){
//            this.addElement(sitE.getAgent(), (T) sitE);
//        }
    }

    /**
     * Get the list of situation entries representing the current situation
     * @return the set of situation entries
     */
    public Map<IDAgent, T> getMySetAgents() {
        return mySetAgents;
    }

    /**
     * Set the list of situation entries representing the current situation
     * @param mySetAgents : the new list of situation entries
     */
    public void setMySetAgents(Map<IDAgent, T> mySetAgents) {
        this.mySetAgents = mySetAgents;
    }

    /**
     * Add a situationEntry to the situation
     * @param key   : the reference of the service agent
     * @param value : the situation entry corresponding the the service agent with the reference "key"
     */
    public void addSituationEntry(IDAgent key, T value){
        this.mySetAgents.put(key,value);
    }

    /**
     * Test if the agent is in the situation
     * @param idServiceAgent : the id of the service agent to test its existence
     * @return true if the service agent exists in the situation, else return false
     */
    public boolean containServiceAgent(IDAgent idServiceAgent){
        return this.mySetAgents.containsKey(idServiceAgent);
    }

    /**
     * Get the situation Entry corresponding of the specified agent
     * @param idAgent : the id of the agent
     * @return the situation entry corresponding to the agent, if the agent does not exist, the function will return null
     */
    public T getSituationEntryByIDAgent(IDAgent idAgent){
        if (this.containServiceAgent(idAgent)){
            return this.mySetAgents.get(idAgent);
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

}
