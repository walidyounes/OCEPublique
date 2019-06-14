/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Perceptions;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.SituationEntry;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.Decisions.AbstractDecision;
import OCE.Messages.MessageTypes;

import java.util.ArrayList;

public class BindPerception extends AbstractPerception {


    /**
     * create a bind perception
     * @param emitter    reference of the agent sending the selection message
     * @param receivers the references of the receivers of the selection message

     */
    public BindPerception(OCEAgent emitter, ArrayList<OCEAgent> receivers) {
        this.emitter = emitter;
        this.receivers = receivers;
    }


    /**
     * treat the bind message and make the suitable decision
     * @param stateConnexionAgent : the connexion's state of this service agent "Created, Connected, NotConnected, Waiting"
     * @param OCEAgentRef : the reference of the agent treating this message (its used to initialise the emitter)
     * @param localService : the information of the service of the agent that's treating this message
     * @return the decision made by the engine
     */
    @Override
    public AbstractDecision toSelfTreat(ServiceAgentConnexionState stateConnexionAgent, OCEAgent OCEAgentRef, OCService localService) {
        return null;
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
    public SituationEntry toSituationEntry() {
        return new SituationEntry((ServiceAgent) this.emitter, MessageTypes.BIND);
    }

    @Override
    public String toString() {
        return "BindPerception{" +
                "emitter=" + emitter +
                ", receivers=" + receivers +
                '}';
    }
}
