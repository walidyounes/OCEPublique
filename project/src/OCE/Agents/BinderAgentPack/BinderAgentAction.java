/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents.BinderAgentPack;

import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import Midlleware.ThreeState.IActionState;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Decisions.AbstractDecision;
import OCE.Medium.Communication.ICommunicationAdapter;

import java.util.ArrayList;
import java.util.logging.Level;

public class BinderAgentAction implements IActionState {

    private ICommunicationAdapter communicationManager;
    private int nbMessages=0;
    private OCService firstServiceAgent=null;
    private OCService secondServiceAgent=null;

    /**
     * Update the communication component
     * @param communicationManager : the component which is in charge of the communication between the agent
     */
    @Override
    public void setCommunicationManager(ICommunicationAdapter communicationManager) {
        this.communicationManager = communicationManager;
    }
    @Override
    public void act(ArrayList<AbstractDecision> decisionsList) {
        MyLogger.log(Level.INFO, "The Binder agent is acting upon the environment !");
        this.nbMessages += decisionsList.size();
        if(nbMessages<2){ // In this cycle we received only one message (it may be the second one or the first one)
            if(firstServiceAgent==null){ // it's the first message
                firstServiceAgent = ((ServiceAgent)decisionsList.get(0).getEmitter()).getHandledService();
            }else{
                //it's the second message
                secondServiceAgent =  ((ServiceAgent)decisionsList.get(0).getEmitter()).getHandledService();
            }
        }else{

        }
    }

}
