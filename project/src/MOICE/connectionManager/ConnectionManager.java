/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.connectionManager;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import MOICE.ICEXMLFormatter;
import MOICE.IFileFormatter;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.ServiceConnection.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionManager implements IConnectionManager {

    private ArrayList<MockupComponent> listComponents; // the list of all the component present in the environment

    private ArrayList<Connection> listConnections; // list of the connections proposed by the engine OCE

    private IFileFormatter myFileFormatter; // The formatter used to format a file in a specific format

    /**
     * Constructor of this component.
     * @param listComponents    : list of components present in the environment
     * @param listConnections   : list of the connections proposed by the engine OCE
     * @param myFileFormatter   : the custom file formatter
     */
    public ConnectionManager(ArrayList<MockupComponent> listComponents, ArrayList<Connection> listConnections, IFileFormatter myFileFormatter) {
        this.listComponents = listComponents;
        this.listConnections = listConnections;
        this.myFileFormatter = myFileFormatter;
    }

    public ConnectionManager(ArrayList<MockupComponent> listComponents, ArrayList<Connection> listConnections) {
        this.listComponents = listComponents;
        this.listConnections = listConnections;
        this.myFileFormatter = new ICEXMLFormatter();
    }

    /**
     * Constructor of this component.
     * @param myFileFormatter   : the custom file formatter
     */
    public ConnectionManager(IFileFormatter myFileFormatter) {
        this.listComponents = new ArrayList<>();
        this.listConnections = new ArrayList<>();
        this.myFileFormatter = myFileFormatter;
    }

    /**
     * Constructor of this component.
     * Every field is set to default value : the list of component and connection are empty and the custom file formatter is set to ICEXMLForamtter
     */
    public ConnectionManager() {
        this.listComponents = new ArrayList<>();
        this.listConnections = new ArrayList<>();
        this.myFileFormatter = new ICEXMLFormatter();
    }

    /**
     * Set the list of components.
     * @param listComponents    : the list of components
     */
    public void setListComponents(ArrayList<MockupComponent> listComponents) {
        this.listComponents = listComponents;
    }

    /**
     * Set the list of connections.
     * @param listConnections   : the list of connections
     */
    public void setListConnections(ArrayList<Connection> listConnections) {
        this.listConnections = listConnections;
    }

    /**
     * Register the component in the list
     * @param component : the component to register
     */
    @Override
    public void registerComponent(OCComponent component) {
        this.listComponents.add((MockupComponent) component);
    }

    /**
     * Unregister the component from the list
     * @param component : the component to unregister
     */
    @Override
    public void unRegisterComponent(OCComponent component) {
        MockupComponent deletedComponent = (MockupComponent) component;
        //Get the connections where the service of this disappearing component are part of
            for(OCService providedService : deletedComponent.getProvidedServices()){
                MockupService providedServiceToDelete = (MockupService) providedService;
                //Search in the list of registered connections, those where this service is part of
                List<Connection> connectionToDelete = this.listConnections.stream().filter(c -> c.getFirstService().equals(providedServiceToDelete) || c.getSecondService().equals(providedServiceToDelete) ).collect(Collectors.toList());
                System.out.println("" + connectionToDelete.toString());
                this.listConnections.removeIf(c -> c.getFirstService().equals(providedServiceToDelete) || c.getSecondService().equals(providedServiceToDelete));
            }
            for(OCService requiredService : deletedComponent.getRequiredServices()){
                MockupService requiredServiceToDelete = (MockupService) requiredService;
                List<Connection> connectionToDelete = this.listConnections.stream().filter(c -> c.getFirstService().equals(requiredServiceToDelete) || c.getSecondService().equals(requiredServiceToDelete) ).collect(Collectors.toList());
                System.out.println("" + connectionToDelete.toString());
                this.listConnections.removeIf(c -> c.getFirstService().equals(requiredServiceToDelete) || c.getSecondService().equals(requiredServiceToDelete));
            }

        //Delete the component from the list
        this.listComponents.remove(deletedComponent);
    }

    /**
     * Register the connection proposed by the engine OCE
     * @param connection    : the proposed connection to register
     */
    @Override
    public void registerConnection(Connection connection) {
        this.listConnections.add(connection);
    }


    /**
     * Unregister the connection proposed by the engine OCE
     * @param connection    : the proposed connection to unregister
     */
    @Override
    public void unRegisterConnection(Connection connection) {
        this.listConnections.remove(connection);
    }

    /**
     * Delete all connections that are handled by the binder agent send as a parameter
     * @param binderAgent : the binder agent that we which to delete the connections
     */
    @Override
    public void unRegisterConnectionByBinderAgent(BinderAgent binderAgent) {
        List<Connection> listConnectionsToRemove = new ArrayList<>();
        //Iterate over the connections
        for (Connection currentConnection: this.listConnections) {
            //If it's matches the criteria
            if(currentConnection.getBinderAgent().equals(binderAgent)){
                //Add it to the list of connections to remove
                listConnectionsToRemove.add(currentConnection);
                System.out.println("Deleting this connection = " + currentConnection.toString() + " --- belonging to this binder agent = " + binderAgent.toString());
            }
        }
        //Remove the connections
        this.listConnections.removeAll(listConnectionsToRemove);
    }

    /**
     * Collect the connection proposed by the engine OCE. This method is dedicated to ICE
     */
    @Override
    public void collectOCEProposedConfiguration() {
        this.myFileFormatter.convertFormat(this.listComponents,this.listConnections);
    }

    /**
     * Get the list of connections proposed by OCE
     *
     * @return : the reference to the list of connections
     */
    @Override
    public List<Connection> getListConnectionProposedOCE() {
        return this.listConnections;
    }

    /**
     * Get the list of components available in the ambient environment
     * @return the list of components
     */
    @Override
    public ArrayList<MockupComponent> getListComponents() {
        return this.listComponents;
    }

    /**
     * Reset to default settings by deleting the list of OCE's proposed connections, the list of components and the content of the ICE's visualisation file
     */
    public void resetToDefaultSettings(){
        this.listConnections.clear();
        this.listComponents.clear();
        this.myFileFormatter.clearICEFileContent();
    }
}
