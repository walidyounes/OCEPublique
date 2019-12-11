/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import MOICE.connectionManager.IConnectionManager;
import OCE.ServiceConnection.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FeedbackManager implements IFeedbackManager, PropertyChangeListener {

    private IConnectionManager connectionManager;           // The connection manager component to get the list of connections proposed by OCE
    private List<Connection> connectionsBeforeAnnotation;   // the list of connections before annotating them
    private PropertyChangeSupport support;                  //Support to notify the listeners of changes that occurs to a property of this class

    /**
     * Constructor of the feedback manager
     */
    public FeedbackManager() {
        this.connectionsBeforeAnnotation = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Constructor of the feedback manager
     */
    public FeedbackManager(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.connectionsBeforeAnnotation = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Use the configuration send by ICE, compute the difference with the configuration proposed by OCE and use it to annotate the connections
     * @param OCEConfigurationPath      : the path of the file send by ICE
     * @param ICEUserConfigurationPath  : the path of the saved configuration proposed by OCE
     */
    @Override
    public void registerUserConfiguration(String OCEConfigurationPath, String ICEUserConfigurationPath) {
        //Get the list of components present in the ambient environment
        List<MockupComponent> componentsList = this.connectionManager.getListComponents();
        //Get the initial connections proposed by OCE
        List<Connection> OCEConnectionList = this.connectionManager.getListConnectionProposedOCE();

        //Save the list of connections before annotation
        Collections.copy(OCEConnectionList,this.connectionsBeforeAnnotation);
        //Create a list of connections potentially added by ICE Todo To delete
        List<Connection> ICEAddedConnectionList = new ArrayList<>();

        //Open the files that contains the configurations
        File OCEConfiguration = new File(OCEConfigurationPath);
        File ICEUserConfiguration = new File(ICEUserConfigurationPath);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

            Document documentOCEConfiguration = dbBuilder.parse(OCEConfiguration);
            Document documentICEUserConfiguration = dbBuilder.parse(ICEUserConfiguration);

            //Create the list of all component in the OCEConfiguration xml FIle
            NodeList componentsOCEConfiguration = documentOCEConfiguration.getElementsByTagName("component");

            //Create the list of all component in the ICEUserConfiguration xml FIle
            NodeList componentsICEUserConfiguration = documentICEUserConfiguration.getElementsByTagName("component");

            for (int indexOCE = 0; indexOCE < componentsOCEConfiguration.getLength(); indexOCE++){
                Node componentNode = componentsOCEConfiguration.item(indexOCE);
                if(componentNode.getNodeType() == Node.ELEMENT_NODE){
                    Element componentElement = (Element) componentNode;
                    //Search if the component exists in the USer configuration
                    String componentName = componentElement.getAttribute("Name");
                    Optional<Element> componentICEUserConfig = isComponentAvailable(componentName, componentsICEUserConfiguration);
                    //If the component is found
                    if(componentICEUserConfig.isPresent()) {
                        NodeList serviceListOCEComponent = componentElement.getElementsByTagName("service");
                        NodeList serviceListICEComponent = componentICEUserConfig.get().getElementsByTagName("service");

                        //The two service list have the same size (we don't delete services - > if the component is found they have the same services in the same order)
                        for (int indexService = 0; indexService < serviceListOCEComponent.getLength(); indexService++) {
                            Node serviceNodeOCE = serviceListOCEComponent.item(indexService);
                            Node serviceNodeICE = serviceListICEComponent.item(indexService);

                            if (serviceNodeOCE.getNodeType() == Node.ELEMENT_NODE && serviceNodeICE.getNodeType() == Node.ELEMENT_NODE) {
                                Element serviceElementOCE = (Element) serviceNodeOCE;
                                //Get the name of the service in OCE File
                                String serviceNameOCE = serviceElementOCE.getAttribute("Name");
                                //Get the matchingID of the service in OCE File
                                String serviceMatchingIDOCE = serviceElementOCE.getAttribute("matchingID");

                                //Get the name of the service in ICE File
                                Element serviceElementICE = (Element) serviceNodeICE;
                                String serviceNameICE = serviceElementICE.getAttribute("Name");
                                //Get the matchingID of the service in ICE File
                                String serviceMatchingIDICE = serviceElementICE.getAttribute("matchingID");

                                String bindOCE = "";
                                String bindICE = "";
                                String serviceType = "";
                                //If the current service is provided we are looking for a required service and vice versa
                                String serviceTypeToFind = "";

                                //Check if the service is provided or required
                                if (serviceElementOCE.getAttribute("xsi:type").contains("Provided")) {
                                    bindOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindRequired"), componentsOCEConfiguration);
                                    bindICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindRequired"), componentsICEUserConfiguration);
                                    serviceType = "PROVIDED";
                                    serviceTypeToFind = "REQUIRED";
                                } else {
                                    //It's a required service
                                    bindOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindProvided"), componentsOCEConfiguration);
                                    bindICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindProvided"), componentsICEUserConfiguration);
                                    serviceType = "REQUIRED";
                                    serviceTypeToFind = "PROVIDED";
                                }
                                if (bindOCE.equalsIgnoreCase("") && bindICE.equalsIgnoreCase("")) { // The service was an remains not connected
                                    // No connection to annotate
                                    System.out.println(componentName + "." + serviceNameOCE + " WAS and STILL NOT CONNECTED ");
                                } else {
                                    if (bindOCE.equalsIgnoreCase("") && !bindICE.equalsIgnoreCase("")) { // A connection was added by the user
                                        //Create a new connection object and add the necessary information

                                        //Get the reference to the services that make this new connection
                                        Optional<MockupService> firstService = this.getServiceByNameOwner(componentName+"."+serviceNameOCE,serviceType,serviceMatchingIDICE, componentsList);
                                        Optional<MockupService> secondService = this.getServiceByNameOwner(bindICE,serviceTypeToFind,serviceMatchingIDICE, componentsList);

                                        if(firstService.isPresent() && secondService.isPresent()) { //We found the two services which are part of the new connection
                                            //Check if one of the two services is part of a proposed connection -> if yes it means that the connection was MODIFIED Not ADDED
                                            if(isConnectionByServiceRefExists(firstService.get(), OCEConnectionList) || isConnectionByServiceRefExists(secondService.get(), OCEConnectionList) ){
                                                System.out.println("This connection IS NOT ADDED BUT A RESULT OF A MODIFIED " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);
                                            }else{
                                                //Check if we added this connection before
                                                if(ICEAddedConnectionList.stream().filter(c -> c.getFirstService().equals(firstService.isPresent()) || c.getSecondService().equals(secondService.get()) || c.getFirstService().equals(secondService.get()) || c.getSecondService().equals(firstService.get())).collect(Collectors.toList()).size() ==0){
                                                    //Create the connection object
                                                    Connection connectionAddedICE = new Connection(firstService.get(), secondService.get());
                                                    //Annotate the connection as Added
                                                    IConnectionState connectionState = new AddedConnectionState();
                                                    connectionAddedICE.setMyConnectionState(connectionState);
                                                    //Add it to the list of connections added by ICE
                                                    ICEAddedConnectionList.add(connectionAddedICE);
                                                    System.out.println("The USER ADDED this connection " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);
                                                }else{
                                                    System.out.println("Connection Already Annotated ! ");
                                                }
                                            }

                                        }
                                        else{
                                            System.out.println("Services doesn't exist = "+firstService.isPresent() + "-" + secondService.isPresent());
                                        }

                                    } else {
                                        //Search for the connection that this service is part of (we are sure that the service was connected at this point)
                                        Map.Entry<Optional<Connection>, Optional<String>> foundConnection = serviceIsPartOfConnection(OCEConnectionList, componentName, serviceNameOCE, serviceType, serviceMatchingIDOCE);

                                        if (!bindOCE.equalsIgnoreCase("") && bindICE.equalsIgnoreCase("")) { // A connection was deleted by the user
                                            System.out.println("The USER DELETED this connection " + " = = " + componentName + "." + serviceNameOCE + "-" + bindOCE);
                                            if (foundConnection.getKey().isPresent() ) {
                                                //Check if the connection have not bean annotated before
                                                if(!foundConnection.getKey().get().getMyConnectionState().isPresent()) {
                                                    //Mark the connection as REJECTED
                                                    IConnectionState connectionState = new RejectedConnectionState();
                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);
                                                }else{
                                                    //Check if the connection was annotated as modified (The other service of the connection may not have been connected by the user)
                                                    if(foundConnection.getKey().get().getMyConnectionState().get() instanceof ModifiedConnectionState ){
                                                        System.out.println("The SECOND service = "+ componentName + "." + serviceNameOCE +  " did not get connected to an other service");
                                                    }
                                                    System.out.println("Connection already been annotated as = " + foundConnection.getKey().get().getMyConnectionState().get().toString());
                                                }
                                            }
                                        } else {
                                            if (bindOCE.equalsIgnoreCase(bindICE)) { // Connection accepted by the user
                                                //System.out.println(componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE + " WAS NOT MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
                                                System.out.println("The USER ACCEPTED this connection " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);
                                                if (foundConnection.getKey().isPresent()) {
                                                    //Check if the connection have not bean annotated before
                                                    if(!foundConnection.getKey().get().getMyConnectionState().isPresent()) {
                                                        //Mark the connection as ACCEPTED
                                                        IConnectionState connectionState = new AcceptedConnectionState();
                                                        foundConnection.getKey().get().setMyConnectionState(connectionState);
                                                    }else{
                                                        System.out.println("Connection already been annotated as = " + foundConnection.getKey().get().getMyConnectionState().get().toString());
                                                    }
                                                }
                                            } else { // Connection modified by the user
                                                System.out.println(componentName + "." + serviceNameOCE + "-" + bindOCE + " WAS MODIFIED " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);
                                                if (foundConnection.getKey().isPresent()) {
                                                    //Mark the connection as MODIFIED
                                                    IConnectionState connectionState=null;

                                                    //Check if the connection was annotated before
                                                    if(foundConnection.getKey().get().getMyConnectionState().isPresent()){
                                                        //Check if the connection was annotated as modified
                                                        if(foundConnection.getKey().get().getMyConnectionState().get() instanceof ModifiedConnectionState ){
                                                            //Get the reference to the modified state
                                                            System.out.println("already annotated as modified ! ");
                                                            connectionState = foundConnection.getKey().get().getMyConnectionState().get();
                                                        }else{
                                                            //We don't check if the connection have been annotated as REJECTED cause when we treat the connection we check -> we replace the rejected by modify
                                                            //Change the annotation of the connection from rejected to modified
                                                            connectionState = new ModifiedConnectionState();
                                                            if(foundConnection.getKey().get().getMyConnectionState().get() instanceof AddedConnectionState){
                                                                System.out.println("Was annotated as ADDED, now annotated as MODIFIED! ");
                                                            }
                                                            else System.out.println("Was annotated as REJECTED, now annotated as MODIFIED! ");
                                                        }

                                                    }else {
                                                        //It's the first time this connection is been annotated
                                                        connectionState = new ModifiedConnectionState();
                                                        System.out.println("first time getting annotated as modified! ");
                                                    }
                                                    //Annotated the connection
                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);
//                                                    //If the current service is provided we are looking for a required service and vice versa
//                                                    String serviceTypeToFind = "";
//                                                    if(serviceType.equalsIgnoreCase("PROVIDED")) serviceTypeToFind = "REQUIRED";
//                                                    else serviceTypeToFind = "PROVIDED";
                                                    //Get the service to which the current service is connected to
                                                    Optional<MockupService> modifiedToService = this.getServiceByNameOwner(bindICE,serviceTypeToFind,serviceMatchingIDICE, componentsList);
                                                    if(modifiedToService.isPresent()){
                                                        //Check the rank of the service
                                                        if (foundConnection.getValue().get().equalsIgnoreCase("First")) {
                                                            ((ModifiedConnectionState)connectionState).setSecondServiceChangedTo(modifiedToService.get());
                                                            System.out.println("The FIRST service = " + componentName + "." + serviceNameOCE + "is connected to ="+ bindICE + " == " + modifiedToService);
                                                        } else { // It is second service
                                                            ((ModifiedConnectionState)connectionState).setFirstServiceChangedTo(modifiedToService.get());
                                                            System.out.println("The SECOND service = "+ bindOCE+  "is connected to ="+ bindICE + " == " + modifiedToService );
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }else{
                        //The user deleted this component from the configuration -> all the binding of this component's services are disconnected
                        System.out.println( componentName + " was deleted by the user !");
                    }
                }
            }
            //Add to the list of annotated connections the connections added by ICE
            OCEConnectionList.addAll(ICEAddedConnectionList);

            System.out.println("List OCE Proposed Connections after annotation = " + OCEConnectionList.toString());
            //Notify the listener that the feedback is computed and the connections are annotated
            this.support.firePropertyChange("AnnotatedConnection", this.connectionsBeforeAnnotation, OCEConnectionList );

            //Delete the initial connections proposed by ICE
            this.connectionManager.getListConnectionProposedOCE().clear();
            try{
                //Delete the two files after computing feedback
                OCEConfiguration.delete();
                ICEUserConfiguration.delete();

            }catch(Exception exception){
                exception.printStackTrace();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Optional<Element> isComponentAvailable(String componentName, NodeList componentsICEUserConfiguration){
        Optional<Element> foundElement = Optional.empty();
        int indexICE= 0;
        boolean found=false;
        while (indexICE < componentsICEUserConfiguration.getLength() && !found){
            Node componentNode = componentsICEUserConfiguration.item(indexICE);
            if(componentNode.getNodeType() == Node.ELEMENT_NODE){
                Element componentElement = (Element) componentNode;
                if(componentElement.getAttribute("Name").equalsIgnoreCase(componentName)){
                    foundElement = Optional.ofNullable(componentElement);
                    found=true;
                }
            }
            indexICE++;
        }
        return foundElement;
    }

    private String convertBindPathToBindName(String bindPath, NodeList componentsList){
        if(bindPath!="") {
            //Delete the symbol "//" from the the bindPath and split it to two parts
            //System.out.println(bindPath);
            bindPath = bindPath.replace("//", "");
            String[] bindPathParts = bindPath.split("/");

            //Get the index of the component
            String[] componentBind = StringUtils.split(bindPathParts[0], ".");
            String componentBindIndex = componentBind[1];

            //Get the index of the service
            String serviceBindIndex = StringUtils.split(bindPathParts[1], ".")[1];
            try {
                int indexComponent = Integer.parseInt(componentBindIndex);
                int indexService = Integer.parseInt(serviceBindIndex);
                Element componentElement = (Element) componentsList.item(indexComponent);
                Element serviceElement = (Element) ((Element) componentsList.item(indexComponent)).getElementsByTagName("service").item(indexService);

                return componentElement.getAttribute("Name") + "." + serviceElement.getAttribute("Name");
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return "";
    }

    /**
     * This function is used to seek for the connection object that the service sent as a parameter is a part of
     * @param OCEConnectionList : the list of connections proposed by OCE
     * @param componentName     : the name of the component that owns the service
     * @param serviceName       : the service name
     * @param serviceType       : the matchingID of the service (Provide or Required)
     * @param matchingID        : the matching ID of the service
     * @return the connection if it's found and the rank of the service (FIRST or SECOND)
     */
    private Map.Entry<Optional<Connection>, Optional<String>> serviceIsPartOfConnection(List<Connection> OCEConnectionList, String componentName, String serviceName, String serviceType, String matchingID){
        Optional<Connection> connectionFound = Optional.empty();
        Optional<String>    rankToFind = Optional.empty();
        //Create the string representation of the service
        String representationServiceToFind = "" + serviceName + "-" + matchingID + "-" + serviceType + "-" + componentName;

        //Search for the connection that contains the service in question
        int index =0;
        boolean found = false;
        while(index < OCEConnectionList.size() && !found){
            //Get the current connection object
            Connection currentConnection = OCEConnectionList.get(index);
            //Check if the first service of the connection is equal to the second one
            if (currentConnection.getFirstService().getStringRepresentation().equalsIgnoreCase(representationServiceToFind)){
                found = true;
                connectionFound = Optional.ofNullable(currentConnection);
                rankToFind = Optional.ofNullable("First");
            }else{
                if(currentConnection.getSecondService().getStringRepresentation().equalsIgnoreCase(representationServiceToFind)){
                    found = true;
                    connectionFound = Optional.ofNullable(currentConnection);
                    rankToFind = Optional.ofNullable("Second");
                }
            }

            index++;
        }
        return new AbstractMap.SimpleEntry<Optional<Connection>, Optional<String>>(connectionFound, rankToFind);
    }

    /**
     * Fetch in the list of components and services for the service that matches the parameter of serviceName and Component Owner name
     * @param serviceOwnerServiceName   : the name of the service and the name of the component owner
     * @param serviceType               : the type of the service (required or provided)
     * @param matchingID                : the matching Id of the service
     * @param componentList             : the list of components in the ambient environment
     * @return  : the reference to the service if it's exist, and "empty" if not
     */
    Optional<MockupService> getServiceByNameOwner(String serviceOwnerServiceName, String serviceType, String matchingID,List<MockupComponent> componentList){
        Optional<MockupService> foundService  = Optional.empty();
        //Split the name of the component owner and the name of the service
        String[] tmpSplit = StringUtils.split(serviceOwnerServiceName, ".");

        String serviceName = tmpSplit[1];
        String componentOwnerName = tmpSplit[0];

        //Iterate of the list of components
        boolean found = false;
        Iterator<MockupComponent> mockupComponentIterator = componentList.iterator();
        while(mockupComponentIterator.hasNext() && !found){
            //Get the component
            MockupComponent currentComponent = mockupComponentIterator.next();
            //If it's the component that we are looking for
            if(currentComponent.getName().equalsIgnoreCase(componentOwnerName)){
                //Check for the service if it exists in the required/Provided services list
                Optional<Iterator<OCService>> serviceIterator = Optional.empty();
                //Initialise the iterator with the one from provided/ Required service list
                if(serviceType.equalsIgnoreCase("Required")){
                    if(!currentComponent.getRequiredServices().isEmpty()){
                        //Initialise the iterator with the one of the required service list
                        serviceIterator = Optional.ofNullable(currentComponent.getRequiredServices().iterator());
                    }
                }else{
                    if(!currentComponent.getProvidedServices().isEmpty()){
                        //Initialise the iterator with the one of the provided service list
                        serviceIterator = Optional.ofNullable(currentComponent.getProvidedServices().iterator());
                    }
                }
                if(serviceIterator.isPresent()){
                    //Search through the iterator for the service
                    while (serviceIterator.get().hasNext() && !found){
                        //Get and cast the current service in the list
                        MockupService currentMockService = (MockupService) serviceIterator.get().next();
                        //Check if it's the required service in question
                        if(currentMockService.getName().equalsIgnoreCase(serviceName) &&currentMockService.getMatchingID().equalsIgnoreCase(matchingID)){
                            found = true;
                            foundService = Optional.ofNullable(currentMockService);
                        }
                    }
                }
            }
        }
        return foundService;
    }

    /**
     * Fetch in the list of connections proposed by OCE if one of them contains the service send as parameter
     * @param service                   : the ref of the service
     * @param connectionList             : the list of connections
     * @return  : false if none of the connection in the list contains the service sent as parameter, else return true
     */
    Boolean isConnectionByServiceRefExists(MockupService service,List<Connection> connectionList){

        boolean foundConnection = false;
        Iterator<Connection> connectionIterator = connectionList.iterator();
        while(connectionIterator.hasNext() && !foundConnection){
            //Get the current connection
            Connection currentConnection = connectionIterator.next();
            if(currentConnection.getFirstService().equals(service) || currentConnection.getSecondService().equals(service)) foundConnection = true;
        }
        return foundConnection;
    }


    @Override
    public void collectFeedback() {

    }

    /**
     * Add a listener for the property change
     * @param listener  : the listener for the change property
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener for the property change
     * @param listener  : the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        //we don't check if the listener exists because the function does it internally
        support.removePropertyChangeListener(listener);
    }

    /**
     * Add a listener to be informed when the feedback is computed
     *
     * @param listener : the reference to the listener
     */
    @Override
    public void addFeedbackComputedListener(PropertyChangeListener listener) {
        this.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener from the list of the entities to be informed when the feedback is computed
     *
     * @param listener : the reference to the listener
     */
    @Override
    public void removeFeedbackComputedListener(PropertyChangeListener listener) {
        this.removePropertyChangeListener(listener);
    }

    /**
     * This method gets called when a bound property is changed
     * (the property here is the file send by ICE (the new configuration, wen it's received we trigger the compute of the feedback and connection's annotation)
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //The files send by the user exists so we treat them
        String iceFilePath = "ICEConfiguration\\ICEConfiguration.ice_editor";
        String oceFilePath = "ICEConfiguration\\OCE-ICE-old.ice_editor";
        System.out.println("Compute feedback :)");
        this.registerUserConfiguration(oceFilePath, iceFilePath);
    }

    /**
     * Reset to default settings by deleting the list of OCE's proposed connections and list of components
     */
    public void resetToDefaultSettings(){
        this.connectionsBeforeAnnotation.clear();
    }
}
