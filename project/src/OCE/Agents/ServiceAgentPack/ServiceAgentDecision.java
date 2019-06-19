/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Decisions.OCEDecision;
import OCE.Decisions.AdvertiseDecision;
import OCE.Decisions.DoNothingDecision;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.OCEMessage;
import OCE.Selection.IMessageSelection;
import OCE.Selection.PrioritySelection;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class implements the decision process of a ServiceAgent
 * @author Walid YOUNES
 * @version 1.0
 */
public class ServiceAgentDecision implements IDecisionState {

    private IMessageSelection selectionMessageStrategy;
    private ServiceAgent myServiceAgent;
    private IRecord referenceResolver;

    public ServiceAgentDecision(IMessageSelection selectionMessageStrategy, ServiceAgent myServiceAgent, IRecord referenceResolver) {
        this.selectionMessageStrategy = selectionMessageStrategy;
        this.myServiceAgent = myServiceAgent;
        this.referenceResolver = referenceResolver;
    }

    /**
     * Get the service Agent that this component is part of
     * @return
     */
    public ServiceAgent getMyServiceAgent() {
        return myServiceAgent;
    }

    /**
     * Update the service Agent which this component is part of
     * @param myServiceAgent :  the service agent
     */
    public void setMyServiceAgent(ServiceAgent myServiceAgent) {

        this.myServiceAgent = myServiceAgent;
    }

    /**
     * Update the component which is in charge of resolving the reference
     * @param referenceResolver : the recorder component of the medium
     */
    public void setReferenceResolver(IRecord referenceResolver) {
        this.referenceResolver = referenceResolver;
    }

    /**
     *  Impelment the decision mechanisme of the binder agent, and produce a list of decisions
     */
    @Override
    public ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perception) {
        // Filter the advertisement (if they exist) and keep only those who matches
        IMatching matching = new Matching();
         /* Criteria matchingAdvertiseCriteria = new MatchingAdvertiseCriteria(myServiceAgent.getHandledService(), matching);

        ArrayList<InfraMessage> filtredMessages = matchingAdvertiseCriteria.meetCriteria(perception);

        // Todo  walid il faut la chnager pour la mettre au propre
        InfraMessage infraMessageSelected;
        if(filtredMessages.size()>0){
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(filtredMessages);
        }else{
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(perception);
        }
        */
         //Create a list of decisions
        ArrayList<OCEDecision> mylistOfDecisions = new ArrayList<>();

        // Check if the service agent didn't receive any messages (empty perception)
        if(perception.isEmpty()){
            if(this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.Created)){ // if the service agent was just created -> it advertises
                //Create an advertisement decision todo : this could be enhanced later on
                OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                mylistOfDecisions.add(myDecision);
            }else{
                // the agent will do nothing Todo: this part also can be enhanced later no
                mylistOfDecisions.add(new DoNothingDecision());
            }
        }else{
            //Select a message to treat
            InfraMessage infraMessageSelected;
            IMessageSelection messageSelection = new PrioritySelection(myServiceAgent.getHandledService(), matching);
            infraMessageSelected = messageSelection.singleSelect(perception);
            //Treat the selected message
            OCEMessage perceptionSelected = infraMessageSelected.toOCEMessage(referenceResolver);
            OCEDecision myDecision = perceptionSelected.toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
            mylistOfDecisions.add(myDecision);
        }


        MyLogger.log(Level.INFO, "Agent : Decision -> List of decisions = "+ mylistOfDecisions.toString() );
        return mylistOfDecisions;
    }
}
