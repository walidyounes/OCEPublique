/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.OCPlateforme.OCComponent;
import MOICE.connectionManager.ConnectionManager;
import MOICE.connectionManager.IConnectionManager;
import MOICE.deploymentManager.DeploymentManager;
import MOICE.deploymentManager.IDeploymentManager;
import MOICE.feedbackManager.FeedbackManager;
import MOICE.feedbackManager.IFeedbackManager;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.IDAgent;
import OCE.ServiceConnection.Connection;

import java.beans.PropertyChangeListener;
import java.util.List;

public class MOICE implements IConnectionManager, IFeedbackManager, IDeploymentManager {

    private IConnectionManager myConnectionManager;
    private IFeedbackManager myFeedbackManager;
    private IDeploymentManager myDeploymentManager;
    private MOICEProbe probeFileStorage;

    /** Holder */
    private static class MOICESingletonHolder
    {
       //Unique instance initialized in advance
        private final static MOICE instance = new MOICE();
    }

    /**
     * Access point to the unique instance of the singleton MOICE
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
        this.myFeedbackManager = new FeedbackManager(this.myConnectionManager);
        this.myDeploymentManager = new DeploymentManager();
        this.probeFileStorage = new MOICEProbe();
        //launch the probing thread
        this.probeFileStorage.run();
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
    public void unRegisterComponent(OCComponent component) {
        this.myConnectionManager.unRegisterComponent(component);
    }

    @Override
    public void unRegisterConnection(Connection connection) {
        this.myConnectionManager.unRegisterConnection(connection);
    }

    @Override
    public void collectOCEProposedConfiguration() {
        this.myConnectionManager.collectOCEProposedConfiguration();
    }

    @Override
    public void deployConfiguration() {
        this.myDeploymentManager.deployConfiguration();
    }

    /**
     * Get the list of connections proposed by OCE
     *
     * @return : the reference to the list of connections
     */
    @Override
    public List<Connection> getListConnectionProposedOCE() {
        return this.myConnectionManager.getListConnectionProposedOCE();
    }

    /**
     * Get the list of components present in the ambient environment
     *
     * @return : the list of components
     */
    @Override
    public List<MockupComponent> getListComponents() {
        return this.myConnectionManager.getListComponents();
    }

    /**
     * Use the configuration send by ICE, compute the difference with the configuration proposed by OCE and use it to annotate the connections
     * @param OCEConfigurationPath      : the path of the file send by ICE
     * @param ICEUserConfigurationPath  : the path of the saved configuration proposed by OCE
     */
    @Override
    public void registerUserConfiguration(String OCEConfigurationPath, String ICEUserConfigurationPath) {
        this.myFeedbackManager.registerUserConfiguration(OCEConfigurationPath, ICEUserConfigurationPath);
    }

    @Override
    public void collectFeedback() {
        this.myFeedbackManager.collectFeedback();
    }

    /**
     * Add a listener to be informed when the feedback is computed
     *
     * @param listener : the reference to the listener
     */
    @Override
    public void addFeedbackComputedListener(PropertyChangeListener listener) {
        this.myFeedbackManager.addFeedbackComputedListener(listener);
    }

    /**
     * Remove a listener from the list of the entities to be informed when the feedback is computed
     *
     * @param listener : the reference to the listener
     */
    @Override
    public void removeFeedbackComputedListener(PropertyChangeListener listener) {
        this.myFeedbackManager.removeFeedbackComputedListener(listener);
    }

    /**
     * Get the connection manager component
     * @return  : the reference to the connection manager component
     */
    public IConnectionManager getMyConnectionManager() {
        return myConnectionManager;
    }

    /**
     * Get the feedback manager component
     * @return  :   the reference to the feedback manager component
     */
    public IFeedbackManager getMyFeedbackManager() {
        return myFeedbackManager;
    }

    /**
     * Get the deployment manager component
     * @return  : the reference to the deployment manager component
     */
    public IDeploymentManager getMyDeploymentManager() {
        return myDeploymentManager;
    }

    /**
     * Get the Probing component responsible of fetching for the information sent by ICE
     * @return  :    the reference to the probing component
     */
    public MOICEProbe getProbeFileStorage() {
        return probeFileStorage;
    }

    /**
     * Reset to default settings of the components
     */
    public void resetToDefaultSettings(){
        ((ConnectionManager)this.myConnectionManager).resetToDefaultSettings();
        ((FeedbackManager)this.myFeedbackManager).resetToDefaultSettings();
    }

    /**
     * Delete all connections that are handled by the binder agent send as a parameter
     * @param binderAgent : the binder agent that we which to delete the connections
     */
    @Override
    public void unRegisterConnectionByBinderAgent(BinderAgent binderAgent) {
        this.myConnectionManager.unRegisterConnectionByBinderAgent(binderAgent);
    }
}
