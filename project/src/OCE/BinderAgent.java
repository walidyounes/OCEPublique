/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import MASInfrastructure.Communication.IMessage;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class implement the agent responsable of binding the services associated to two ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class BinderAgent implements IPerceptionState, IDecisionState, IActionState {

    /**
     * Implement the perception process of the agent, which consist in reading the received messages
     */
    @Override
    public ArrayList<IMessage> percept() {
        MyLogger.log(Level.CONFIG, "The Binder agent is percepting the envirnment !");
        return null;
    }

    @Override
    public void setServiceAgent(ServiceAgent serviceAgent) {

    }

    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {

    }

    /**
     * Impelment the decision mechanisme of the binder agent, and produce a list of decisions
     */
    @Override
    public void decide() {
        MyLogger.log(Level.CONFIG, "The binding agent is making a decision !");
    }

    /**
     * Execute the taken decisions and affect the environement acordingly
     */
    @Override
    public void act() {
        MyLogger.log(Level.CONFIG, "The binding agent lanch the bidning procedure !");
    }

}
