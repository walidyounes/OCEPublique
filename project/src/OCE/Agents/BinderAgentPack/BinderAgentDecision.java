/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import Logger.OCELogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEDecisions.BindDecision;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.InfrastructureMessages.BindInfraMessage;
import OCE.OCEMessages.OCEMessage;
import OCE.OCEMessages.BindMessage;
import OCE.Selection.IMessageSelection;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BinderAgentDecision implements IDecisionState {

    private IMessageSelection selectionMessageStrategy;
    private BinderAgent myBinderAgent;
    private IRecord referenceResolver;
    private int nbMessages;

    public BinderAgentDecision(IMessageSelection selectionMessageStrategy, BinderAgent myBinderAgent, IRecord referenceResolver) {
        this.selectionMessageStrategy = selectionMessageStrategy;
        this.myBinderAgent = myBinderAgent;
        this.referenceResolver = referenceResolver;
        this.nbMessages = 0;
    }

    /**
     * get the reference of the binder Agent
     * @return the requested reference
     */
    public BinderAgent getMyBinderAgent() {
        return myBinderAgent;
    }

    /**
     * Update the binder agent
     * @param myBinderAgent :  the new binder agent
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
    public ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perceptions) {
        OCELogger.log(Level.INFO, " Binder agent - "+ this.myBinderAgent +" - : Decision -> ");

        // The list of decisions mad by the binder agent
        ArrayList<OCEDecision> myListOfDecisions = new ArrayList<>();
        //Transform the messages to perceptions and filter to leave only the Bind InfraMessage (until now we have only two types of message that the binder agent can treat : Empty and Bind)
        ArrayList<OCEMessage> bindingPerceptions = new ArrayList<>( perceptions.stream()
                                                                                        .filter(m -> m instanceof BindInfraMessage)
                                                                                        .map(p -> p.toOCEMessage(referenceResolver))
                                                                                        .collect(Collectors.toList())
                                                                             );
        if(bindingPerceptions.size()> 0){ // The binder agent received at least one bindMessage
            //Check if  the binder agent received the two messages that he supposed to receive from both agents
            //this.nbMessages += bindingPerceptions.size();
            //if (this.nbMessages <2){ // we didn't receive all the message
             //   OCELogger.log(Level.INFO,"Waiting for the second message - nbMessage " + nbMessages);

                // myListOfDecisions.add(new DoNothingDecision());
           // }else{ // launch the physical binding
                bindingPerceptions.stream()
                                    .map(p -> (BindMessage) p)
                                    .collect(Collectors.toList())
                                    .forEach(p -> myListOfDecisions.add(new BindDecision(p.getEmitter(), p.getReceivers())));
             //   this.nbMessages = 0; // reinitialise the counter
            }else{ // No Bind InfraMessage were received
            // myListOfDecisions.add(new DoNothingDecision());
       }

        return myListOfDecisions;
    }
}
