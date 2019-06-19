/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;
import OCE.Decisions.OCEDecision;

import java.util.ArrayList;

public class ActionState implements IEtat {

    private IEtat nextState;
    private IActionState myWayOfAction;

    public ActionState(IEtat nextState, IActionState myWayOfAction) {
        this.nextState = nextState;
        this.myWayOfAction = myWayOfAction;
    }

    @Override
    public void execute(LifeCycle c) {
        //MyLogger.log(Level.INFO, " InfraAgent is in Action state " );
        // Retrive the list of decisions made by the engine in the previous state
        ArrayList<OCEDecision> decisions = c.getSharedData("ListDecisions");
        // execute the action method of the agent
        myWayOfAction.act(decisions);
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
