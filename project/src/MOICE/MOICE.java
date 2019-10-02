/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.OCPlateforme.OCComponent;
import MOICE.connectionManager.ConnectionManager;
import MOICE.connectionManager.IConnectionManager;
import MOICE.deploymentManager.DeploymentManager;
import MOICE.deploymentManager.IDeploymentManager;
import MOICE.feedbackManager.FeedbackManager;
import MOICE.feedbackManager.IFeedbackManager;
import OCE.ServiceConnection.Connection;

public class MOICE implements IConnectionManager, IFeedbackManager, IDeploymentManager {

    private IConnectionManager myConnectionManager;
    private IFeedbackManager myFeedbackManager;
    private IDeploymentManager myDeploymentManager;

    /** Holder */
    private static class MOICESingletonHolder
    {
       //Unique instance not initialized in advance
        private final static MOICE instance = new MOICE();
    }

    /**
     * Acces point to the unique instance of the singleton MOICE
     **/
    public static MOICE getInstance()
    {
        return MOICESingletonHolder.instance;
    }

    /**
     * Create the MOICE middleware with pre-initialized attributes
     * @param myConnectionManager   : the connection manager component
     * @param myFeedbackManager     : the feedback manager component
     * @param myDeploymentManager   : the deployment manager component
     */
//    public MOICE(IConnectionManager myConnectionManager, IFeedbackManager myFeedbackManager, IDeploymentManager myDeploymentManager) {
//        this.myConnectionManager = myConnectionManager;
//        this.myFeedbackManager = myFeedbackManager;
//        this.myDeploymentManager = myDeploymentManager;
//    }


    /**
     * Create the MOICE middleware. (Private constructor to implement the singleton pattern)
     */
    private MOICE() {
        this.myConnectionManager = new ConnectionManager();
        this.myFeedbackManager = new FeedbackManager();
        this.myDeploymentManager = new DeploymentManager();
    }

    @Override
    public void registerComponent(OCComponent component) {
        this.myConnectionManager.registerComponent(component);
    }

    @Override
    public void registerConnection(Connection connection) {
        this.myConnectionManager.registerConnection(connection);
    }

    @Override
    public void collectConnection() {
        this.myConnectionManager.collectConnection();
    }

    @Override
    public void deployConfiguration() {
        this.myDeploymentManager.deployConfiguration();
    }

    @Override
    public void registerFeedback() {
        this.myFeedbackManager.registerFeedback();
    }

    @Override
    public void collectFeedback() {
        this.myFeedbackManager.collectFeedback();
    }
}
