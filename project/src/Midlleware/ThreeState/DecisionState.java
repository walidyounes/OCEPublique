/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.State.IState;
import MASInfrastructure.State.LifeCycle;
import OCE.OCEDecisions.OCEDecision;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public class DecisionState implements IState {

    private IState nextState;
    private IDecisionState myWayOfDecision;

    public DecisionState(IState nextState, IDecisionState myWayOfDecision) {
        this.nextState = nextState;
        this.myWayOfDecision = myWayOfDecision;
    }

    @Override
    public void execute(LifeCycle c) {
        //OCELogger.log(Level.INFO, " InfrastructureAgent is in Decision state " );
        // Retrieve the list of perceptions initialized in the previous state
        ArrayList<InfraMessage> infraMessages = c.getSharedData("ListPerceptions");
        // execute the decision method of the agent
        ArrayList<OCEDecision> myListOfDecisions= myWayOfDecision.decide(infraMessages);

       // OCELogger.log(Level.INFO, "List of decisions " + myListOfDecisions);
        //send the data to the next state
        c.shareVariable("ListDecisions", myListOfDecisions);
        //Move forward to the next state "Action state"
        c.setCurrentState(this.nextState);
    }

    /**
     *set the next state to go to from this state
     * @param nextState : the next state in the cyrcle
     */
    public void setNextState(IState nextState) {
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
