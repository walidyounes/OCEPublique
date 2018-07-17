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

public class PerceptionState implements IEtat{

    private IEtat nextState;
    private IPerceptionState myWayOfPerception;

    public PerceptionState(IEtat nextState, IPerceptionState myWayOfPerception) {
        this.nextState = nextState;
        this.myWayOfPerception = myWayOfPerception;
    }

    @Override
    public void execute(LifeCycle c) {
        MyLogger.log(Level.INFO, " InfraAgent is in Perception state " );
        // Execute the perception method of the agent
        ArrayList<IMessage> messages = myWayOfPerception.percept();
        // Passe the list of messages to the next state
        c.shareVariable("ListPerceptions", messages);
        c.setCurrentState(this.nextState);
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
