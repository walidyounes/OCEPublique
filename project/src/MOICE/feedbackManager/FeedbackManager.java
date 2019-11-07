/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import OCE.ServiceConnection.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.text.html.Option;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FeedbackManager implements IFeedbackManager {


//    @Override
//    public void registerUserConfiguration(File OCEConfiguration, File ICEUserConfiguration, List<Connection> OCEConnectionList) {
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
//
//            Document documentOCEConfiguration = dbBuilder.parse(OCEConfiguration);
//            Document documentICEUserConfiguration = dbBuilder.parse(ICEUserConfiguration);
//
//            //Create the list of all component in the OCEConfiguration xml FIle
//            NodeList componentsOCEConfiguration = documentOCEConfiguration.getElementsByTagName("component");
//
//            //Create the list of all component in the ICEUserConfiguration xml FIle
//            NodeList componentsICEUserConfiguration = documentICEUserConfiguration.getElementsByTagName("component");
//
//            for (int indexOCE = 0; indexOCE < componentsOCEConfiguration.getLength(); indexOCE++){
//                Node componentNode = componentsOCEConfiguration.item(indexOCE);
//                if(componentNode.getNodeType() == Node.ELEMENT_NODE){
//                    Element componentElement = (Element) componentNode;
//                    //Search if the component exists in the USer configuration
//                    String componentName = componentElement.getAttribute("Name");
//                    Optional<Element> componentICEUserConfig = isComponentAvailable(componentName, componentsICEUserConfiguration);
//                    //If the component is found
//                    if(componentICEUserConfig.isPresent()){
//                        NodeList serviceListOCEComponent = componentElement.getElementsByTagName("service");
//                        NodeList serviceListICEComponent = componentICEUserConfig.get().getElementsByTagName("service");
//
//                        //The two service list have the same size (we don't delete services - > if the component is found they have the same services in the same order)
//                        for(int indexService = 0; indexService < serviceListOCEComponent.getLength(); indexService++){
//                            Node serviceNodeOCE = serviceListOCEComponent.item(indexService);
//                            Node serviceNodeICE = serviceListICEComponent.item(indexService);
//
//                            if(serviceNodeOCE.getNodeType() == Node.ELEMENT_NODE && serviceNodeICE.getNodeType() == Node.ELEMENT_NODE ){
//                                Element serviceElementOCE = (Element) serviceNodeOCE;
//                                //Get the name of the service in OCE File
//                                String serviceNameOCE = serviceElementOCE.getAttribute("Name");
//                                //Get the matchingID of the service in OCE File
//                                String serviceMatchingIDOCE = serviceElementOCE.getAttribute("matchingID");
//
//                                //Get the name of the service in ICE File
//                                Element serviceElementICE = (Element) serviceNodeICE;
//                                String serviceNameICE = serviceElementICE.getAttribute("Name");
//                                //Get the matchingID of the service in ICE File
//                                String serviceMatchingIDICE = serviceElementICE.getAttribute("matchingID");
//
//                                //Check if the service is provided or required
//                                if(serviceElementOCE.getAttribute("xsi:type").contains("Provided")){
//
//                                    String bindRequiredOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindRequired"),componentsOCEConfiguration);
//                                    String bindRequiredICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindRequired"),componentsICEUserConfiguration);
//
//                                    if(bindRequiredOCE.equalsIgnoreCase("") && bindRequiredICE.equalsIgnoreCase("")){ // The service was an remains not connected
//                                        // No connection to annotate
//                                        System.out.println(componentName + "."+ serviceNameOCE + " WAS and STILL NOT CONNECTED ");
//                                    }else{
//                                        if(bindRequiredOCE.equalsIgnoreCase("") && !bindRequiredICE.equalsIgnoreCase("")){ // A connection was added by the user
//                                            System.out.println("The USER ADDED this connection " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
//
//                                        }else{
//                                            //Search for the connection that this service is part of (we are sure that the service was connectd at this point)
//                                            Map.Entry<Optional<Connection>, Optional<String>> foundConnection = serviceIsPartOfConnection(OCEConnectionList, componentName, serviceNameOCE, "PROVIDED", serviceMatchingIDOCE);
//
//                                            if(!bindRequiredOCE.equalsIgnoreCase("") && bindRequiredICE.equalsIgnoreCase("")) { // A connection was deleted by the user
//                                                System.out.println("The USER DELETED this connection " + " = = " + componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE);
//                                                if (foundConnection.getKey().isPresent()){
//                                                    //Mark the connection as REJECTED
//                                                    IConnectionState connectionState = new RejectedConnectionState();
//                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);
//                                                }
//                                            }else{
//                                                if(bindRequiredOCE.equalsIgnoreCase(bindRequiredICE)){ // Connection accepted by the user
//                                                    //System.out.println(componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE + " WAS NOT MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
//                                                    System.out.println("The USER ACCEPTED this connection " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
//
//                                                    if (foundConnection.getKey().isPresent()){
//                                                        //Mark the connection as ACCEPTED
//                                                        IConnectionState connectionState = new AcceptedConnectionState();
//                                                        foundConnection.getKey().get().setMyConnectionState(connectionState);
//                                                    }
//                                                }else{ // Connection modified by the user
//                                                    System.out.println(componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE + " WAS MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
//                                                    //Todo : walid 03/11/2019 : this part is still to be addressed
//                                                    if (foundConnection.getKey().isPresent()){
//                                                        //Mark the connection as MODIFIED
//                                                        IConnectionState connectionState = new ModifiedConnectionState();
//                                                        foundConnection.getKey().get().setMyConnectionState(connectionState);
//
//                                                        //Check the rank of the service
//                                                        if(foundConnection.getValue().get().equalsIgnoreCase("First")){
//                                                            //Check the second service
//                                                        }else{ // It is second service
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                }else{
//                                    //It's a required service
//                                    String bindProvidedOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindProvided"),componentsOCEConfiguration);
//                                    String bindProvidedICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindProvided"),componentsICEUserConfiguration);
//
//                                    if(bindProvidedOCE.equalsIgnoreCase("") && bindProvidedICE.equalsIgnoreCase("")){ // The service was an remains not connected
//                                        // No connection to annotate
//                                        System.out.println(componentName + "."+ serviceNameOCE + " WAS and STILL NOT CONNECTED ");
//                                    }else{
//                                        if(bindProvidedOCE.equalsIgnoreCase("") && !bindProvidedICE.equalsIgnoreCase("")){ // A connection was added by the user
//                                            System.out.println("The USER ADDED this connection " + " = = " + componentName + "."+ serviceNameICE + "-" + bindProvidedICE);
//
//                                        }else{
//                                            //Search for the connection that this service is part of
//                                            Map.Entry<Optional<Connection>, Optional<String>> foundConnection = serviceIsPartOfConnection(OCEConnectionList, componentName, serviceNameOCE, "REQUIRED", serviceMatchingIDOCE);
//
//                                            if(!bindProvidedOCE.equalsIgnoreCase("") && bindProvidedICE.equalsIgnoreCase("")) { // A connection was deleted by the user
//                                                System.out.println("The USER DELETED this connection " + " = = " + componentName + "."+ serviceNameOCE + "-" + bindProvidedOCE);
//                                                if (foundConnection.getKey().isPresent()){
//                                                    //Mark the connection as REJECTED
//                                                    IConnectionState connectionState = new RejectedConnectionState();
//                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);
//                                                }
//                                            }else{
//                                                if(bindProvidedOCE.equalsIgnoreCase(bindProvidedICE)){ // Connection accepted by the user
//                                                    //System.out.println(componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE + " WAS NOT MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
//                                                    System.out.println("The USER ACCEPTED this connection " + " = = " + componentName + "."+ serviceNameICE + "-" + bindProvidedICE);
//
//                                                    if (foundConnection.getKey().isPresent()){
//                                                        //Mark the connection as ACCEPTED
//                                                        IConnectionState connectionState = new AcceptedConnectionState();
//                                                        foundConnection.getKey().get().setMyConnectionState(connectionState);
//                                                    }
//                                                }else{ // Connection modified by the user
//                                                    System.out.println(componentName + "."+ serviceNameOCE + "-" + bindProvidedOCE + " WAS MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindProvidedICE);
//                                                    //Todo : walid 03/11/2019 : this part is still to be addressed
//                                                    if (foundConnection.getKey().isPresent()){
//                                                        //Mark the connection as MODIFIED
//                                                        IConnectionState connectionState = new ModifiedConnectionState();
//                                                        foundConnection.getKey().get().setMyConnectionState(connectionState);
//
//                                                        //Check the rank of the service
//                                                        if(foundConnection.getValue().get().equalsIgnoreCase("First")){
//                                                            //Check the second service
//                                                        }else{ // It is second service
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
////                                    if(bindProvidedOCE.equalsIgnoreCase(bindProvidedICE)){
////                                        System.out.println(componentName + "."+ serviceNameOCE + "-" + bindProvidedOCE + " WAS NOT MODIFIED " + " = = " +  componentName + "."+ serviceNameICE + "-" + bindProvidedICE);
////                                        //System.out.println(bindProvidedOCE + " was not modified " + " - " + bindProvidedICE);
////                                        if (foundConnection.getKey().isPresent()){
////                                            //Mark the connection as ACCEPTED
////                                            IConnectionState connectionState = new AcceptedConnectionState();
////                                            foundConnection.getKey().get().setMyConnectionState(connectionState);
////                                        }
////                                    }else{
////                                        System.out.println(componentName + "."+ serviceNameOCE + "-" + bindProvidedOCE + " WAS MODIFIED " + " = = " +  componentName + "."+ serviceNameICE + "-" + bindProvidedICE);
////                                        // System.out.println(bindProvidedOCE + " was modified to = " + bindProvidedICE);
////                                    }
//                                }
//                            }
//
//                        }
//
//                    }else{
//                        //The user deleted this component from the configuration -> all the binding of this component's services are disconnected
//                        System.out.println( componentName + " was deleted by the user !");
//                    }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    @Override
    public void registerUserConfiguration(File OCEConfiguration, File ICEUserConfiguration, List<Connection> OCEConnectionList) {
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
                                //Check if the service is provided or required
                                if (serviceElementOCE.getAttribute("xsi:type").contains("Provided")) {
                                    bindOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindRequired"), componentsOCEConfiguration);
                                    bindICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindRequired"), componentsICEUserConfiguration);
                                    serviceType = "PROVIDED";
                                } else {
                                    //It's a required service
                                    bindOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindProvided"), componentsOCEConfiguration);
                                    bindICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindProvided"), componentsICEUserConfiguration);
                                    serviceType = "REQUIRED";
                                }
                                if (bindOCE.equalsIgnoreCase("") && bindICE.equalsIgnoreCase("")) { // The service was an remains not connected
                                    // No connection to annotate
                                    System.out.println(componentName + "." + serviceNameOCE + " WAS and STILL NOT CONNECTED ");
                                } else {
                                    if (bindOCE.equalsIgnoreCase("") && !bindICE.equalsIgnoreCase("")) { // A connection was added by the user
                                        System.out.println("The USER ADDED this connection " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);

                                    } else {
                                        //Search for the connection that this service is part of (we are sure that the service was connected at this point)
                                        Map.Entry<Optional<Connection>, Optional<String>> foundConnection = serviceIsPartOfConnection(OCEConnectionList, componentName, serviceNameOCE, serviceType, serviceMatchingIDOCE);

                                        if (!bindOCE.equalsIgnoreCase("") && bindICE.equalsIgnoreCase("")) { // A connection was deleted by the user
                                            System.out.println("The USER DELETED this connection " + " = = " + componentName + "." + serviceNameOCE + "-" + bindOCE);
                                            if (foundConnection.getKey().isPresent()) {
                                                //Mark the connection as REJECTED
                                                IConnectionState connectionState = new RejectedConnectionState();
                                                foundConnection.getKey().get().setMyConnectionState(connectionState);
                                            }
                                        } else {
                                            if (bindOCE.equalsIgnoreCase(bindICE)) { // Connection accepted by the user
                                                //System.out.println(componentName + "."+ serviceNameOCE + "-" + bindRequiredOCE + " WAS NOT MODIFIED " + " = = " + componentName + "."+ serviceNameICE + "-" + bindRequiredICE);
                                                System.out.println("The USER ACCEPTED this connection " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);

                                                if (foundConnection.getKey().isPresent()) {
                                                    //Mark the connection as ACCEPTED
                                                    IConnectionState connectionState = new AcceptedConnectionState();
                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);
                                                }
                                            } else { // Connection modified by the user
                                                System.out.println(componentName + "." + serviceNameOCE + "-" + bindOCE + " WAS MODIFIED " + " = = " + componentName + "." + serviceNameICE + "-" + bindICE);
                                                //Todo : walid 03/11/2019 : this part is still to be addressed
                                                if (foundConnection.getKey().isPresent()) {
                                                    //Mark the connection as MODIFIED
                                                    IConnectionState connectionState = new ModifiedConnectionState();
                                                    foundConnection.getKey().get().setMyConnectionState(connectionState);

                                                    //Check the rank of the service
                                                    if (foundConnection.getValue().get().equalsIgnoreCase("First")) {
                                                        //Check the second service
                                                    } else { // It is second service

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

    @Override
    public void collectFeedback() {

    }
}
