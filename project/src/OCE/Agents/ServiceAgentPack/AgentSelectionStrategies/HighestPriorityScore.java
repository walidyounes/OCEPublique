/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HighestPriorityScore implements IAgentSelectionStrategy {


    @Override
    public Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {

        //Get the agents which have the maximum priority (in message type)
        int HighestPriority  = (int) scoredCS.getAgentSituationEntries().values().stream().map(e-> e.getMessageType().ordinal()).max(Comparator.comparing(Integer::intValue)).get();
        System.out.println("MAX Priority = "+HighestPriority);
        //Select all the agents with the highest priority
        List<IDAgent> listAgentHighestPriority = scoredCS.getAgentSituationEntries().values().stream().filter(e -> e.getMessageType().ordinal()==HighestPriority).map(e -> e.getAgent()).collect(Collectors.toList());

        //If we have more that one agent with a highest priority -> we choose depending on the highest SCORE
        if(listAgentHighestPriority.size()>1) {

                //Get the maximum score of the agents that have the highest priority
                Double maximumValue = (Double) scoredCS.getAgentSituationEntries().values().stream().filter(e->listAgentHighestPriority.contains(e.getAgent())).map(e -> e.getScore()).max(Comparator.comparing(Double::doubleValue)).get();
                //Select from the list of agent with highest priority those who have the highest score
                List<IDAgent> selectedBestScoreAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e->listAgentHighestPriority.contains(e.getAgent())).filter(e -> e.getScore()==maximumValue).map(e -> e.getAgent()).collect(Collectors.toList());

                //If their is more thant one agent with the highest priority and highest score
                if (selectedBestScoreAgent.size() > 1) {
                    //choose one randomly between them
                    Random r = new Random();
                    int indexAgent = r.nextInt(selectedBestScoreAgent.size());
                    //Get the ID of the agent corresponding to the random generated index
                    IDAgent bestAgent = selectedBestScoreAgent.get(indexAgent);
                    return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent)));
                } else {
                    //Get the best agent (there is only one)
                    IDAgent bestAgent = selectedBestScoreAgent.get(0);
                    return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent)));
                }
        }else{
            //Get the best agent (there is only one)
            IDAgent bestAgent = listAgentHighestPriority.get(0);
            return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent, scoredCS.getAgentSituationEntries().get(bestAgent)));
        }
    }
}
