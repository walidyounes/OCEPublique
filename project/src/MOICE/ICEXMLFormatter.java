/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.MockupCompo.Way;
import OCE.ServiceConnection.Connection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ICEXMLFormatter implements IFileFormatter {

    List<MockupComponent> listComponents;
    List<Connection> listConnections;

    /**
     * Construct the custom file formatter used by ICE.
     * @param listComponents
     * @param listConnections
     */
//    public ICEXMLFormatter(ArrayList<MockupComponent> listComponents, ArrayList<Connection> listConnections){
//        this.listComponents = listComponents;
//        this.listConnections = listConnections;
//    }

    /**
     * Construct the custom file formatter used by ICE.
     */
    public ICEXMLFormatter() {
    }

//    /**
//     * Register the list of component present in the environment
//     * @param listComponents    : list of components
//     */
//    public void setListComponents(ArrayList<MockupComponent> listComponents) {
//        this.listComponents = listComponents;
//    }
//
//    /**
//     * Register the list of connections proposed by OCE.
//     * @param listConnections   : list of the connections.
//     */
//    public void setListConnections(ArrayList<Connection> listConnections) {
//        this.listConnections = listConnections;
//    }

    @Override
    public void convertFormat(List<MockupComponent> listComponents, List<Connection> listConnections) {
        try{
            this.listComponents     = listComponents;
            this.listConnections    = listConnections;
            // Open the model XML file
            //BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/koussaifi/Desktop/Model_Test.xml"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("MyLogFiles/editor.ice_editor"));
            //write the first line
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <iCE_Editor:Environment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \" xmlns:iCE_Editor=\"http://www.eclipse.org/sirius/sample/ice_editor\"> \n");
            // Writing the XML of the model starting from the first
            for(int k = 0; k< listComponents.size(); k++){
                    bw.write("<component Name=\""+ listComponents.get(k).getName()+ "\">\n");
                    if (listComponents.get(k).getProvidedServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getProvidedServices().size(); i++){
                            MockupService currentService = (MockupService) listComponents.get(k).getProvidedServices().get(i);
                            Map.Entry<Optional<MockupService>,Optional<MockupService>> mapIsConnectedConnectedTo = isConnected(currentService);
                            if(mapIsConnectedConnectedTo.getKey().isPresent()) {
                                Map.Entry<Integer, Integer> indexes = findIndexes(mapIsConnectedConnectedTo);

                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+currentService.getName()+"\" "
                                        + "bindRequired=\"//@component."+ indexes.getKey()+
                                        "/@service."+ indexes.getValue()+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+currentService.getName()+"\"/>\n");
                            }
                        }
                    }
                    if (listComponents.get(k).getRequiredServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getRequiredServices().size(); i++){
                            MockupService currentService = (MockupService) listComponents.get(k).getRequiredServices().get(i);
                            Map.Entry<Optional<MockupService>,Optional<MockupService>> mapIsConnectedConnectedTo = isConnected(currentService);
                            if(mapIsConnectedConnectedTo.getKey().isPresent()) {
                                Map.Entry<Integer, Integer> indexes = findIndexes(mapIsConnectedConnectedTo);

                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+currentService.getName()+"\" "
                                        + "bindProvided=\"//@component."+ indexes.getKey()+
                                        "/@service."+ indexes.getValue()+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+currentService.getName()+"\"/>\n");
                            }
                        }
                    }
                    bw.write("</component>\n");
            }
            // Writing the endlines of the XML
            bw.write("</iCE_Editor:Environment> \n");

            // Saving the file
            bw.close();
        } catch (Exception e){
            System.out.println("Error : ");
            // System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

        private Map.Entry<Integer, Integer> findIndexes(Map.Entry<Optional<MockupService>,Optional<MockupService>> mapIsConnectedConnectedTo){
            int indexComponent = 0;
            int indexService = 0;
            //Get the service with whom the service "serv" is connected to (it may be null)
            Optional<MockupService> serviceConnectedTo = mapIsConnectedConnectedTo.getValue();
            if(serviceConnectedTo.isPresent()){
                List<MockupComponent> theMockupComponent = this.listComponents.stream().filter(c -> c.getName().equalsIgnoreCase(serviceConnectedTo.get().getOwner())).collect(Collectors.toList());
                if(!theMockupComponent.isEmpty()){ // if the list is not empty it contains only one component
                    indexComponent = this.listComponents.indexOf(theMockupComponent.get(0));
                    if(serviceConnectedTo.get().getWay()== Way.PROVIDED){
                        indexService = theMockupComponent.get(0).getProvidedServices().indexOf(serviceConnectedTo.get());
                    }else {
                        indexService = theMockupComponent.get(0).getRequiredServices().indexOf(serviceConnectedTo.get());
                    }
                }
            }
            return new AbstractMap.SimpleEntry<Integer, Integer>(indexComponent, indexService);

//            for (int i = 0; i< listComponents.size(); i++){
//                if (!listComponents.get(i).equals(comp)){
//                    switch (serviceType) {
//                        case "Required":
//                            for(int j = 0; j< listComponents.get(i).getProvidedServices().size(); j++){
//                                MockupService provService = (MockupService) listComponents.get(i).getProvidedServices().get(j);
//                                if(
//                                        && connectedTo(provService).equalsIgnoreCase(comp.getName())){
//                                    switch (findIndexOfWhat) {
//                                        case "Comp":
//                                            index = i;
//                                            break;
//
//                                        case "Serv":
//                                            index = j;
//                                            break;
//                                    }
//                                }
//                            }
//                            break;
//                        case "Provided":
//                            for(int j = 0; j< listComponents.get(i).getRequiredServices().size(); j++){
//                                MockupService reqServ = (MockupService) listComponents.get(i).getRequiredServices().get(j);
//                                if(reqServ.getName().equalsIgnoreCase(serv.getName())
//                                        && connectedTo(reqServ).equalsIgnoreCase(comp.getName())){
//                                    switch (findIndexOfWhat) {
//                                        case "Comp":
//                                            index = i;
//                                            break;
//
//                                        case "Serv":
//                                            int incrementIndex = 0;
//                                            if(!listComponents.get(i).getProvidedServices().isEmpty()){
//                                                incrementIndex = listComponents.get(i).getProvidedServices().size();
//                                            }
//                                            index = j + incrementIndex;
//                                            break;
//                                    }
//                                }
//                            }
//                            break;
//                    }
//                }
//            }
//
//            return index;
        }

//    // Walid version
//    private int findIndexes(MockupComponent comp, MockupService serv, String serviceType, String findIndexOfWhat){
//        int index = 0;
//        for (int i = 0; i< listComponents.size(); i++){
//            if (!listComponents.get(i).getName().equalsIgnoreCase(comp.getName())){
//                switch (serviceType) {
//                    case "Required":
//                        for(int j = 0; j< listComponents.get(i).getProvidedServices().size(); j++){
//                            MockupService provServ = (MockupService) listComponents.get(i).getProvidedServices().get(j);
//                            if(provServ.equals(serv)
//                                    && connectedTo(provServ).equalsIgnoreCase(comp.getName())){
//                                switch (findIndexOfWhat) {
//                                    case "Comp":
//                                        index = i;
//                                        break;
//
//                                    case "Serv":
//                                        index = j;
//                                        break;
//                                }
//                            }
//                        }
//                        break;
//                    case "Provided":
//                        for(int j = 0; j< listComponents.get(i).getRequiredServices().size(); j++){
//                            MockupService reqServ = (MockupService) listComponents.get(i).getRequiredServices().get(j);
//                            if(reqServ.equals(serv)
//                                    && connectedTo(reqServ).equalsIgnoreCase(comp.getName())){
//                                switch (findIndexOfWhat) {
//                                    case "Comp":
//                                        index = i;
//                                        break;
//
//                                    case "Serv":
//                                        int incrementIndex = 0;
//                                        if(!listComponents.get(i).getProvidedServices().isEmpty()){
//                                            incrementIndex = listComponents.get(i).getProvidedServices().size();
//                                        }
//                                        index = j + incrementIndex;
//                                        break;
//                                }
//                            }
//                        }
//                        break;
//                }
//            }
//        }
//
//        return index;
//    }
//
//        private boolean isBindedWithAnotherComponent(MockupComponent comp){
//            boolean isBinded = false;
//            int numberOfBindedProvidedServices = 0, numberOfBindedRequiredServices = 0;
//            for(int i=0; i<comp.getProvidedServices().size(); i++){
//                MockupService serv = (MockupService) comp.getProvidedServices().get(i);
//                if(isConnected(serv)){
//                    numberOfBindedProvidedServices++;
//                }
//            }
//            for(int i=0; i<comp.getRequiredServices().size(); i++){
//                MockupService serv = (MockupService) comp.getRequiredServices().get(i);
//                if(isConnected(serv)){
//                    numberOfBindedRequiredServices++;
//                }
//            }
//            if(numberOfBindedProvidedServices > 0
//                    || numberOfBindedRequiredServices > 0){
//                isBinded = true;
//            }
//            return isBinded;
//        }

    private Map.Entry<Optional<MockupService>,Optional<MockupService>> isConnected(MockupService service){
        Optional<MockupService> mockupService = Optional.empty();
        Optional<MockupService> mockupServiceConnectedTo = Optional.empty();

        for(int i = 0; i< listConnections.size(); i++){
            if(listConnections.get(i).containService(service)) {
                System.out.println(" service is connected = " + listConnections.get(i).toString());
                mockupService = Optional.of(service);
                if(listConnections.get(i).getFirstService().equals(service)){
                    mockupServiceConnectedTo = Optional.of(listConnections.get(i).getSecondService());
                }else{
                    mockupServiceConnectedTo = Optional.of(listConnections.get(i).getFirstService());
                }

                return new AbstractMap.SimpleEntry(mockupService, mockupServiceConnectedTo);
            }
        }
        return new AbstractMap.SimpleEntry(mockupService, mockupServiceConnectedTo);
    }


//        private String connectedTo(MockupService serv){
//            for(int i = 0; i< listConnections.size(); i++){
//                if(serv.getName().equalsIgnoreCase(listConnections.get(i).getFirstService().getName())){
//                    return listConnections.get(i).getFirstService().getOwner();
//                }else if(serv.getName().equalsIgnoreCase(listConnections.get(i).getSecondService().getName())){
//                    return listConnections.get(i).getSecondService().getOwner();
//                }
//            }
//            return "";
//        }

}
