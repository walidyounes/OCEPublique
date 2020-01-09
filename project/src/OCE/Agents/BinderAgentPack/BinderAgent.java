/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MOICE.MOICE;
import Midlleware.ThreeState.IActionState;
import Midlleware.ThreeState.IDecisionState;
import Midlleware.ThreeState.IPerceptionState;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Medium.Recorder.IRecord;

import javax.swing.text.html.Option;
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
    private int counterBeforeSuicide;
    private IRecord oceRecord;

    public BinderAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction) {
        this.myID = new IDAgent();
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
        this.counterBeforeSuicide = 2;
    }

    public BinderAgent(IPerceptionState myWayOfPerception, IDecisionState myWayOfDecision, IActionState myWayOfAction, IRecord oceRecord) {
        this.myID = new IDAgent();
        this.myWayOfPerception = myWayOfPerception;
        this.myWayOfDecision = myWayOfDecision;
        this.myWayOfAction = myWayOfAction;
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
        this.oceRecord = oceRecord;
        this.counterBeforeSuicide = 2;
    }

    /**
     * Change the Id of the service agent, it's used so that the OCE agent have the same ID as the Infrastructure Agent's ID
     * @param newIDAgent : the new ID
     */
    public void setMyIDAgent(IDAgent newIDAgent){
        this.myID = newIDAgent;
    }

    /**
     * Launch the suicide mechanism of this agent
     */
    public void suicide(){
        OCELogger.log(Level.INFO, " The binder agent = " + this.toString() + " is committing SUICIDE !");
        //Unregister from OCE's record witch will trigger automatically the delete from the infrastructure
        this.oceRecord.unregisterOCEAgent(this);
        //Delete, if exits, the connections proposed by this binder agent to be presented to ICE
        MOICE.getInstance().unRegisterConnectionByBinderAgent(this);
    }

    /**
     * Get the reference of the first service handled by this binder agent
     * @return the reference of the service if it exists, Empty otherwise
     */
    public Optional<OCService> getFirstService() {
        return firstService;
    }

    /**
     * Set the reference of the first service handled by this binder agent
     * @param firstService
     */
    public void setFirstService(OCService firstService) {
        this.firstService = Optional.ofNullable(firstService);
        //Increment the number of handled service;
        incrementServiceCounter();
    }

    /**
     * Get the reference of the second service handled by this binder agent
     * @return the reference of the service if it exists, Empty otherwise
     */
    public Optional<OCService> getSecondService() {
        return secondService;
    }

    /**
     * Set the reference of the second service handled by this binder agent
     * @param secondService
     */
    public void setSecondService(OCService secondService) {
        this.secondService = Optional.ofNullable(secondService);
        //Increment the number of handled service;
        incrementServiceCounter();
    }


    /**
     * Increment the counter of the number of services handled by this binder agent, the counter maximum value is 2.
     */
    private void incrementServiceCounter(){
        this.counterBeforeSuicide++;
        int tempValue = this.counterBeforeSuicide;
        //if the value is greater than 2, put it to 2
        this.counterBeforeSuicide = this.counterBeforeSuicide > 2 ? 2 : tempValue;
    }

    /**
     * This function is called by the service agents which this binder agent administrate their connection.
     * If the two service agent delete their service from the binder agent, the later trigger the suicide  mechanism
     */
    public void deleteMyService(OCService serviceToDelete){
        boolean found = false;
        boolean first = false; // variable to indicate if it's the second or first service

        //check if the service to delete is one of the services handled by this agents

        if (this.firstService.isPresent()){
            if(this.firstService.get().equals(serviceToDelete)){
                found = true;
                first = true;
            }
        }

        if (this.secondService.isPresent()){
            if(this.secondService.get().equals(serviceToDelete)){
                found = true;
                first = false;
            }
        }
        if (found){
            counterBeforeSuicide --;
            if(first){
                this.firstService = Optional.empty();
            }else{
                this.secondService = Optional.empty();
            }
            if(counterBeforeSuicide==0){
                //launch the suicide mechanism
                this.suicide();
            }

        }else{
            //if it's not found do nothing
            OCELogger.log(Level.WARNING, "The service to delete is not handled by this service ! ");
        }

    }

    /**
     * Add the services send in the parameters as handled services by this binder agent
     * @param firstService  :   the reference of the first service to add
     * @param secondService :   the reference of the second service to add
     * @return true in both cases : the operation succeed or the two services are already handled by this binder agent. False otherwise.
     */
    public boolean addHandledServices(MockupService firstService, MockupService secondService){
        //Check if the two attribute are empty or not
        if(!this.firstService.isPresent() && !this.secondService.isPresent()){
            //add the two services as handled by this binder agent
            this.setFirstService(firstService);
            this.setSecondService(secondService);
            return true;
        }
        else{
            //Check if this binder agent already handle this services
            if(this.firstService.isPresent() && this.secondService.isPresent()){
                return ((this.firstService.get().equals(firstService) && this.secondService.get().equals(secondService)) || (this.firstService.get().equals(secondService) && this.secondService.get().equals(firstService)) );
            }else return false;
        }
    }

    /**
     * Reset the two attributes of the handled service to "empty"
     */
    public void resetHandledServices(){
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
    }
    /**
     * Reset the set of attributes of this agent to factory settings
     */
    @Override
    public void resetToFactoryDefaultSettings() {
        this.firstService = Optional.empty();
        this.secondService = Optional.empty();
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
}
