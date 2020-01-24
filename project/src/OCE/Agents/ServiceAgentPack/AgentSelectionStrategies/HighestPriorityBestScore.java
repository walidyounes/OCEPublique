/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import Logger.OCELogger;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HighestPriorityBestScore implements IAgentSelectionStrategy{
    double epsilon;

    public HighestPriorityBestScore(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {
        boolean chooseLater = true;                                           // Variable to indicate whether we did choose a best agents or we didn't, if this variable is equal to "true" at the end of the algorithm we select RANDOMLY an agent from a particular set "ListAgentsToChooseFrom"
        Optional<List<IDAgent>> listAgentsToChooseFrom = Optional.empty();   //The set of agents to choose from the best agents, this set is used if the variable "chooseLater is "true"
        Optional<IDAgent> bestAgent = Optional.empty();                      //The reference of the selected best agent


        //Generate a random probability of exploration
        Random randomPE = new Random();
        double explorationProbability = randomPE.nextDouble();
        System.out.println("Exploration probability = "+explorationProbability);
        OCELogger.log(Level.INFO, "Exploration probability = "+explorationProbability);

        //Get the agents which have the maximum priority (in message type)
        int HighestPriority  = (int) scoredCS.getAgentSituationEntries().values().stream().map(e-> e.getMessageType().ordinal()).max(Comparator.comparing(Integer::intValue)).get();
        System.out.println("MAX Priority = "+HighestPriority);
        //Select all the agents with the highest priority
        List<IDAgent> listHighestPriorityAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e -> e.getMessageType().ordinal()==HighestPriority).map(e -> e.getAgent()).collect(Collectors.toList());

        if(explorationProbability <= epsilon){ // Exploration : choose randomly an agent (besides the agents with the highest priority)
            //mark that the choice is going to be done further in the code
            chooseLater=true;
            //Extract the set from which the best agent is selected
            List<IDAgent> listNotHighestPriorityAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e -> !listHighestPriorityAgent.contains(e.getAgent())).map(e-> e.getAgent()).collect(Collectors.toList());
            if(listNotHighestPriorityAgent.isEmpty()){ //The cas where for example in the current situation we have 1 entry or n entry all with the same priority
                //we will choose from the list of all agents, which is equal to list Highest Priority agents
                listAgentsToChooseFrom = Optional.ofNullable(listHighestPriorityAgent);
            }else {
                //we will choose from the agents which don't have the best score
                listAgentsToChooseFrom = Optional.ofNullable(listNotHighestPriorityAgent);
            }
        }else {// Exploitation -> choose the agent with the Highest Priority, if multiple exists choose the one with the best score, if not possible choose randomly
            if(listHighestPriorityAgent.size()>1){
                //Get from the list of agents with the the highest priority, the agents which have the best score

                //Get the maximum score of the agents that have the highest priority
                Double maximumValue = (Double) scoredCS.getAgentSituationEntries().values().stream().filter(e->listHighestPriorityAgent.contains(e.getAgent())).map(e -> e.getScore()).max(Comparator.comparing(Double::doubleValue)).get();
                //Select from the list of agent with highest priority those who have the highest score
                List<IDAgent> listBestScoreAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e->listHighestPriorityAgent.contains(e.getAgent())).filter(e -> e.getScore()==maximumValue).map(e -> e.getAgent()).collect(Collectors.toList());

                if(listBestScoreAgent.size()>1){ // If their is multiple agents with the best score having the same priority -> choose randomly
                    chooseLater = true; // mark that we will need to choose later the best agent;
                    listAgentsToChooseFrom = Optional.ofNullable(listBestScoreAgent);
                }else{
                    chooseLater= false; // mark that we won't need to choose later;
                    //we take the first element cause we are sure that their is only one element
                    bestAgent = Optional.ofNullable(listBestScoreAgent.get(0));
                }
            }else{
                chooseLater= false; // mark that we won't need to choose later;
                //we take the first element cause we are sure that their is only one element
                bestAgent = Optional.ofNullable(listHighestPriorityAgent.get(0));
            }

        }

        if(chooseLater){
            if(listAgentsToChooseFrom.isPresent()){
                Random randomIndexG = new Random();
                int indexAgent = randomIndexG.nextInt(listAgentsToChooseFrom.get().size());
                System.out.println("random index = "+indexAgent);
                bestAgent = Optional.ofNullable(listAgentsToChooseFrom.get().get(indexAgent));
            }else{
                System.out.println("ERROR, no list of agents to hoose from !");
            }

        }

        if(bestAgent.isPresent()){ // If we managed to select an agent
            return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent.get(), scoredCS.getAgentSituationEntries().get(bestAgent.get())));
        }else{
            return Optional.empty();
        }
    }
}
