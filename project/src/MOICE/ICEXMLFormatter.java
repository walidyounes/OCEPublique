/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.MockupCompo.Way;
import AmbientEnvironment.OCPlateforme.OCService;
import OCE.ServiceConnection.Connection;

import java.io.*;
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

    /**
     * Create the file used by ICE for visualition with the right formatting
     * @param listComponents    : the list of available components in the environment
     * @param listConnections   : the list of connections proposed by OCE
     */
    @Override
    public void convertFormat(List<MockupComponent> listComponents, List<Connection> listConnections) {
        try{
            this.listComponents     = listComponents;
            this.listConnections    = listConnections;
            // Open the model XML file
            // BufferedWriter bw = new BufferedWriter(new FileWriter("MyLogFiles/ICE.ice_editor"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\khacid\\Downloads\\ICE-master\\ICE-master\\runtime-Editor\\runtime-Editor\\org.eclipse.iceEditor.sample\\ICE.ice_editor"));
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

                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+currentService.getName()+"\"  matchingID=\""+currentService.getMatchingID() + "\" "
                                        + "bindRequired=\"//@component."+ indexes.getKey()+
                                        "/@service."+ indexes.getValue()+"\" Cardinality=\"1\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:ProvidedService\" Name=\""+currentService.getName()+"\"  matchingID=\""+currentService.getMatchingID() +"\" Cardinality=\"1\"/>\n");
                            }
                        }
                    }
                    if (listComponents.get(k).getRequiredServices().size() != 0){
                        for(int i = 0; i< listComponents.get(k).getRequiredServices().size(); i++){
                            MockupService currentService = (MockupService) listComponents.get(k).getRequiredServices().get(i);
                            Map.Entry<Optional<MockupService>,Optional<MockupService>> mapIsConnectedConnectedTo = isConnected(currentService);
                            if(mapIsConnectedConnectedTo.getKey().isPresent()) {
                                Map.Entry<Integer, Integer> indexes = findIndexes(mapIsConnectedConnectedTo);

                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+currentService.getName()+"\"  matchingID=\""+currentService.getMatchingID()+"\" "
                                        + "bindProvided=\"//@component."+ indexes.getKey()+
                                        "/@service."+ indexes.getValue()+"\" Cardinality=\"1\"/>\n");
                            } else {
                                bw.write("<service xsi:type=\"iCE_Editor:RequiredService\" Name=\""+currentService.getName()+"\"  matchingID=\""+currentService.getMatchingID()+"\" Cardinality=\"1\"/>\n");
                            }
                        }
                    }
                    bw.write("</component>\n");
            }
            // Writing the end lines of the XML
            bw.write("</iCE_Editor:Environment> \n");

            // Saving the file
            bw.close();

            //Making the OCE Copy of the generated file

            final String ICEFilePath = "C:\\Users\\khacid\\Downloads\\ICE-master\\ICE-master\\runtime-Editor\\runtime-Editor\\org.eclipse.iceEditor.sample\\ICE.ice_editor";

            final String OCEFilePath = "ICEConfiguration\\OCE-ICE-old.ice_editor";

            try (final InputStream is = new FileInputStream(ICEFilePath);  final OutputStream os = new FileOutputStream(OCEFilePath)) {
                //Make the copy
                is.transferTo(os);
                //Close both streams
                is.close();
                os.close();
            }

            catch (IOException exception) {
                System.out.println("Error : ");
                exception.printStackTrace();
            }
        } catch (Exception e){
            System.out.println("Error : ");
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

                    List<OCService>  componentServicesList = theMockupComponent.get(0).getAllServices();

                    //System.out.println("The Component found  = "+theMockupComponent.get(0));
                    boolean foundS = false;
                    if(serviceConnectedTo.get().getWay()== Way.PROVIDED){
//                        List<OCService> listProvidedServices = theMockupComponent.get(0).getProvidedServices();
//                        while(!foundS && indexService < listProvidedServices.size()){
//
//                            MockupService currentService = (MockupService) listProvidedServices.get(indexService);
//                            System.out.println(" \n \n Current Service P = "+currentService + "\n \n");
//                            if(currentService.getName().equalsIgnoreCase(serviceConnectedTo.get().getName())) {
//                                foundS = true;
//                                System.out.println("Index of services P  = "+indexService);
//                            }else{
//                                indexService ++;
//                            }
//                        }
                        indexService = componentServicesList.indexOf(serviceConnectedTo.get());
                        //System.out.println("Index of services P  = "+indexService);
                    }else {
                        indexService = componentServicesList.indexOf(serviceConnectedTo.get());
                        //System.out.println("Index of services R  = "+indexService);
                    }
                }
            }
            return new AbstractMap.SimpleEntry<Integer, Integer>(indexComponent, indexService);
        }


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

    /**
     * Clear the content of the file used to visualisation in ICE (technically it means creating a file with empty content)
     */
    @Override
    public void clearICEFileContent() {
        try {
            // Open the model XML file
            // BufferedWriter bw = new BufferedWriter(new FileWriter("MyLogFiles/ICE.ice_editor"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\khacid\\Downloads\\ICE-master\\ICE-master\\runtime-Editor\\runtime-Editor\\org.eclipse.iceEditor.sample\\ICE.ice_editor"));
            //write the first line
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <iCE_Editor:Environment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \" xmlns:iCE_Editor=\"http://www.eclipse.org/sirius/sample/ice_editor\"> \n");

            // Writing the end lines of the XML
            bw.write("</iCE_Editor:Environment> \n");

            // Saving the file
            bw.close();

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
