/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Recorder.IRecord;
import OCE.Messages.Message;
import OCE.Perceptions.AbstractPerception;
import OCE.Selection.IMessageSelection;
import OCE.Selection.PrioritySelection;
import OCE.Tools.Criteria;
import OCE.Tools.FilterTool.MatchingAdvertiseCriteria;
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
    public ArrayList<AbstractDecision> decide(ArrayList<Message> perceptions) {
        // Filter the advertisement (if they exist) and keep only those who matches
        IMatching matching = new Matching();
         /* Criteria matchingAdvertiseCriteria = new MatchingAdvertiseCriteria(myServiceAgent.getHandledService(), matching);

        ArrayList<Message> filtredMessages = matchingAdvertiseCriteria.meetCriteria(perceptions);

        // Todo  walid il faut la chnager pour la mettre au propre
        Message messageSelected;
        if(filtredMessages.size()>0){
            //Call the selection method to select the messages to treat
            messageSelected = this.selectionMessageStrategy.singleSelect(filtredMessages);
        }else{
            //Call the selection method to select the messages to treat
            messageSelected = this.selectionMessageStrategy.singleSelect(perceptions);
        }
        */
        Message messageSelected;
        IMessageSelection messageSelection = new PrioritySelection(myServiceAgent.getHandledService(), matching);
        messageSelected = messageSelection.singleSelect(perceptions);
        //Treat the selected message
        AbstractPerception perceptionSelected = messageSelected.toPerception(referenceResolver);
       // perceptionSelected = filtredPerceptions.get(0);

        AbstractDecision myDecision = perceptionSelected.toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
        ArrayList<AbstractDecision> mylistOfDecisions = new ArrayList<>();
        mylistOfDecisions.add(myDecision);
        MyLogger.log(Level.INFO, "Agent : Decision -> List of decisions = "+ mylistOfDecisions.toString() );
        return mylistOfDecisions;
    }
}
