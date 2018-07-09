/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

public class PerceptionState implements IEtat{

    private IEtat nextState;
    private IPerceptionState myWayOfPerception;

    public PerceptionState(IEtat nextState, IPerceptionState myWayOfPerception) {
        this.nextState = nextState;
        this.myWayOfPerception = myWayOfPerception;
    }

    @Override
    public void execute(LifeCyrcle c) {

    }

    /**
     * set the next state to go to from this state
     * @param nextState : the next state in the cyrcle
     */
    public void setNextState(IEtat nextState) {
        this.nextState = nextState;
    }

    /**
     * set the strategy of perception, each agent can have a different way to perceive
     * @param myWayOfPerception : the strategy how to perceive
     */
    public void setMyWayOfPerception(IPerceptionState myWayOfPerception) {
        this.myWayOfPerception = myWayOfPerception;
    }
}
