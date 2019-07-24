/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.OCEMessages.MessageTypes;
import java.util.*;
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
    public static <U extends SituationEntry,V extends SituationEntry> Set<IDAgent> intersection(Situation<U> firstSituation, Situation<V> secondSituation){

        Map<IDAgent, V> otherSituationEntries = secondSituation.getMySetAgents();

        Set<IDAgent> intersect = new TreeSet<>(firstSituation.getMySetAgents().keySet().stream().filter(secondSituation.getMySetAgents().keySet()::contains).collect(Collectors.toSet()));

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
     * @return PackageSituation.PackageSituation representing the union set of this situation and the one send as a parameter
     */
    public static <U extends SituationEntry,V extends SituationEntry> Set<IDAgent> union(Situation<U> firstSituation, Situation<V> secondSituation){
        Set<IDAgent> union = new TreeSet<>(firstSituation.getMySetAgents().keySet());
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
        Set<IDAgent> intersect = intersection(firstSituation, secondSituation);
        //System.out.println("Taille Intersection = " + intersect.size());
        Set<IDAgent> unionE = union(firstSituation, secondSituation);
        //System.out.println("Taille Union " + unionE.size());
        return (double) intersect.size() / (double)unionE.size();
    }

    /**
     * Extract from the list of reference situations those that are similar to the current situation, i.e: the degree of similarity is greater than the threshold
     * @param currentSituation          : the current situation
     * @param listReferenceSituations   : the list of reference situations (the knowledge base)
     * @param threshold                 : the threshold used for selecting similar reference situations
     * @return if no reference situation is similar to the current situation we return an empty list, otherwise we return the list ordered by the degree of similarity
     */
    public static Map<Situation<ReferenceSituationEntry>, Double> getSimilarReferenceSituations(Situation<CurrentSituationEntry> currentSituation, List<Situation<ReferenceSituationEntry>> listReferenceSituations, double threshold){

        //Create the list of similar reference situations
        Map<Situation<ReferenceSituationEntry>, Double> listSimilarRSituations = new HashMap<>();

        //Iterate on the list of reference situations and compute the similarity
        for(Situation<ReferenceSituationEntry> refSituation : listReferenceSituations){
            //compute the similarity
            double degreeSimilarity= computeJaccardSimilarity(currentSituation,refSituation);
            System.out.println("RS: "+refSituation);
            System.out.println("Similarity: "+ degreeSimilarity);
            //check if the degree of similarity is greater than the threshold
            if(degreeSimilarity >= threshold){
                //Add the reference situation to the list of similar situations
                listSimilarRSituations.put(refSituation,degreeSimilarity);
            }
        }
        //Order the list of similar reference situations by the decreasing order of the degree of similarity
        if(!listSimilarRSituations.isEmpty()){
            Map<Situation<ReferenceSituationEntry>, Double> orderedListSimilarRSituations  = listSimilarRSituations.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            System.out.println("test "+orderedListSimilarRSituations.containsValue(1.0));
            //If there is a reference situation that is equal exactly to the current situation we return it, //this reference situation is indexed 0 cause we ordered the list depending on the degree of similarity
            if(orderedListSimilarRSituations.containsValue(1.0)){
                //filter the ordered list leaving only the one with the degree of similarity = 1
                Map<Situation<ReferenceSituationEntry>, Double> filtredOrderedListSimilarRSituations = orderedListSimilarRSituations.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue()==1.0)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                return filtredOrderedListSimilarRSituations;
            }
            else {
                return orderedListSimilarRSituations;
            }
        }else{
            return listSimilarRSituations;
        }
    }

    /**
     * Score the service agent of the current situation using the scores stored in the reference situation where it exists
     * @param currentSituation          : the situation to be scored
     * @param listReferenceSituations   : the list of reference situations (which are similar to "currentSituation") used to score the current situation, and the degree of similarity
     * @param initialValue              : the numerical value used to initialise the score
     * @return the scored current situation
     */
    public static Situation<ScoredCurrentSituationEntry> scoreCurrentSituation(Situation<CurrentSituationEntry> currentSituation, Map<Situation<ReferenceSituationEntry>, Double> listReferenceSituations, double initialValue){
        Situation<ScoredCurrentSituationEntry> scoredCurrentSituation = new Situation<>(); // Create an empty situation
        //For each service agent in the current situation
        for (IDAgent serviceAgent : currentSituation.getMySetAgents().keySet()){
            int count = 0; // the number of references situations where the service agent appears
            double scoreServiceAgent = initialValue; // the value of score (the mean of scores from the reference situation where the service agent appears or initial value)
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
                //Todo : change this code and put it in a function in the class PackageSituation.CurrentSituationEntry
                //Create a scoredCurrentSituationEntry for the service agent
                serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);
            }else{ // The service agent doesn't appear in any reference situation
                //Generate a random score
                //Create a scoredCurrentSituationEntry for the service agent
                serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);
            }
            //Add the entry to the scored situation entry
            scoredCurrentSituation.addSituationEntry(serviceAgent, serviceAgentScoredEntry);
        }
        return scoredCurrentSituation;
    }

    /**
     * Select the best agent depending on multiple strategies of selection (best score, best score with type and priority of messages)
     * @param scoredCS               : the scored situation from where we will select the best agent
     * @param agentSelectionStrategy : the strategy used to choose he best agent
     * @return the selected agent
     */
    public static Map.Entry<IDAgent, ScoredCurrentSituationEntry> selectBestAgent(Situation<ScoredCurrentSituationEntry> scoredCS, IAgentSelectionStrategy agentSelectionStrategy){
        return agentSelectionStrategy.execute(scoredCS);
    }

    /**
     * Extract the feedback from the xml file and used it to update the scores using the bandit formula
     * @param scoredCS              : the scored current situation to be updated
     * @param xmlFeedbackFileName   : the file containing the user feedback
     * @return a new reference situation to put in the agent knowledge base
     */
    public static Situation<ReferenceSituationEntry> updateScore(Situation<ScoredCurrentSituationEntry> scoredCS, String xmlFeedbackFileName){
        return null;
    }

}
