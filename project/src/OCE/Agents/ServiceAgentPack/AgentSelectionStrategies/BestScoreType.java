/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack.AgentSelectionStrategies;

import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.Learning.ScoredCurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;

import java.util.Map;

public class BestScoreType implements IAgentSelectionStrategy {
    @Override
    public Map.Entry<IDAgent, ScoredCurrentSituationEntry> execute(Situation<ScoredCurrentSituationEntry> scoredCS) {
        return null;
    }
}
