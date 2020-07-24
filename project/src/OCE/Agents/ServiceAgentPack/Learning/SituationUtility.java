/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.Learning;

import Logger.OCELogger;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.OCEMessages.MessageTypes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This class implement a set of functions used on object of matchingID situation
 * @author Walid YOUNES
 * @version 1.0
 */
public class SituationUtility {
    public static double CSN = 0.2; // The variable that indicates the sensitivity coefficient to novelty of the user (default value = 0.2)

    /**
     * Compute the intersection between two situation given as parameter
     * @param firstSituation  : the first situation
     * @param secondSituation : the second situation
     * @return a Set of ServiceAgent {A_i | A_i in firstSituation && A_i in secondSituation)
     */
    public static <U extends SituationEntry,V extends SituationEntry> Set<IDAgent> intersection(Situation<U> firstSituation, Situation<V> secondSituation){

        Map<IDAgent, V> otherSituationEntries = secondSituation.getAgentSituationEntries();

        Set<IDAgent> intersect = new TreeSet<>(firstSituation.getAgentSituationEntries().keySet().stream().filter(secondSituation.getAgentSituationEntries().keySet()::contains).collect(Collectors.toSet()));

//        System.out.println("Taille Intersection = " + keyIntersection.size());
//        for (ServiceAgent key: firstSituation.getAgentSituationEntries().keySet())
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
        Set<IDAgent> union = new TreeSet<>(firstSituation.getAgentSituationEntries().keySet());
        union.addAll(secondSituation.getAgentSituationEntries().keySet());
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
    public static Map<Situation<ReferenceSituationEntry>, Double> getSimilarReferenceSituations(Situation<CurrentSituationEntry> currentSituation, Set<Situation<ReferenceSituationEntry>> listReferenceSituations, double threshold){

        //Create the list of similar reference situations
        Map<Situation<ReferenceSituationEntry>, Double> listSimilarRSituations = new HashMap<>();

        //Iterate on the list of reference situations and compute the similarity
        for(Situation<ReferenceSituationEntry> refSituation : listReferenceSituations){

            //Map of the types of all agents present in refsituation with max and mean score
            Map<String,ReferenceSituationEntry> types = new TreeMap<>();
            Map<String,ReferenceSituationEntry> typesMean = new TreeMap<>();

            for (IDAgent agent : refSituation.getAgentSituationEntries().keySet()) {
                String typeAgent = agent.getType();
                boolean contains = types.containsKey(typeAgent);
                Double scoreAgent = refSituation.getAgentSituationEntries().get(agent).getScore();

                //add agent if type not null and not encountered
                //or update it if encountered but worst score than current one
                if (typeAgent != "" && ( !contains || (contains && scoreAgent > types.get(typeAgent).getScore()) ))
                    types.put(typeAgent, refSituation.getSituationEntryByIDAgent(agent));

                // using mean instead of max
                if (typeAgent != "") {
                    double currentScore = 0.0;
                    double div = 1.0;
                    if (typesMean.containsKey(typeAgent)) {
                        currentScore = typesMean.get(typeAgent).getScore();
                        div = 2.0;
                    }
                    typesMean.put(typeAgent, new ReferenceSituationEntry(
                            agent,
                            (refSituation.getSituationEntryByIDAgent(agent).getScore()+currentScore) / div));
                }
            }

            //reference situation where we substitute disappeared agents with a type by new agent of the same type
            Situation<ReferenceSituationEntry> resultMapping = new Situation<>();
            //foreach agent in current situation
            for (IDAgent currentAgent : currentSituation.getAgentSituationEntries().keySet()) {
                String type = currentAgent.getType(); //agent's type
                boolean typesContains = types.containsKey(type); //agent in types
                ReferenceSituationEntry refEntry = refSituation.getSituationEntryByIDAgent(currentAgent);
                // use it for max score instead of mean
                ReferenceSituationEntry typeEntry = types.get(type);

                // use it for mean score instead of max
                //ReferenceSituationEntry typeEntry = typesMean.get(type);

                // ALGO PRETRAITEMENT
                //if an agent is in refSituation he is added to result with his own score
                //only if his score is the best of his type or his type is not identified yet
                //else he take the score of the best of his type
//                if (refSituation.getAgentSituationEntries().keySet().contains(currentAgent)) {
//                    if (typesContains) {
//                        if (refEntry.getScore() > typeEntry.getScore())
//                            resultMapping.addSituationEntry(currentAgent,refEntry);
//                        else
//                            resultMapping.addSituationEntry(currentAgent,new ReferenceSituationEntry(currentAgent,typeEntry.getScore()));
//                    }
//                    else
//                        resultMapping.addSituationEntry(currentAgent, refEntry);
//                }
//                else {
//                    if (typesContains)
//                        resultMapping.addSituationEntry(currentAgent,new ReferenceSituationEntry(currentAgent,typeEntry.getScore()));
//                }

                //ALGO NOUVEAU COMPOSANT
                if (refSituation.getAgentSituationEntries().keySet().contains(currentAgent)) {
                    resultMapping.addSituationEntry(currentAgent, refEntry);
                }
                else {
                    if (typesContains)
                        resultMapping.addSituationEntry(currentAgent,new ReferenceSituationEntry(currentAgent,typeEntry.getScore()));
                }
            }

//            System.out.println("**************************\nDebut");
//            System.out.println(currentSituation.toString());
//            System.out.println(refSituation.toString());
//            System.out.println(types.toString());
//            System.out.println(resultMapping.toString());
//            System.out.println("FIN\n****************************");

            //compute the similarity
            double degreeSimilarity = computeJaccardSimilarity(currentSituation,resultMapping);
            System.out.println("RS: "+refSituation);
            System.out.println("Similarity: "+ degreeSimilarity);
            //check if the degree of similarity is greater than the threshold
            if(degreeSimilarity >= threshold){
                //Add the reference situation to the list of similar situations
                listSimilarRSituations.put(resultMapping,degreeSimilarity);
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
        List<ScoredCurrentSituationEntry> situationEntriesToScoreLaterMax = new ArrayList<>(); // List of situations entries to score with maximum value after all the other scores were compute
        List<ScoredCurrentSituationEntry> situationEntriesToScoreLaterMean = new ArrayList<>(); // List of situations entries to score with sum value after all the other scores were compute

        double maxScoreValue=0.0; //Maximum of scores of the agents that we managed to score in this situation
        double sumScoreValue=0.0; //The sum of scores of the agents that we managed to score in the current situation

        //If no reference situation similar to the current situation were found
        if(listReferenceSituations.size() == 0){
            OCELogger.log(Level.INFO,"Scoring Current situation -> no reference situations similaire to the current one were found ! ");
            //Get the number of agents in the current situation
            int numberAgentCS = currentSituation.getAgentSituationEntries().size();
            if(numberAgentCS > 0) {
                double valueScoreToPut = 1 / (double) numberAgentCS;
                OCELogger.log(Level.INFO,"Scoring Current situation -> no reference situations similaire to the current one were found -> initial value to put = " + valueScoreToPut);
                //For each service agent in the current situation
                for (IDAgent serviceAgent : currentSituation.getAgentSituationEntries().keySet()){
                    //Get the message type send by the service agent in the current situation
                    MessageTypes messageType = currentSituation.getAgentSituationEntries().get(serviceAgent).getMessageType();
                    //Add the entry to the scored situation entry
                    scoredCurrentSituation.addSituationEntry(serviceAgent,  new ScoredCurrentSituationEntry(serviceAgent, messageType, valueScoreToPut));
                }
            }
        }else{
            //For each service agent in the current situation
            for (IDAgent serviceAgent : currentSituation.getAgentSituationEntries().keySet()){
                int count = 0; // the number of references situations where the service agent appears
                double sum_weights=0.0; // The sum of the degree of similarity
                double scoreServiceAgent = initialValue; // the value of score (the mean of scores from the reference situation where the service agent appears or initial value)
                //For each similar reference situations
                for(Situation<ReferenceSituationEntry> referenceSituation : listReferenceSituations.keySet()){
                    //check if the service agent exists in the reference situation
                    if(referenceSituation.containServiceAgent(serviceAgent)){
                        //Get the score of the agent and multiply it by the degree of similarity between this reference situation and the current situation
                        scoreServiceAgent += referenceSituation.getAgentSituationEntries().get(serviceAgent).getScore()* listReferenceSituations.get(referenceSituation);
                        //Increase the number of reference situation where the agent is found
                        count++;
                        //Add the current value of the degree of similarity to the sum
                        sum_weights+=listReferenceSituations.get(referenceSituation);
                    }
                }
                ScoredCurrentSituationEntry serviceAgentScoredEntry =null; // the entry corresponding to the service agent after scoring
                //Get the message type send by the service agent in the current situation
                MessageTypes messageType = currentSituation.getAgentSituationEntries().get(serviceAgent).getMessageType();
                //check if the service agent appears at least in one reference situation
                if(count!=0) {
                    //Compute the weighted mean of scores
                    scoreServiceAgent = scoreServiceAgent / sum_weights;

                    //Update the maximum of scores  of the agents that we managed to score in the current situation
                    if(scoreServiceAgent > maxScoreValue){
                        maxScoreValue = scoreServiceAgent;
                    }
                    //Update the sum of scores  of the agents that we managed to score in the current situation
                    sumScoreValue = sumScoreValue + scoreServiceAgent;

                    //Todo walid Suggestion : change this code and put it in a function in the class PackageSituation.CurrentSituationEntry
                    //Create a scoredCurrentSituationEntry for the service agent
                    serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);

                }else{ // The service agent doesn't appear in any reference situation
                    //Create a scoredCurrentSituationEntry for the service agent
                    serviceAgentScoredEntry = new ScoredCurrentSituationEntry(serviceAgent, messageType, scoreServiceAgent);

                    //Generate a random probability of sensitivity coefficient to novelty
                    Random random = new Random();
                    double probatCSN = random.nextDouble();
                    OCELogger.log(Level.INFO,"Scoring Current situation -> Probability CSN = " + probatCSN);
                    //System.out.println("Probabilite de CSN = "+probatCSN);
                    if(probatCSN <= SituationUtility.CSN){ //Score with maximum
                        situationEntriesToScoreLaterMax.add(serviceAgentScoredEntry);
                    }else{//Score with sum of values
                        situationEntriesToScoreLaterMean.add(serviceAgentScoredEntry);
                    }
                }
                //Add the entry to the scored situation entry
                scoredCurrentSituation.addSituationEntry(serviceAgent, serviceAgentScoredEntry);
            }
            //Update the scores for the service agents that we couldn't score

            Random random = new Random();
            double finalMaxScoreValue = maxScoreValue + 0.01;
            // situationEntriesToScoreLaterMax.forEach(se -> se.setScore(finalMaxScoreValue+random.nextDouble()));
            situationEntriesToScoreLaterMax.forEach(se -> se.setScore(finalMaxScoreValue));
            //we compute the mean values of the score  (we take out the maximum score and the agent if the number of agents >2)
            int numberAgents = 0;
            if(scoredCurrentSituation.getAgentSituationEntries().size()==2){//Todo 15/01/2020 15:31 - here if we have 2 agents (one of them is new) sumScores == maxSCores -> the mean will be equal to 0
                numberAgents = 2;
                final double finalMeanValue = (maxScoreValue /numberAgents); // The meanScore = MaxScore /2
                situationEntriesToScoreLaterMean.forEach(se-> se.setScore(finalMeanValue));
            }else{
                if(scoredCurrentSituation.getAgentSituationEntries().size() > 2 ){
                    numberAgents = (scoredCurrentSituation.getAgentSituationEntries().size() -2);
                }else{
                    numberAgents = scoredCurrentSituation.getAgentSituationEntries().size();
                }
                final double finalMeanValue = (numberAgents >0 )? ((sumScoreValue-maxScoreValue) /numberAgents): 0.0;
                situationEntriesToScoreLaterMean.forEach(se-> se.setScore(finalMeanValue));
            }

        }
        OCELogger.log(Level.INFO,"Scoring Current situation -> scored current situation = " + scoredCurrentSituation);
        return scoredCurrentSituation;
    }

    /**
     * Select the best agent depending on multiple strategies of selection (best score, best score with matchingID and priority of messages)
     * @param scoredCS               : the scored situation from where we will select the best agent
     * @param agentSelectionStrategy : the strategy used to choose he best agent
     * @return the selected agent
     */
    public static Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> selectBestAgent(Situation<ScoredCurrentSituationEntry> scoredCS, IAgentSelectionStrategy agentSelectionStrategy){
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

    /**
     * Update the score of the agent in the scored current situation depending on the value of the feedback
     * @param scoredCurrentSituation    : the scored situation to be updated
     * @param agentRef                  : the reference  of the agent or which the score will be updated
     * @param alpha                     : the learning rate
     * @param reinforcement                      : A numerical value used to update the score
     * @return the updated scored current situation
     */
    public static Situation<ScoredCurrentSituationEntry> updateScoreCurrentSituation(Situation<ScoredCurrentSituationEntry> scoredCurrentSituation, IDAgent agentRef, double alpha, double reinforcement){

        ScoredCurrentSituationEntry agentSituationEntryToUpdate = scoredCurrentSituation.getSituationEntryByIDAgent(agentRef); // get the situation entry to update corresponding to the agent
        //Update the score using the bandit algorithm formula : newScore = OldScore + alpha(reinforcement - oldScore)
        double oldScore = agentSituationEntryToUpdate.getScore();
        //System.out.println("Old Score = "+ oldScore);
        double newScore = oldScore + alpha*(reinforcement - oldScore);
        //System.out.println("New Score = "+ newScore);
        //Update the score in the situationEntry
        agentSituationEntryToUpdate.setScore(newScore);

        return scoredCurrentSituation;
    }

    /**
     * Normalize the value of the scores in scored current situation so that the sum of scores will be equal to 1
     * @param scoredCurrentSituation : the scored current situations to be normalized
     */
    public static void normalizeScoresSCS(Situation<ScoredCurrentSituationEntry> scoredCurrentSituation) {

        //Create the formatter for round the values of scores
        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("###.###", otherSymbols);
        //If the number of agents in the current situation is higher than 1
        boolean sizeOne = (scoredCurrentSituation.getAgentSituationEntries().size() <=1);
        //Translation of all the scores to a positive interval [0, + a] a> 0 if their is negative scores
        boolean negative=false;
        Double minValue  =  scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e-> ((ScoredCurrentSituationEntry) e).getScore()).mapToDouble(d -> (double) d).min().getAsDouble();
        if(minValue < 0){
            negative = true;
            //Add the minimal value to all the scores of all agents
            scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> (ScoredCurrentSituationEntry) e).forEach(e -> ((ScoredCurrentSituationEntry) e).setScore(Double.parseDouble(df.format((((ScoredCurrentSituationEntry) e).getScore() - minValue)))));
        }
        //If their is one agent in the current situation and it's scored negatively we normalised it to 0
        if(sizeOne && negative) {
            scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> (ScoredCurrentSituationEntry) e).forEach(e -> ((ScoredCurrentSituationEntry) e).setScore(Double.parseDouble(df.format(0))));
        }else {
            //Compute the sum of all scores
            Double sumValue = scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> ((ScoredCurrentSituationEntry) e).getScore()).mapToDouble(d -> (double) d).sum();
            //normalize the scores
            if (sumValue != 0) {
                scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> (ScoredCurrentSituationEntry) e).forEach(e -> ((ScoredCurrentSituationEntry) e).setScore(Double.parseDouble(df.format((((ScoredCurrentSituationEntry) e).getScore() / sumValue)))));
            }else{
                //la somme == 0, we initialise with the same value but keeping the sum==1
                int numberOfAgentsInSituation = scoredCurrentSituation.getAgentSituationEntries().size();
                double value = Double.parseDouble(df.format(1/numberOfAgentsInSituation));
                scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> (ScoredCurrentSituationEntry) e).forEach(e -> ((ScoredCurrentSituationEntry) e).setScore(value));
            }
        }
    }

    /**
     * Compute and return the maximum value of the scores in the current scored situation
     * @param scoredCurrentSituation    :  the scored current situations
     * @return : the maximum value, returns 1 if the current scored situation is empty
     */
    public static double computeMaxScoresSCS(Situation<ScoredCurrentSituationEntry> scoredCurrentSituation){
        double maxValue = 1.0;

        if(scoredCurrentSituation.getAgentSituationEntries().size() >0){
            //The SCS contains at least one entry
            maxValue = scoredCurrentSituation.getAgentSituationEntries().values().stream().map(e -> ((ScoredCurrentSituationEntry) e).getScore()).mapToDouble(d -> (double) d).max().getAsDouble();
            return maxValue;
        }else{
            return maxValue;
        }
    }

}
