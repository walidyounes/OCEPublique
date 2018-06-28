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
    public void executer(LifeCyrcle c) {

    }

    /**
     *
     * @param nextState
     */
    public void setNextState(IEtat nextState) {
        this.nextState = nextState;
    }

    /**
     *
     * @param myWayOfDecision
     */
    public void setMyWayOfDecision(IDecisionState myWayOfDecision) {
        this.myWayOfDecision = myWayOfDecision;
    }
}
