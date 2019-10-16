/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.*;

public class BestScoreEpsilonGreedy implements IAgentSelectionStrategy {

    private double epsilon; // the threshold for the Epsilon greedy strategy of selection

    public BestScoreEpsilonGreedy(double epsilon) {
        epsilon = epsilon;
    }

    @Override
    public Map.Entry<IDAgent, ScoredCurrentSituationEntry> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {
        //Create the list of potential best agent
        List<IDAgent> listBestAgent = new ArrayList<>();
        //Generate a random probability of exploration
        Random random = new Random();
        double probatExploration = random.nextDouble();
        System.out.println("ProbatExploration = "+probatExploration);

        if(probatExploration <= epsilon){ // Exploration -> choose a random Agent

            Random randomIndexG = new Random();
            int indexAgent = randomIndexG.nextInt(scoredCS.getAgentSituationEntries().size());
            System.out.println("random index = "+indexAgent);
            List<IDAgent> listAgents = new ArrayList<>(scoredCS.getAgentSituationEntries().keySet());
            IDAgent bestAgent = listAgents.get(indexAgent);
            return new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent));

        }else {//Exploitation -> choose the best agent according to the values of score

            //Get the maximum score in the situation
            Double maximumValue  = (Double) scoredCS.getAgentSituationEntries().values().stream().map(e-> ((ScoredCurrentSituationEntry) e).getScore()).max(Comparator.comparing(Double::doubleValue)).get();
            //System.out.println("MAX score="+maximumValue);
            for (Map.Entry<IDAgent, ScoredCurrentSituationEntry> entry : scoredCS.getAgentSituationEntries().entrySet()) {  // Iterate through the list of agent and their score
                if (entry.getValue().getScore()==maximumValue) {
                    System.out.println(entry.getKey());     // Print the key with max value
                    //Add the agent to the list of best agents
                    listBestAgent.add(entry.getKey());
                }
            }
            //check if we have more that one agent with score==maximum value
            if(listBestAgent.size()>1){
                //choose one randomlly between them
                Random r = new Random();
                int indexAgent = r.nextInt(listBestAgent.size());
                //System.out.println("random index = "+indexAgent);
                //Get the ID corresponding to the random generated index
                IDAgent bestAgent = listBestAgent.get(indexAgent);
                return new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent));
            }
            else{
                //Get the best agent (there is only one)
                IDAgent bestAgent = listBestAgent.get(0);
                return new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent));
            }
        }
    }
}
