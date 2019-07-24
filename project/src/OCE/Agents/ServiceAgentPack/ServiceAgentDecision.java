/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.ServiceAgentPack;

import Logger.MyLogger;
import Midlleware.ThreeState.IDecisionState;
import OCE.Agents.IDAgent;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.BestScoreEpsilonGreedy;
import OCE.Agents.ServiceAgentPack.AgentSelectionStrategies.IAgentSelectionStrategy;
import OCE.Agents.ServiceAgentPack.Learning.*;
import OCE.Decisions.AdvertiseDecision;
import OCE.Decisions.DoNothingDecision;
import OCE.Decisions.OCEDecision;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Medium.Recorder.IRecord;
import OCE.OCEMessages.OCEMessage;
import OCE.Selection.IMessageSelection;
import OCE.Unifieur.IMatching;
import OCE.Unifieur.Matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
     *  Implement the decision mechanism of the binder agent, and produce a list of decisions
     */
    @Override
    public ArrayList<OCEDecision> decide(ArrayList<InfraMessage> perception) {
        // Filter the advertisement (if they exist) and keep only those who matches
        IMatching matching = new Matching();
         /* Criteria matchingAdvertiseCriteria = new MatchingAdvertiseCriteria(myServiceAgent.getHandledService(), matching);

        ArrayList<InfraMessage> filteredMessages = matchingAdvertiseCriteria.meetCriteria(perception);

        // Todo  walid il faut la changer pour la mettre au propre
        InfraMessage infraMessageSelected;
        if(filteredMessages.size()>0){
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(filteredMessages);
        }else{
            //Call the selection method to select the messages to treat
            infraMessageSelected = this.selectionMessageStrategy.singleSelect(perception);
        }
        */
         //Create a list of decisions
        ArrayList<OCEDecision> myListOfDecisions = new ArrayList<>();

        // Check if the service agent didn't receive any messages (empty perception)
        if(perception.isEmpty()){
            if(this.myServiceAgent.getMyConnexionState().equals(ServiceAgentConnexionState.Created)){ // if the service agent was just created -> it advertises
                //Create an advertisement decision todo : this could be enhanced later on
                OCEDecision myDecision = new AdvertiseDecision(this.myServiceAgent, new ArrayList<>(), this.myServiceAgent.getHandledService());
                myListOfDecisions.add(myDecision);
            }else{
                // the agent will do nothing Todo: this part also can be enhanced later no
                myListOfDecisions.add(new DoNothingDecision());
            }
        }else{
            // ********** Version before learning
//            //Select a message to treat
//            InfraMessage infraMessageSelected;
//            IMessageSelection messageSelection = new PrioritySelection(myServiceAgent.getHandledService(), matching);
//            infraMessageSelected = messageSelection.singleSelect(perception);
//            //Treat the selected message
//            OCEMessage perceptionSelected = infraMessageSelected.toOCEMessage(referenceResolver);
//            OCEDecision myDecision = perceptionSelected.toSelfTreat(myServiceAgent.getMyConnexionState(), myServiceAgent, myServiceAgent.getHandledService());
//            myListOfDecisions.add(myDecision);

            // ********** Version After Learning
            //transform the Infrastructure messages to OCEMessages
            List<OCEMessage> OCEPerception = perception.stream().map(m -> m.toOCEMessage(referenceResolver)).collect(Collectors.toList());
            //Create the current situation
                //Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                //check if it's the start of the agent cycle
                if(this.myServiceAgent.getMyCurrentCycleNumber()==0){
                    // Start of the cycle
                    // We initialise a new current situation
                    this.myServiceAgent.setMyCurrentSituation(new Situation<CurrentSituationEntry>(OCEPerception));
                }else{
                    // we update the current one
                    Situation<CurrentSituationEntry> myCurrentSituation = new Situation<CurrentSituationEntry>(OCEPerception);
                    this.myServiceAgent.getMyCurrentSituation().getMySetAgents().putAll(myCurrentSituation.getMySetAgents());
                }
                MyLogger.log(Level.INFO,"Agent : Decision -> Current Situation = "+ this.myServiceAgent.getMyCurrentSituation().toString());
            //Check for similar Reference Situation
                //Define the threshold for selecting similar reference situation
                double threshold = 0.3;
                Map<Situation<ReferenceSituationEntry>, Double> listSimilarRS = SituationUtility.getSimilarReferenceSituations(this.myServiceAgent.getMyCurrentSituation(),this.myServiceAgent.getMyKnowledgeBase(),threshold);
                MyLogger.log(Level.INFO,"Agent : Decision -> The list of RS selected with a threshold '"+ threshold + "' = " +listSimilarRS.toString());
            //Score the current situation
                //The value used to initialise the score
                double initialValue = 0.0;
                //Using the similar reference situations score the current situation, if no RF similar found initialise the score to initialValue
                Situation<ScoredCurrentSituationEntry> myScoredCurrentSituation = SituationUtility.scoreCurrentSituation(this.myServiceAgent.getMyCurrentSituation(), listSimilarRS,initialValue);
                MyLogger.log(Level.INFO,"Agent : Decision -> The scored current situation = " + myScoredCurrentSituation.toString());

            //Select the best agent to respond to from the scored current situation
                double epsilon=0.2;
                IAgentSelectionStrategy agentSelectionStrategy = new BestScoreEpsilonGreedy(epsilon);
                Map.Entry<IDAgent, ScoredCurrentSituationEntry> bestAgent = SituationUtility.selectBestAgent(myScoredCurrentSituation,agentSelectionStrategy);
                MyLogger.log(Level.INFO," Agent : Decision -> Using the maximum score and epsilon greedy strategy ("+epsilon+"), the best agent = " + bestAgent);
                //Todo : convert the situation entry selected to OCE message
            myListOfDecisions.add(new DoNothingDecision()); // Todo : delete just for test
        }


        MyLogger.log(Level.INFO, "Agent : Decision -> List of decisions = "+ myListOfDecisions.toString() );
        return myListOfDecisions;
    }
}
