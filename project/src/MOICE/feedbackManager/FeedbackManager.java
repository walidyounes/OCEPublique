/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE.feedbackManager;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Optional;

public class FeedbackManager implements IFeedbackManager {

    private File OCEConfiguration;
    private File ICEUserConfiguration;

    public FeedbackManager() {
    }

    @Override
    public void registerUserConfiguration(File OCEConfiguration,File ICEUserConfiguration) {
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
                    if(componentICEUserConfig.isPresent()){
                        NodeList serviceListOCEComponent = componentElement.getElementsByTagName("service");
                        NodeList serviceListICEComponent = componentICEUserConfig.get().getElementsByTagName("service");

                        //The two service list have the same size (we don't delete services - > if the component is found they have the same services in the same order)
                        for(int indexService = 0; indexService < serviceListOCEComponent.getLength(); indexService++){
                            Node serviceNodeOCE = serviceListOCEComponent.item(indexService);
                            Node serviceNodeICE = serviceListICEComponent.item(indexService);

                            if(serviceNodeOCE.getNodeType() == Node.ELEMENT_NODE && serviceNodeICE.getNodeType() == Node.ELEMENT_NODE ){
                                Element serviceElementOCE = (Element) serviceNodeOCE;
                                Element serviceElementICE = (Element) serviceNodeICE;
                                //Check if the service is provided or required
                                if(serviceElementOCE.getAttribute("xsi:type").contains("Provided")){
                                    String bindRequiredOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindRequired"),componentsOCEConfiguration);
                                    String bindRequiredICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindRequired"),componentsICEUserConfiguration);

                                    if(bindRequiredOCE.equalsIgnoreCase(bindRequiredICE)){
                                        System.out.println(bindRequiredOCE + " was not modified " + " - " + bindRequiredICE);
                                    }else{
                                        System.out.println(bindRequiredOCE + " was modified to = " + bindRequiredICE);
                                    }
                                }else{
                                    //It's a required service
                                    String bindProvidedOCE = convertBindPathToBindName(serviceElementOCE.getAttribute("bindProvided"),componentsOCEConfiguration);
                                    String bindProvidedICE = convertBindPathToBindName(serviceElementICE.getAttribute("bindProvided"),componentsICEUserConfiguration);

                                    if(bindProvidedOCE.equalsIgnoreCase(bindProvidedICE)){
                                        System.out.println(bindProvidedOCE + " was not modified " + " - " + bindProvidedICE);
                                    }else{
                                        System.out.println(bindProvidedOCE + " was modified to = " + bindProvidedICE);
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
                    foundElement = Optional.of(componentElement);
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

    @Override
    public void collectFeedback() {

    }
}
