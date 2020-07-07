/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import Logger.OCELogger;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BestScoreHighestPriority implements IAgentSelectionStrategy {
    double epsilon;

    public BestScoreHighestPriority(double epsilon) {
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

        //Get the maximum score in the situation
        Double bestScore  = (Double) scoredCS.getAgentSituationEntries().values().stream().map(e-> e.getScore()).max(Comparator.comparing(Double::doubleValue)).get();
        //Select from the list of agent with the best score
        List<IDAgent> listBestScoreAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e -> e.getScore()==bestScore).map(e -> e.getAgent()).collect(Collectors.toList());

        if(explorationProbability <= epsilon){ // Exploration : choose randomly an agent (which is probably not the best one)
            //mark that the choice is going to be done further in the co de
            chooseLater=true;
            //Extract the set from which the best agent is selected
            List<IDAgent> listNotBestScoreAgent = scoredCS.getAgentSituationEntries().values().stream().filter(e -> !listBestScoreAgent.contains(e.getAgent())).map(e-> e.getAgent()).collect(Collectors.toList());
            if(listNotBestScoreAgent.isEmpty()){ //The case where for example in the current situation we have 1 entry or n entry all with the same score
                //we will choose from the list of all agents, which is equal to list BestScore agents
                listAgentsToChooseFrom = Optional.ofNullable(listBestScoreAgent);
            }else {
                //we will choose from the agents which don't have the best score
                listAgentsToChooseFrom = Optional.ofNullable(listNotBestScoreAgent);
            }
        }else {// Exploitation -> choose the agent with the best score, if multiple exists choose the one with the highest priority, if not possible choose randomly
            if(listBestScoreAgent.size()>1){ // Refer to the priority defined by the ARSA protocol
                //Get from the list of agents with the best scores, the agents which have the highest priority (in order Agree -> Select -> Reply -> Advertise)
                int HighestPriority  = (int) scoredCS.getAgentSituationEntries().values().stream().filter(e-> listBestScoreAgent.contains(e.getAgent())).map(e-> e.getMessageType().ordinal()).max(Comparator.comparing(Integer::intValue)).get();
                System.out.println("MAX Priority = "+HighestPriority);
                List<IDAgent> listAgentHighestPriority = scoredCS.getAgentSituationEntries().values().stream().filter(e-> listBestScoreAgent.contains(e.getAgent())).filter(e -> e.getMessageType().ordinal()==HighestPriority).map(e -> e.getAgent()).collect(Collectors.toList());
                if(listAgentHighestPriority.size()>1){ // If their is multiple agents with the best score having the same priority -> choose randomly
                    chooseLater = true; // mark that we will need to choose later the best agent;
                    listAgentsToChooseFrom = Optional.ofNullable(listAgentHighestPriority);
                }else{
                    chooseLater= false; // mark that we won't need to choose later;
                    //we take the first element cause we are sure that their is only one element
                    bestAgent = Optional.ofNullable(listAgentHighestPriority.get(0));
                }
            }else{
                chooseLater= false; // mark that we won't need to choose later;
                //we take the first element cause we are sure that their is only one element
                bestAgent = Optional.ofNullable(listBestScoreAgent.get(0));
            }

        }

        if(chooseLater){
            if(listAgentsToChooseFrom.isPresent()){
                Random randomIndexG = new Random();
                int indexAgent = randomIndexG.nextInt(listAgentsToChooseFrom.get().size());
                System.out.println("random index = "+indexAgent);
                bestAgent = Optional.ofNullable(listAgentsToChooseFrom.get().get(indexAgent));
            }else{
                System.out.println("ERROR, no list of agents to choose from !");
            }

        }

        if(bestAgent.isPresent()){ // If we managed to select an agent
            return Optional.ofNullable(new AbstractMap.SimpleEntry<IDAgent, ScoredCurrentSituationEntry>(bestAgent.get(), scoredCS.getAgentSituationEntries().get(bestAgent.get())));
        }else{
            return Optional.empty();
        }
    }
}
