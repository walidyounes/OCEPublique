/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;

public class ActionState implements IEtat {

    private IEtat nextState;
    private IActionState myWayOfAction;

    public ActionState(IEtat nextState, IActionState myWayOfAction) {
        this.nextState = nextState;
        this.myWayOfAction = myWayOfAction;
    }

    @Override
    public void execute(LifeCycle c) {

    }

    /**
     * change the current state of the cycle
     * @param nextState the next state in the cycle
     */
    public void setNextState(IEtat nextState) {
        this.nextState = nextState;
    }

    /**
     * set the strategy of action, each agent can have a different way to act upon the environment
     * @param myWayOfAction the way that the agent act
     */
    public void setMyWayOfAction(IActionState myWayOfAction) {
        this.myWayOfAction = myWayOfAction;
    }
}
