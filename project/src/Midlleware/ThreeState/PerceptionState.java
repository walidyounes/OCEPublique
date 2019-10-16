/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Midlleware.ThreeState;

import MASInfrastructure.State.IState;
import MASInfrastructure.State.LifeCycle;
import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public class PerceptionState implements IState {

    private IState nextState;
    private IPerceptionState myWayOfPerception;

    public PerceptionState(IState nextState, IPerceptionState myWayOfPerception) {
        this.nextState = nextState;
        this.myWayOfPerception = myWayOfPerception;
    }

    @Override
    public void execute(LifeCycle c) {
        //MyLogger.log(Level.INFO, " InfraAgent is in Perception state " );
        // Execute the perception method of the agent
        ArrayList<InfraMessage> infraMessages = myWayOfPerception.percept();
        // Passe the list of infraMessages to the next state
        c.shareVariable("ListPerceptions", infraMessages);
        c.setCurrentState(this.nextState);
    }

    /**
     * set the next state to go to from this state
     * @param nextState : the next state in the cyrcle
     */
    public void setNextState(IState nextState) {
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
