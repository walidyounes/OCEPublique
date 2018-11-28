/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Recorder.IRecord;
import OCE.Messages.Message;
import OCE.Perceptions.AbstractPerception;
import OCE.Selection.IMessageSelection;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
     * get the service Agent that this component is part of
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
       // List<AbstractPerception> filtredPerceptions = perceptions.parallelStream().map(m -> m.toPerception(referenceResolver)).filter(p -> p.toSelfFilterAdvertise()).filter(p -> matching.match(p.getEmitter().getMyInfraAgent().getHandledService(),myServiceAgent.getHandledService())).collect(Collectors.toList());

        //Call the selection method to select the messages to treat
        Message messageSelected = this.selectionMessageStrategy.singleSelect(perceptions);
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
