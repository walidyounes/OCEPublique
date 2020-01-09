/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.Map;
import java.util.Optional;

public interface IAgentSelectionStrategy {
    Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> execute(Situation<ScoredCurrentSituationEntry> scoredCS);
}
