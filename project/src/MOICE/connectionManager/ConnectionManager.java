/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.connectionManager;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.OCPlateforme.OCComponent;
import MOICE.ICEXMLFormatter;
import MOICE.IFileFormatter;
import OCE.ServiceConnection.Connection;

import java.util.ArrayList;

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
     * Register the component present in the environment
     */
    @Override
    public void registerComponent(OCComponent component) {
        this.listComponents.add((MockupComponent) component);
    }

    /**
     * Register the connection proposed by the engine OCE
     */
    @Override
    public void registerConnection(Connection connection) {
        this.listConnections.add(connection);
    }

    /**
     * Collect the connection proposed by the engine OCE. This method is dedicated to ICE
     */
    @Override
    public void collectConnection() {
        this.myFileFormatter.convertFormat(this.listComponents,this.listConnections);
    }
}
