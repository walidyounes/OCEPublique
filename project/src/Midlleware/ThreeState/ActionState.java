/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import Infrastructure.Etat.IEtat;
import Infrastructure.Etat.LifeCyrcle;

public class ActionState implements IEtat {

    private IEtat nextState;
    private IActionState myWayOfAction;

    public ActionState(IEtat nextState, IActionState myWayOfAction) {
        this.nextState = nextState;
        this.myWayOfAction = myWayOfAction;
    }

    @Override
    public void execute(LifeCyrcle c) {

    }

    /**
     *
     * @param nextState
     */
    public void setNextState(IEtat nextState) {
        this.nextState = nextState;
    }

    /**
     * set the strategy of action, each agent can have a different way to act upon the environment
     * @param myWayOfAction
     */
    public void setMyWayOfAction(IActionState myWayOfAction) {
        this.myWayOfAction = myWayOfAction;
    }
}
