/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Decisions.AbstractDecision;
import OCE.Decisions.BindDecision;
import OCE.Decisions.EmptyDecision;
import OCE.Medium.Recorder.IRecord;
import OCE.Messages.BindMessage;
import OCE.Messages.Message;
import OCE.Perceptions.AbstractPerception;
import OCE.Perceptions.BindPerception;
import OCE.Selection.IMessageSelection;

import java.util.ArrayList;
import java.util.Collections;
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
    public ArrayList<AbstractDecision> decide(ArrayList<Message> perceptions) {
        MyLogger.log(Level.INFO, "The Binder agent is making decisions !");


        // The list of decisions mad by the binder agent
        ArrayList<AbstractDecision> myListOfDecisions = new ArrayList<>();
        //Transform the messages to perceptions and filter to leave only the Bind Message (until now we have only two types of message that the binder agent can treat : Empty and Bind)
        ArrayList<AbstractPerception> bindingPerceptions = new ArrayList<>( perceptions.stream()
                                                                                        .filter(m -> m instanceof BindMessage)
                                                                                        .map(p -> p.toPerception(referenceResolver))
                                                                                        .collect(Collectors.toList())
                                                                             );
        if(bindingPerceptions.size()> 0){ // The binder agent received at least one bindMessage
            //Check if  the binder agent received the two messages that he supposed to receive from both agents
            //this.nbMessages += bindingPerceptions.size();
            //if (this.nbMessages <2){ // we didn't receive all the message
             //   MyLogger.log(Level.INFO,"Waiting for the second message - nbMessage " + nbMessages);

                // myListOfDecisions.add(new EmptyDecision());
           // }else{ // launch the physical binding
                bindingPerceptions.stream()
                                    .map(p -> (BindPerception) p)
                                    .collect(Collectors.toList())
                                    .forEach(p -> myListOfDecisions.add(new BindDecision(p.getEmitter(), p.getReceivers())));
             //   this.nbMessages = 0; // reinitialise the counter
            }else{ // No Bind Message were received
            // myListOfDecisions.add(new EmptyDecision());
       }

        return myListOfDecisions;
    }
}
