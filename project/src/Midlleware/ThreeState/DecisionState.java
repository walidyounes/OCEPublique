/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

public class DecisionState implements IEtat {

    private IEtat nextState;
    private IDecisionState myWayOfDecision;

    public DecisionState(IEtat nextState, IDecisionState myWayOfDecision) {
        this.nextState = nextState;
        this.myWayOfDecision = myWayOfDecision;
    }

    @Override
    public void execute(LifeCyrcle c) {

    }

    /**
     *set the next state to go to from this state
     * @param nextState : the next state in the cyrcle
     */
    public void setNextState(IEtat nextState) {
        this.nextState = nextState;
    }

    /**
     * set the strategy of decision, each agent can have a different way to decide
     * @param myWayOfDecision
     */
    public void setMyWayOfDecision(IDecisionState myWayOfDecision) {
        this.myWayOfDecision = myWayOfDecision;
    }
}
