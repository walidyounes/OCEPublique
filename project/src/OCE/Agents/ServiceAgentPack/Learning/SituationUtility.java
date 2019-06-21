/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class implement a set of functions used on object of type situation
 * @author Walid YOUNES
 * @version 1.0
 */
public class SituationUtility {


    /**
     * Compute the intersection between two situation given as parameter
     * @param firstSituation  : the first situation
     * @param secondSituation : the second situation
     * @return a Set of ServiceAgent {A_i | A_i in firstSituation && A_i in secondSituation)
     */
    public static <U extends SituationEntry,V extends SituationEntry> Set<ServiceAgent> intersection(Situation<U> firstSituation, Situation<V> secondSituation){

        Map<ServiceAgent, V> otherSituationEntries = secondSituation.getMySetAgents();

        Set<ServiceAgent> intersect = new TreeSet<>(firstSituation.getMySetAgents().keySet().stream().filter(secondSituation.getMySetAgents().keySet()::contains).collect(Collectors.toSet()));

//        System.out.println("Taille Intersection = " + keyIntersection.size());
//        for (ServiceAgent key: firstSituation.getMySetAgents().keySet())
//        {
//            if (otherSituationEntries.containsKey(key))
//                intersect.add(key);
//        }
        return intersect;
    }

    /**
     * Compute the intersection between two situation given as parameter
     * @param firstSituation  : the first situation
     * @param secondSituation : the other situation
     * @return Situation representing the union set of this situation and the one send as a parameter
     */
    public static <U extends SituationEntry,V extends SituationEntry> Set<ServiceAgent> union(Situation<U> firstSituation, Situation<V> secondSituation){
        Set<ServiceAgent> union = new TreeSet<>(firstSituation.getMySetAgents().keySet());
        union.addAll(secondSituation.getMySetAgents().keySet());
        return union;
    }

    /**
     * Compute the similarity between this situation and the situation send as a parameter using the JACCARD Similarity function
     * @param firstSituation  : the first situation
     * @param secondSituation : the second situation
     * @return the similarity degree between the two situations (1  the two are similar, 0 else)
     */
    public static <U extends SituationEntry,V extends SituationEntry> double computeJaccardSimilarity(Situation<U> firstSituation, Situation<V> secondSituation){
        Set<ServiceAgent> intersect = intersection(firstSituation, secondSituation);
        System.out.println("Taille Intersection = " + intersect.size());
        Set<ServiceAgent> unionE = union(firstSituation, secondSituation);
        System.out.println("Taille Union " + unionE.size());
        return (double) intersect.size() / (double)unionE.size();
    }


    /**
     * Score the service agent of the current situation using the scores stored in the reference situation where it exists
     * @param currentSituation          : the situation to be scored
     * @param listReferenceSituations   : the list of reference situations (which are similar to "currentSituation") used to score the current situation, and the degree of similarity
     * @return the scored current situation
     */
    public static Situation<ScoredCurrentSituationEntry> scoreCurrentSituation(Situation<CurrentSituationEntry> currentSituation, Map<Situation<ReferenceSituationEntry>, Integer> listReferenceSituations){
        Situation<ScoredCurrentSituationEntry> scoredCurrentSituation = new Situation<>(); // Create an empty situation
        //For each service agent in the current situation
        for (ServiceAgent serviceAgent : currentSituation.getMySetAgents().keySet()){
            int count = 0; // the number of references situations where the service agent appears
            double scoreServiceAgent = 0.0; // the value of score (the mean of scores from the reference situation where the service agent appears)
            //For each similar reference situations
            for(Situation<ReferenceSituationEntry> referenceSituation : listReferenceSituations.keySet()){
                //check if the service agent exists in the reference situation
                if(referenceSituation.containServiceAgent(serviceAgent)){
                    //Get the score of the agent and multiply it by the degree of similarity between this reference situation and the current situation
                    scoreServiceAgent += referenceSituation.getMySetAgents().get(serviceAgent).getScore()* listReferenceSituations.get(referenceSituation);
                    //Increase the number of reference situation where the agent is found
                    count++;
                }
            }
            ScoredCurrentSituationEntry serviceAgentScoredEntry =null; // the entry corresponding to the service agent after scoring
            //Get the message type send by the service agent in the current situation
            MessageTypes messageType = currentSituation.getMySetAgents().get(serviceAgent).getMessageType();
            //check if the service agent appears at least in one reference situation
            if(count!=0) {
                //Compute the mean of scores
                scoreServiceAgent = scoreServiceAgent / count;
                //Todo : change this code and put it in a function in the class CurrentSituationEntry
                //Create a scoredCurrentSituationEntry for the service agent
                serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);

            }else{ // The service agent doesn't appear in any reference situation
                //Generate a random score
                //ScoreServiceAgent == 0.0 cause it's initialised to 0  // todo : change the score from 0 to a random value
                //Create a scoredCurrentSituationEntry for the service agent
                serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);
            }
            //Add the entry to the scored situation entry
            scoredCurrentSituation.addSituationEntry(serviceAgent, serviceAgentScoredEntry);
        }
        return null;
    }
}
