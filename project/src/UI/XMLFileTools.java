/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MultiLinkMockupService;
import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.MockupCompo.Way;
import AmbientEnvironment.OCPlateforme.OCService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLFileTools {

    public static List<MockupComponent> readXMLComponentFile(String xmlFilePath){
        // List of the components contained in the file
        List<MockupComponent> listComponents = new ArrayList<>();
        try {

            //Create the XMLHandler as a Dom Document
            File fXmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            //Get the root Element -> in my case it's the ICE_Environment
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //Get all the component items
            NodeList listComponentsNodes = doc.getElementsByTagName("component");
            System.out.println("----------------------------");

            for (int componentNodeID = 0; componentNodeID < listComponentsNodes.getLength(); componentNodeID++) {

                Node componentNode = listComponentsNodes.item(componentNodeID);

                System.out.println("\nCurrent Element :" + componentNode.getNodeName());

                if (componentNode.getNodeType() == Node.ELEMENT_NODE) {
                    //Get the attributes of the component
                    Element componentElement = (Element) componentNode;
                    //Get the Name of the component
                    String componentName = componentElement.getAttribute("Name");

                    System.out.println("Name component : " + componentElement.getAttribute("Name"));
                    System.out.println("Role component : " + componentElement.getAttribute("Role"));

                    //Create the list of provided services for the component
                    ArrayList<OCService> providedServices = new ArrayList<>();
                    // Create the list of required services for the component
                    ArrayList<OCService> requiredServices = new ArrayList<>();

                    //Get the list of services of this component
                    NodeList listServicesNodes = componentElement.getElementsByTagName("service");
                    //Get the attribute of the service
                    for (int serviceNodeID = 0; serviceNodeID < listServicesNodes.getLength(); serviceNodeID++) {

                        Node serviceNode = listServicesNodes.item(serviceNodeID);

                        System.out.println("\n service :" + serviceNode.getNodeName());

                        if (serviceNode.getNodeType() == Node.ELEMENT_NODE) {


                            //Get the attributes of the service
                            Element serviceElement = (Element) serviceNode;
                            String serviceName = serviceElement.getAttribute("Name");
                            String serviceMatchingID = serviceElement.getAttribute("matchingID");
                            String serviceWay = serviceElement.getAttribute("xsi:type");
                            String serviceCardinality = serviceElement.getAttribute("Cardinality");
                            String serviceType = serviceElement.getAttribute("type");
                            String serviceCrowdedness = serviceElement.getAttribute("Crowdedness");

                            OCService serviceToAdd = null;
                            //Check if the service is a provided service
                            if( serviceWay!= "" && (serviceWay.contains("Provided") || serviceWay.contains("provided")) ) {
                                //Check if it's a single link (cardinality = 1)
                                if(serviceCardinality!="" && serviceCardinality.equals("1")) {
                                    serviceToAdd  = new SingleLinkMockupService(serviceName,serviceMatchingID, componentName, Way.PROVIDED, serviceType, serviceCrowdedness);
                                    providedServices.add(serviceToAdd);
                                }else{
                                    serviceToAdd = new  MultiLinkMockupService(serviceName,serviceMatchingID, componentName, Way.PROVIDED, serviceType);
                                    providedServices.add(serviceToAdd);
                                }

                            }
                            if( serviceWay!= "" && (serviceWay.contains("Required") || serviceWay.contains("required")) ) {
                                //Check if it's a single link (cardinality = 1)
                                if(serviceCardinality!="" && serviceCardinality.equals("1")) {
                                    serviceToAdd  = new SingleLinkMockupService(serviceName,serviceMatchingID, componentName, Way.REQUIRED, serviceType, serviceCrowdedness);
                                    requiredServices.add(serviceToAdd);
                                }else{
                                    serviceToAdd = new  MultiLinkMockupService(serviceName,serviceMatchingID, componentName, Way.REQUIRED, serviceType);
                                    requiredServices.add(serviceToAdd);
                                }
                            }
                        }
                    }
                    listComponents.add(new MockupComponent(componentName, providedServices, requiredServices));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ListXML Components " + listComponents);
        return listComponents;
    }
}
