/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.Map;

public interface IAgentSelectionStrategy {
    Map.Entry<IDAgent, ScoredCurrentSituationEntry> execute(Situation<ScoredCurrentSituationEntry> scoredCS);
}
