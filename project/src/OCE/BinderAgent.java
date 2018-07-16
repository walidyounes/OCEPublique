/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import Logger.MyLogger;
import MASInfrastructure.Agent.InfraAgent;
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
public class BinderAgent  {

    private IDAgent myID;
    private IPerceptionState myWayOfPerception;
    private IDecisionState myWayOfDecision;
    private IActionState myWayOfAction;

    public BinderAgent(IDAgent myID, IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = myID;
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
    }
}
