/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages.ARSAMessages;


import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.MockupCompo.Way;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.ARSADecisions.AgreeDecision;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;
import OCE.OCEMessages.MessageTypes;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class represents the select message sent in the third step of the ARSA protocol
 * @author Walid YOUNES
 * @version 1.0
 */
public class SelectMessage extends ARSAMessage {
    private BinderAgent binderAgent; // The reference of the binding InfrastructureAgent

    /**
     * create a selection message
     * @param emitter    reference of the agent sending the selection message
     * @param receivers the references of the receivers of the selection message
     * @param binderAgent the agent responsible of executing the physical binding
     */
    public SelectMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers, BinderAgent binderAgent) {
        this.emitter = emitter;
        this.receivers = receivers;
        this.binderAgent = binderAgent;
    }

    /**
     * get the reference of the binding agent
     * @return the reference
     */
    public BinderAgent getBinderAgent() {
        return binderAgent;
    }

    /**
     * set the reference of the binding agent
     * @param binderAgent : the reference of the binding agent
     */
    public void setBinderAgent(BinderAgent binderAgent) {
        this.binderAgent = binderAgent;
    }

    /**
     * treat the selection message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a selection message ");
        boolean mutualSelection=false;
        try{
            //Verify if the current agent selected in the previous cycles the the emitter agent that we received a select from him
            if (OCEAgentRef.getMySelectedAgent().getMyID().toString().equals(this.emitter.getMyID().toString())){
                OCELogger.log(Level.INFO, OCEAgentRef + "************"+ this.emitter +" selected each other ");
                //get the matchingID (Way) of the service that the current agent handle
                Way myServiceWay=((MockupService)localService).getWay();
                // If the agent handle a provided service, it ignores this selection message. Which means when mutual selection happen, we give the choice to the required service (it's a choice not important)
                if(myServiceWay.equals(Way.PROVIDED)){
                    mutualSelection = true;
                }
            }
        }
        catch (NullPointerException e){
            OCELogger.log(Level.WARNING, "the selected agent reference is NULL (no agent was selected !) ");
        }
        //Verify if their is mutual selection
        if ( !mutualSelection ){
            // Send a agree message to the emitter of this message
            ArrayList<OCEAgent> agreeReceivers = new ArrayList<>();
            agreeReceivers.add(this.emitter);
            AgreeDecision agreeDecision=  new AgreeDecision(OCEAgentRef, agreeReceivers);
            // Add the reference contact of the Binder Agent
            agreeDecision.setBinderAgent(this.binderAgent);
            //Deleting the other binder agent
            //I moved the code elsewhere cause it causes problem
            return agreeDecision;
        }else{
            return new DoNothingDecision();
        }

    }

    /**
     * This function is called to filter a list of messages depending on their types
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.SELECT);
    }
}
