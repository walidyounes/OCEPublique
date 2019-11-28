/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Medium.Recorder.IRecord;

import java.util.Optional;
import java.util.logging.Level;

/**
 * This class implement the agent responsible of binding the services associated to two ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class BinderAgent extends OCEAgent {

    private Optional<OCService> firstService;
    private Optional<OCService> secondService;
    private IRecord oceRecord;

    public BinderAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
    }

    public BinderAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction, IRecord oceRecord) {
        this.myID = new IDAgent();
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
        this.oceRecord = oceRecord;
    }

    /**
     * Change the Id of the service agent, it's used so that the OCE agent have the same ID as the Infrastructure Agent's ID
     * @param newIDAgent : the new ID
     */
    public void setMyIDAgent(IDAgent newIDAgent){
        this.myID = newIDAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinderAgent)) return false;

        BinderAgent that = (BinderAgent) o;

        return myID.equals(that.myID);
    }

    @Override
    public String toString() {
        return this.myID.toString();
    }

    /**
     * Launch the suicide mechanism of this agent
     */
    public void suicide(){
        OCELogger.log(Level.INFO, " The agent = " + this.toString() + " is committing SUICIDE !");
        //Unregister from OCE's record witch will trigger automatically the delete from the infrastructure
        this.oceRecord.unregisterOCEAgent(this);
    }

    public Optional<OCService> getFirstService() {
        return firstService;
    }

    public void setFirstService(OCService firstService) {
        this.firstService = Optional.ofNullable(firstService);
    }

    public Optional<OCService> getSecondService() {
        return secondService;
    }

    public void setSecondService(OCService secondService) {
        this.secondService = Optional.ofNullable(secondService);
    }
}
