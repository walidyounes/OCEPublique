/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import Logger.MyLogger;
import MASInfrastructure.Communication.IMessage;
import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;

import java.util.ArrayList;
import java.util.logging.Level;

public class DecisionState implements IEtat {

    private IEtat nextState;
    private IDecisionState myWayOfDecision;

    public DecisionState(IEtat nextState, IDecisionState myWayOfDecision) {
        this.nextState = nextState;
        this.myWayOfDecision = myWayOfDecision;
    }

    @Override
    public void execute(LifeCycle c) {
        MyLogger.log(Level.INFO, " InfraAgent is in Decision state " );
        // Retrive the list of perceptions initialized in the previous state
        ArrayList<IMessage> messages = c.getSharedData("ListPerceptions");
        // execute the decision method of the agent
        myWayOfDecision.decide(messages);
        c.setCurrentState(this.nextState);
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
     * @param myWayOfDecision : the way that the agent decide
     */
    public void setMyWayOfDecision(IDecisionState myWayOfDecision) {
        this.myWayOfDecision = myWayOfDecision;
    }
}
