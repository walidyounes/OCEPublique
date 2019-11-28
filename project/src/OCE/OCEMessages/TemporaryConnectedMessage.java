/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.OCEMessages;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.CurrentSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.OCEDecisions.DoNothingDecision;
import OCE.OCEDecisions.OCEDecision;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

public class TemporaryConnectedMessage extends OCEMessage {

    /**
     * create a bind perception
     * @param emitter    reference of the agent sending the bind message (usualy it's a binder agent)
     * @param receivers the references of the receivers of the bind message
     */
    public TemporaryConnectedMessage(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }

    /**
     * treat the message and make the suitable decision
     *
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef         : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService        : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public OCEDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        OCELogger.log(Level.INFO, OCEAgentRef + " treats a Temporary Connected message");
        //Cast the matchingID of the agent to a service agent
        ServiceAgent serviceAgent = (ServiceAgent) OCEAgentRef;
        //Change the connexion state of the service agent
        serviceAgent.setMyConnexionState(ServiceAgentConnexionState.TemporaryConnected);
        //Reset the attribute "connectedTo" to initial value (empty)
        //serviceAgent.setConnectedTo(Optional.empty());

        //The agent treating this message check if it received it from it's binder agent, if it's not the case -> it delete his binder agent (cause it's useless)
        if (((ServiceAgent) OCEAgentRef).getMyBinderAgent().isPresent()){
            //Get the binder agent
            BinderAgent binderAgent = ((ServiceAgent) OCEAgentRef).getMyBinderAgent().get();
            if(!binderAgent.equals(this.emitter)){
                OCELogger.log(Level.INFO, OCEAgentRef + "is deleting its binder agent " + ((ServiceAgent) OCEAgentRef).getMyBinderAgent().get());
                binderAgent.suicide();
                //Delete the reference of the binder agent from the service agent
                ((ServiceAgent) OCEAgentRef).deleteMyBinderAgent();
            }
            //Add in the agent treating this message the reference of the emitter as it's binder agent
            ((ServiceAgent) OCEAgentRef).setMyBinderAgent((BinderAgent) this.emitter);
        }
        return new DoNothingDecision();
    }

    /**
     * This function is called to filter a list of messages depending on their types
     *
     * @return true if this message is an advertisement message
     */
    @Override
    public Boolean toSelfFilterAdvertise() {
        return false;
    }

    /**
     * This function transform the perception to a situation entry used by the agent in the decision process (learning)
     *
     * @return the situation entry corresponding to the message
     */
    @Override
    public SituationEntry toEntrySituation() {
        return new CurrentSituationEntry(((ServiceAgent) this.emitter).getMyID(), MessageTypes.TemporaryConnected);
    }

    @Override
    public String toString() {
        return "TemporaryConnectedMessage{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
