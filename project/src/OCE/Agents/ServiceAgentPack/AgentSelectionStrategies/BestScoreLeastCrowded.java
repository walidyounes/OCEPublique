/*
 * Copyright (c) 2020.  Kahina HACID, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.Map;
import java.util.Optional;

public class BestScoreLeastCrowded implements IAgentSelectionStrategy {
    @Override
    public Optional<Map.Entry<IDAgent, ScoredCurrentSituationEntry>> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {

        return Optional.empty();
    }
}
