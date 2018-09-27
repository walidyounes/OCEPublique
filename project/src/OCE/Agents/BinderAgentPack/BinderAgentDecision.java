/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Recorder.IRecord;
import OCE.Messages.Message;
import OCE.Perceptions.AbstractPerception;
import OCE.Selection.IMessageSelection;

import java.util.ArrayList;
import java.util.logging.Level;

public class BinderAgentDecision implements IDecisionState {

    private IMessageSelection selectionMessageStrategy;
    private BinderAgent myBinderAgent;
    private IRecord referenceResolver;

    public BinderAgentDecision(IMessageSelection selectionMessageStrategy, BinderAgent myBinderAgent, IRecord referenceResolver) {
        this.selectionMessageStrategy = selectionMessageStrategy;
        this.myBinderAgent = myBinderAgent;
        this.referenceResolver = referenceResolver;
    }

    /**
     * get the service Agent that this component is part of
     * @return
     */
    public BinderAgent getMyBinderAgent() {
        return myBinderAgent;
    }

    /**
     * Update the service Agent which this component is part of
     * @param myBinderAgent :  the service agent
     */
    public void setMyBinderAgent(BinderAgent myBinderAgent) {

        this.myBinderAgent = myBinderAgent;
    }

    /**
     * Update the component which is in charge of resolving the reference
     * @param referenceResolver : the recorder component of the medium
     */
    public void setReferenceResolver(IRecord referenceResolver) {
        this.referenceResolver = referenceResolver;
    }


    @Override
    public ArrayList<AbstractDecision> decide(ArrayList<Message> perceptions) {
        MyLogger.log(Level.INFO, "The Binder agent is making decisions !");
        //Call the selection method to select the messages to treat
        // Message messageSelected = this.selectionMessageStrategy.singleSelect(perceptions);
        //Treat the selected message
       // AbstractPerception perceptionSelected = messageSelected.toPerception(referenceResolver);
        // AbstractDecision myDecision = perceptionSelected.toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
        ArrayList<AbstractDecision> mylistOfDecisions = new ArrayList<>();
        // mylistOfDecisions.add(myDecision);
        return mylistOfDecisions;
    }
}
