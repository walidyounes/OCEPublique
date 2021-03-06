/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import Midlleware.ThreeState.IActionState;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.InfrastructureMessages.WaitingForFeedbackInfraMessage;
import OCE.OCEDecisions.OCEDecision;
import OCE.DeviceBinder.PhysicalDeviceBinder;
import OCE.Medium.Communication.ICommunicationAdapter;
import OCE.ServiceConnection.Connection;

import java.util.ArrayList;
import java.util.logging.Level;

public class BinderAgentAction implements IActionState {

    private ICommunicationAdapter communicationManager;
    private OCEAgent myBinderAgent; // the reference of the binder agent, it is used when sending the feedback message
    private int nbMessages=0;
    private OCEAgent firstServiceAgent;
    private OCEAgent secondServiceAgent;
    private OCService firstService =null;
    private OCService secondService =null;
    private PhysicalDeviceBinder physicalDeviceBinder;

    /**
     * Update the communication component
     * @param communicationManager : the component which is in charge of the communication between the agent
     */
    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }
    @Override
    public void act(ArrayList<OCEDecision> decisionsList) {
        OCELogger.log(Level.INFO, " Binder agent - "+ this.myBinderAgent +" - : Action -> ");
        if(decisionsList.size()>0){
            if(decisionsList.size()==2){ // We received the two messages that we were waiting for
                OCELogger.log(Level.INFO, "The Binder agent received two messages !");
                //Extract the information from the messages
                this.firstServiceAgent = (ServiceAgent)decisionsList.get(0).getEmitter();
                this.secondServiceAgent = (ServiceAgent)decisionsList.get(1).getEmitter();
                this.firstService = ((ServiceAgent)decisionsList.get(0).getEmitter()).getHandledService();
                this.secondService =  ((ServiceAgent)decisionsList.get(1).getEmitter()).getHandledService();
                //Save the two services in the binder agent
                ((BinderAgent)this.myBinderAgent).setFirstService(this.firstService);
                ((BinderAgent)this.myBinderAgent).setSecondService(this.secondService);

                //Launch the physical binging
                this.physicalDeviceBinder = PhysicalDeviceBinder.getInstance();
                //Register the connection in the DeviceBinder
                Connection connection = new Connection((ServiceAgent) this.firstServiceAgent, (ServiceAgent) this.secondServiceAgent,(MockupService) this.firstService, (MockupService) this.secondService,(BinderAgent) this.myBinderAgent);
                this.physicalDeviceBinder.addConnexion(connection);
                //Launch the bindings
                this.physicalDeviceBinder.bindServices(firstService, secondService);

                // reinitialise the number of received messages
                this.nbMessages =0;
//                //Simulate the feedback : send to both agent an automatic response
//                //create the message with a "ACCEPTED" response
//                FeedbackInfraMessage feedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ACCEPTED);
//                //Add the two agents as receivers for the message
//                ArrayList<OCEAgent> receivers = new ArrayList<>();
//                receivers.add(this.firstServiceAgent);
//                receivers.add(this.secondServiceAgent);
//                //send the message using the communication manager
//                this.communicationManager.sendMessage(feedbackMessage,this.myBinderAgent,receivers);

                //Send the temporary connected message to both agents
                //create the message
                WaitingForFeedbackInfraMessage waitingForFeedbackInfraMessage = new WaitingForFeedbackInfraMessage(null, null);
                //Add the two agents as receivers for the message
                ArrayList<OCEAgent> receivers = new ArrayList<>();
                receivers.add(this.firstServiceAgent);
                receivers.add(this.secondServiceAgent);
                //send the message using the communication manager
                this.communicationManager.sendMessage(waitingForFeedbackInfraMessage,this.myBinderAgent,receivers);

            }else{ // In this cycle we received only one message
                this.nbMessages += decisionsList.size();
                if(nbMessages<2){  // it may be the second one or the first one
                    OCELogger.log(Level.INFO, "The Binder agent received the first message !");
                    this.firstService = ((ServiceAgent)decisionsList.get(0).getEmitter()).getHandledService();
                    this.firstServiceAgent = (ServiceAgent)decisionsList.get(0).getEmitter();

                }else{
                    OCELogger.log(Level.INFO, "The Binder agent received the second message !");
                    this.secondService = ((ServiceAgent)decisionsList.get(0).getEmitter()).getHandledService();
                    this.secondServiceAgent = (ServiceAgent)decisionsList.get(0).getEmitter();
                    //Launch the physical binging
                    this.physicalDeviceBinder = PhysicalDeviceBinder.getInstance();
                    //Register the connection in the DeviceBinder
                    Connection connection = new Connection((ServiceAgent) this.firstServiceAgent, (ServiceAgent) this.secondServiceAgent,(MockupService) this.firstService, (MockupService) this.secondService,(BinderAgent) this.myBinderAgent);
                    this.physicalDeviceBinder.addConnexion(connection);
                    //Launch the bindings
                    this.physicalDeviceBinder.bindServices(firstService, secondService);

                    //Save the two services in the binder agent
                    ((BinderAgent)this.myBinderAgent).setFirstService(this.firstService);
                    ((BinderAgent)this.myBinderAgent).setSecondService(this.secondService);

                    // reinitialise the number of received messages
                    this.nbMessages =0;
//                    //Simulate the feedback : send to both agent an automatic response
//                    //create the message with a "ACCEPTED" response
//                    FeedbackInfraMessage feedbackMessage = new FeedbackInfraMessage(null, null, FeedbackValues.ACCEPTED);
//                    //Add the two agents as receivers for the message
//                    ArrayList<OCEAgent> receivers = new ArrayList<>();
//                    receivers.add(this.firstServiceAgent);
//                    receivers.add(this.secondServiceAgent);
//                    //send the message using the communication manager
//                    this.communicationManager.sendMessage(feedbackMessage,this.myBinderAgent,receivers);

                    //Send the temporary connected message to both agents
                        //create the message
                        WaitingForFeedbackInfraMessage waitingForFeedbackInfraMessage = new WaitingForFeedbackInfraMessage(null, null);
                        //Add the two agents as receivers for the message
                        ArrayList<OCEAgent> receivers = new ArrayList<>();
                        receivers.add(this.firstServiceAgent);
                        receivers.add(this.secondServiceAgent);
                        //send the message using the communication manager
                        this.communicationManager.sendMessage(waitingForFeedbackInfraMessage,this.myBinderAgent,receivers);
                }
            }
        }else
        {
            OCELogger.log(Level.INFO, "The binder agent didn't receive any thing in this cycle ");
        }


    }

    /**
     * Set the reference of the binder agent that encapsulate this module
     * @param myBinderAgent : the reference of the binder agent
     */
    @Override
    public void setBinderAgent(OCEAgent myBinderAgent) {
        this.myBinderAgent = myBinderAgent;
    }
}
