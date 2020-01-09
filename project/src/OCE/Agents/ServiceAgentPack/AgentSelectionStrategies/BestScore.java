/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.*;

public class BestScore implements IAgentSelectionStrategy {
    @Override
    public Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {
        //Create the list of potential best agent
        List<IDAgent> listBestAgent = new ArrayList<>();

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
            //choose one random
            Random r = new Random();
            int indexAgent = r.nextInt(listBestAgent.size());
            //System.out.println("random index = "+indexAgent);
            //Get the ID corresponding to the random generated index
            IDAgent bestAgent = listBestAgent.get(indexAgent);
            return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent)));
        }
        else{
            //Get the best agent (there is only one)
            IDAgent bestAgent = listBestAgent.get(0);
            return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent)));
        }
    }
}
