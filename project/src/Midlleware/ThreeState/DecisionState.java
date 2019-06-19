/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.Etat.IEtat;
import MASInfrastructure.Etat.LifeCycle;
import OCE.Decisions.OCEDecision;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public class DecisionState implements IEtat {

    private IEtat nextState;
    private IDecisionState myWayOfDecision;

    public DecisionState(IEtat nextState, IDecisionState myWayOfDecision) {
        this.nextState = nextState;
        this.myWayOfDecision = myWayOfDecision;
    }

    @Override
    public void execute(LifeCycle c) {
        //MyLogger.log(Level.INFO, " InfraAgent is in Decision state " );
        // Retrive the list of perceptions initialized in the previous state
        ArrayList<InfraMessage> infraMessages = c.getSharedData("ListPerceptions");
        // execute the decision method of the agent
        ArrayList<OCEDecision> myListOfDecisions= myWayOfDecision.decide(infraMessages);

       // MyLogger.log(Level.INFO, "List of decisions " + myListOfDecisions);
        //send the data to the next state
        c.shareVariable("ListDecisions", myListOfDecisions);
        //Move forward to the next state "Action state"
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
