/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import OCE.ServiceConnection.Connection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            this.listComponents = listComponents;
            this.listConnections = listConnections;
            // Open the model XML file
            //BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/koussaifi/Desktop/Model_Test.xml"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("MyLogFiles/editor.ice_editor"));
            //write the first line
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <iCE_Editor:Environment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \" xmlns:iCE_Editor=\"http://www.eclipse.org/sirius/sample/ice_editor\"> \n");
            // Writing the XML of the model starting from the first
            for(int k = 0; k< listComponents.size(); k++){
                if(isBindedWithAnotherComponent(listComponents.get(k))){
                    bw.write("<component Name=\""+ listComponents.get(k).getName()+ "\">\n");
                    if (listComponents.get(k).getProvidedServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getProvidedServices().size(); i++){
                            MockupService serv = (MockupService) listComponents.get(k).getProvidedServices().get(i);
                            if(isConnected(serv)) {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+serv.getName()+"\" "
                                        + "bindRequired=\"//@component."+findCompNumber(listComponents.get(k), serv, "Provided", "Comp")+
                                        "/@service."+findCompNumber(listComponents.get(k), serv, "Provided", "Serv")+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+serv.getName()+"\"/>\n");
                            }
                        }
                    }
                    if (listComponents.get(k).getRequiredServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getRequiredServices().size(); i++){
                            MockupService serv = (MockupService) listComponents.get(k).getRequiredServices().get(i);
                            if(isConnected(serv)) {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+serv.getName()+"\" "
                                        + "bindProvided=\"//@component."+findCompNumber(listComponents.get(k), serv, "Required", "Comp")+
                                        "/@service."+findCompNumber(listComponents.get(k), serv, "Required", "Serv")+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+serv.getName()+"\"/>\n");
                            }
                        }
                    }
                    bw.write("</component>\n");
                }else{
                    bw.write("<component Name=\""+ listComponents.get(k).getName()+ "\">\n");
                    if (listComponents.get(k).getProvidedServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getProvidedServices().size(); i++){
                            MockupService serv = (MockupService) listComponents.get(k).getProvidedServices().get(i);
                            if(isConnected(serv)) {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+serv.getName()+"\" "
                                        + "bindRequired=\"//@component."+findCompNumber(listComponents.get(k), serv, "Provided", "Comp")+
                                        "/@service."+findCompNumber(listComponents.get(k), serv, "Provided", "Serv")+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+serv.getName()+"\"/>\n");
                            }
                        }
                    }
                    if (listComponents.get(k).getRequiredServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getRequiredServices().size(); i++){
                            MockupService serv = (MockupService) listComponents.get(k).getRequiredServices().get(i);
                            if(isConnected(serv)) {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+serv.getName()+"\" "
                                        + "bindProvided=\"//@component."+findCompNumber(listComponents.get(k), serv, "Required", "Comp")+
                                        "/@service."+findCompNumber(listComponents.get(k), serv, "Required", "Serv")+"\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+serv.getName()+"\"/>\n");
                            }
                        }
                    }
                    bw.write("</component>\n");
                }
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

//        private int findCompNumber(MockupComponent comp, MockupService serv, String serviceType, String findIndexOfWhat){
//            int index = 0;
//            for (int i = 0; i< listComponents.size(); i++){
//                if (!listComponents.get(i).getName().equalsIgnoreCase(comp.getName())){
//                    switch (serviceType) {
//                        case "Required":
//                            for(int j = 0; j< listComponents.get(i).getProvidedServices().size(); j++){
//                                MockupService provServ = (MockupService) listComponents.get(i).getProvidedServices().get(j);
//                                if(provServ.getName().equalsIgnoreCase(serv.getName())
//                                        && connectedTo(provServ).equalsIgnoreCase(comp.getName())){
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
//        }

    // Walid version
    private int findCompNumber(MockupComponent comp, MockupService serv, String serviceType, String findIndexOfWhat){
        int index = 0;
        for (int i = 0; i< listComponents.size(); i++){
            if (!listComponents.get(i).getName().equalsIgnoreCase(comp.getName())){
                switch (serviceType) {
                    case "Required":
                        for(int j = 0; j< listComponents.get(i).getProvidedServices().size(); j++){
                            MockupService provServ = (MockupService) listComponents.get(i).getProvidedServices().get(j);
                            if(provServ.equals(serv)
                                    && connectedTo(provServ).equalsIgnoreCase(comp.getName())){
                                switch (findIndexOfWhat) {
                                    case "Comp":
                                        index = i;
                                        break;

                                    case "Serv":
                                        index = j;
                                        break;
                                }
                            }
                        }
                        break;
                    case "Provided":
                        for(int j = 0; j< listComponents.get(i).getRequiredServices().size(); j++){
                            MockupService reqServ = (MockupService) listComponents.get(i).getRequiredServices().get(j);
                            if(reqServ.equals(serv)
                                    && connectedTo(reqServ).equalsIgnoreCase(comp.getName())){
                                switch (findIndexOfWhat) {
                                    case "Comp":
                                        index = i;
                                        break;

                                    case "Serv":
                                        int incrementIndex = 0;
                                        if(!listComponents.get(i).getProvidedServices().isEmpty()){
                                            incrementIndex = listComponents.get(i).getProvidedServices().size();
                                        }
                                        index = j + incrementIndex;
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
        }

        return index;
    }

        private boolean isBindedWithAnotherComponent(MockupComponent comp){
            boolean isBinded = false;
            int numberOfBindedProvidedServices = 0, numberOfBindedRequiredServices = 0;
            for(int i=0; i<comp.getProvidedServices().size(); i++){
                MockupService serv = (MockupService) comp.getProvidedServices().get(i);
                if(isConnected(serv)){
                    numberOfBindedProvidedServices++;
                }
            }
            for(int i=0; i<comp.getRequiredServices().size(); i++){
                MockupService serv = (MockupService) comp.getRequiredServices().get(i);
                if(isConnected(serv)){
                    numberOfBindedRequiredServices++;
                }
            }
            if(numberOfBindedProvidedServices > 0
                    || numberOfBindedRequiredServices > 0){
                isBinded = true;
            }
            return isBinded;
        }


//        private boolean isConnected(MockupService serv){
//            for(int i = 0; i< listConnections.size(); i++){
//                if(serv.getName().equalsIgnoreCase(listConnections.get(i).getFirstService().getName())
//                        || serv.getName().equalsIgnoreCase(listConnections.get(i).getSecondService().getName())) {
//                    System.out.println(" service is connected = " + serv.toString());
//                    return true;
//                }
//            }
//            return false;
//        }

    //walid version
    private boolean isConnected(MockupService serv){
        for(int i = 0; i< listConnections.size(); i++){
            if(listConnections.get(i).getFirstService().equals(serv)
                    || listConnections.get(i).getSecondService().equals(serv)) {
                System.out.println(" service is connected = " + serv.toString());
                return true;
            }
        }
        return false;
    }

    // Walid version
        private String connectedTo(MockupService serv){
            for(int i = 0; i< listConnections.size(); i++){
                if(listConnections.get(i).getFirstService().equals(serv)){
                    return listConnections.get(i).getFirstService().getOwner();
                }else if(listConnections.get(i).getFirstService().equals(serv)){
                    return listConnections.get(i).getSecondService().getOwner();
                }
            }
            return "";
        }

}
