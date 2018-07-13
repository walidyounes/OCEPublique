/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import Logger.MyLogger;
import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;

import java.util.logging.Level;

public class ActionState implements IEtat {

    private IEtat nextState;
    private IActionState myWayOfAction;

    public ActionState(IEtat nextState, IActionState myWayOfAction) {
        this.nextState = nextState;
        this.myWayOfAction = myWayOfAction;
    }

    @Override
    public void execute(LifeCycle c) {
        MyLogger.log(Level.INFO, " Agent is in Action state " );
        myWayOfAction.act();
        c.setCurrentState(this.nextState);
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
