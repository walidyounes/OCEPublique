/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MASInfrastructure.Infrastructure;
import OCE.Agents.OCEAgent;
import OCE.DeviceBinder.PhysicalDeviceBinder;
import OCE.OCE;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class UIMockupController implements Initializable {
    @FXML private JFXTextField designationComponent;
    @FXML private JFXTextField nameService;
    @FXML private JFXTextField matchingIDService;
    @FXML private JFXListView<Label> servicesList;
    @FXML private JFXListView<Label> componentsList;
    @FXML private JFXRadioButton providedR,requiredR,singleR,multipleR;
    @FXML private AnchorPane visualisationPane;
   // @FXML private AnchorPane Terminal;
    @FXML private JFXTextField NbCyclesAgent;
    @FXML private JFXListView<OCEAgent> agentsListUI;
    private TextArea UILog;
    private JFXPopup popup;

    private ArrayList<OCService> providedByC;
    private ArrayList<OCService> requiredByC;
    private Graph serviceGraph;

    private MockupFacadeAdapter mockupFacadeAdapter;
    private Infrastructure infrastructure;
//    private Thread simulation;
    private OCE myOCE;
    private Thread opportunisticCompositionEngine;
    private boolean runExecution;
    private PhysicalDeviceBinder physicalDeviceBinder;
    private final int defaultMaxCycleAgent = 400;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.servicesList.setExpanded(true);
        this.componentsList.setExpanded(true);
        this.providedR.setSelected(true);
        this.singleR.setSelected(true);

        this.UILog = new TextArea();
//        this.Terminal.setTopAnchor( this.UILog, 0d);
//        this.Terminal.setBottomAnchor( this.UILog, 0d);
//        this.Terminal.setRightAnchor( this.UILog, 0d);
//        this.Terminal.setLeftAnchor( this.UILog, 0d);
//        this.Terminal.getChildren().add( this.UILog);

        // Initialize the Mockup
        this.mockupFacadeAdapter = new MockupFacadeAdapter();
        //Initialize the MAS Infrastructure
        this.infrastructure = new Infrastructure();
        // Initialize the boolean variable for pause and resume
        this.runExecution = true;

        // Create the list of services
        this.requiredByC = new ArrayList<>();
        this.providedByC = new ArrayList<>();

        // Create the thread for the execution
//        this.simulation = new Thread( new Simulation(this.mockupFacadeAdapter, this.infrastructure));
        this.myOCE = new OCE(this.mockupFacadeAdapter, this.infrastructure);
        this.opportunisticCompositionEngine = new Thread(this.myOCE);

        //Set the binding between the list of agents and the ListView in the UI
        this.agentsListUI.setItems(this.myOCE.gteAllAgents());
        this.agentsListUI.setCellFactory(a -> new AgentUIPersonalisedCell());
        this.agentsListUI.setExpanded(true);

//        //Add a selection listener to the  list view of agents
//        this.agentsListUI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OCEAgent>() {
//            @Override
//            public void changed(ObservableValue<? extends OCEAgent> observable, OCEAgent oldValue, OCEAgent newValue) {
//                //Check whether item is selected and set value of selected item to Label
//                if(agentsListUI.getSelectionModel().getSelectedItem() != null)
//                {
//                    OCEAgent selectedOCEAgent = agentsListUI.getSelectionModel().getSelectedItem();
//                    agentsListUI.getSelectionModel().
//                    if(selectedOCEAgent instanceof ServiceAgent){
//                        Tooltip agentTooltip = new Tooltip();
//                        agentTooltip.setText(selectedOCEAgent.toString());
//                    }
//
//                }
//            }
//        });

        // Initialise popUp
        initPopup();

        // Initialise the graph
        initGraph();
        previewGraph();

        // Init log
        // Create and configure the logger service
        OCELogger logger = new OCELogger();
        logger.uiLogProperty().addListener(new ChangeListener<String>() {
                                               @Override
                                               public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                                   Platform.runLater(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           UILog.setText(newValue);
                                                       }
                                                   });
                                               }
                                           });
        OCELogger.init();
        this.physicalDeviceBinder = PhysicalDeviceBinder.getInstance();
    /*    physicalDeviceBinder.UIBinderServicesProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        String[] idServices = newValue.split("-");
                        System.out.println("idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
                        OCELogger.log(Level.INFO,"idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
                        addEdge(idServices[0], idServices[1]);
            }
        });*/
        this.physicalDeviceBinder.UIBinderSProperty().setValueListener(new NotifySetStringProperty.OnSetValueListener() {
                                                                           @Override
                                                                           public void onValueSet(String value) {
                                                                               System.out.println(value);
                                                                               String[] idServices = value.split("-");
                                                                               System.out.println("idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
                                                                               OCELogger.log(Level.INFO,"idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
                                                                               addEdge(idServices[0], idServices[1]);
                                                                           }
                                                                       });

        this.NbCyclesAgent.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { updateMaxCycleAgent(); } });
    }

    @FXML
    private void addService(ActionEvent event){
        //System.out.println("adding services " + this.nameService.getText().length());
        Label label = new Label();
        OCService serviceToAdd;
        int providedORRequired = -1;
        int singleORMultiple = -1;
        if(this.designationComponent.getText().length()>0) {
            if (this.nameService.getText().length() > 0) {
                if(this.matchingIDService.getText().length() > 0) {
                    String textToAdd = "Name = " + this.nameService.getText() +"-"+this.matchingIDService.getText();
                    if (this.providedR.isSelected()) {
                        // providedImage = new ImageView(new Image(new FileInputStream("/provided.png")));
                        label.setGraphic(new ImageView("/provided.png"));
                        providedORRequired = 0; //ProvideService
                    } else {
                        if (this.requiredR.isSelected()) {
                            // requiredImage = new ImageView(new Image(new FileInputStream("/required.png")));
                            label.setGraphic(new ImageView("/required.png"));
                            providedORRequired = 1; //RequiredService
                        }
                    }

                    if (this.singleR.isSelected()) {
                        textToAdd += " -- = SINGLE";
                        singleORMultiple = 0; // Single Service
                    } else {
                        if (this.multipleR.isSelected()) {
                            textToAdd += " -- = MULTIPLE";
                            singleORMultiple = 1; // Multiple Service
                        }
                    }
                    if (singleORMultiple == 0) {
                        if (providedORRequired == 0) {
                            serviceToAdd = new SingleLinkMockupService(this.nameService.getText(),this.matchingIDService.getText(), this.designationComponent.getText(), Way.PROVIDED);
                            this.providedByC.add(serviceToAdd);
                        } else {
                            if (providedORRequired == 1) {
                                serviceToAdd = new SingleLinkMockupService(this.nameService.getText(),this.matchingIDService.getText(), this.designationComponent.getText(), Way.REQUIRED);
                                this.requiredByC.add(serviceToAdd);
                            }
                        }

                    } else {
                        if (singleORMultiple == 1) {
                            if (providedORRequired == 0) {
                                serviceToAdd = new MultiLinkMockupService(this.nameService.getText(),this.matchingIDService.getText(), this.designationComponent.getText(), Way.PROVIDED);
                                this.providedByC.add(serviceToAdd);
                            } else {
                                if (providedORRequired == 1) {
                                    serviceToAdd = new MultiLinkMockupService(this.nameService.getText(),this.matchingIDService.getText(), this.designationComponent.getText(), Way.REQUIRED);
                                    this.requiredByC.add(serviceToAdd);
                                }
                            }
                        }
                    }
                    label.getStyleClass().add("label-list");
                    label.setText(textToAdd);
                    this.servicesList.getItems().add(label);
                    this.nameService.setText("");
                    this.matchingIDService.setText("");
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the matchingID of the service first !!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the name of the service first !!");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the designation of the component !!");
            alert.show();
        }
    }

    @FXML
    private void cancelAddingServices(ActionEvent event){
        this.servicesList.getItems().clear();
        this.nameService.setText("");
        this.requiredByC.clear();
        this.providedByC.clear();
    }

    @FXML
    private void addComponent(ActionEvent event){
        Label label = new Label();

        if(this.designationComponent.getText().length()>0) {
            if(!this.servicesList.getItems().isEmpty()) {
                String textToAdd = "" + this.designationComponent.getText();
                label.setText(textToAdd);
                label.getStyleClass().add("label-list");
                label.setGraphic(new ImageView("/component.png"));
                this.componentsList.getItems().add(label);
                //Add component to mockup
                addComponentToMockup(this.designationComponent.getText(), this.providedByC, this.requiredByC);
                //Reset UI Elements
                deleteUIElements();
                //Display the services and agents in the graph view
                previewGraph();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please add at least one service !!");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the designation of the component first !!");
            alert.show();
        }
    }

    @FXML
    private void lunchSimulation(ActionEvent event){
       //this.simulation.start();
        this.opportunisticCompositionEngine.start();
        //disable the launch button
        ((JFXButton)event.getSource()).setDisable(true);
    }

    @FXML
    private void pauseResumeExecution(ActionEvent event){
        this.runExecution = !this.runExecution;
        System.out.println("Run Execution = " + this.runExecution);
        if(!this.runExecution){
            this.infrastructure.pauseScheduling();
            ((JFXButton)event.getSource()).setText("Reprise");
        }else{
            this.infrastructure.restartScheduling();
            ((JFXButton)event.getSource()).setText("Pause");
        }

    }


    @FXML
    private void stopExecution(ActionEvent event){
        this.infrastructure.stopScheduling();
    }

    private void deleteUIElements(){
        this.servicesList.getItems().clear();
        this.requiredByC.clear();
        this.providedByC.clear();
        this.designationComponent.setText("");
        this.nameService.setText("");
        this.matchingIDService.setText("");
    }

    /**
     * Add a component to the mockup environment
     * @param componentName : the component name of the component
     */
    private void addComponentToMockup(String componentName, ArrayList<OCService> providedServices, ArrayList<OCService> requiredServices){
        // Creation of the component "C"
        MockupComponent C1 = new MockupComponent(componentName, providedServices, requiredServices);
        // Add the component to the mockup container
        this.mockupFacadeAdapter.addComponent(C1);
        System.out.println(this.mockupFacadeAdapter.getComponents().toString());
        // Update the graph visualisation
        addProvidedServiceToGraphe(providedServices);
        addRequiredServiceToGraphe(requiredServices);
        //Clear the provided and required services list
        providedServices.clear();
        requiredServices.clear();
    }

    @FXML
    public void addComponentsFromXMLFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String xmlFilePath = selectedFile.getAbsolutePath();
            List<MockupComponent> listComponents = XMLFileTools.readXMLComponentFile(xmlFilePath);
            for (MockupComponent component : listComponents){
                addComponentToMockup(component.getName(),component.getProvidedServices(), component.getRequiredServices());
                Label label = new Label();
                String textToAdd = "" + component.getName();
                label.setText(textToAdd);
                label.getStyleClass().add("label-list");
                label.setGraphic(new ImageView("/component.png"));
                this.componentsList.getItems().add(label);
            }


        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No File selected !");
            alert.show();
        }

    }

    private void deleteComponentFromMockup(String nameComp){
        // search for the component
        List<OCComponent> components = this.mockupFacadeAdapter.getComponents().stream()
                                            .filter(p -> ((MockupComponent)p).getName().equalsIgnoreCase(nameComp)).collect(Collectors.toList());

        //get the component
        MockupComponent C1 = (MockupComponent) components.get(0);
        // Delete the component from the mockup container
        System.out.println("Removing : " + C1.getName());
        this.mockupFacadeAdapter.removeComponent(C1);

        // Update the graph visualisation
        deleteServicesFromGraph(C1.getProvidedServices());
        deleteServicesFromGraph(C1.getRequiredServices());

    }

    /**
     * Initialize the graph visualizer
     */
    private void initGraph(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        this.serviceGraph = new MultiGraph("serviceGraph");
        // ...
        this.serviceGraph.removeAttribute("ui.stylesheet");
        this.serviceGraph.addAttribute("ui.stylesheet", "url('UI\\grapheStyleSheet.css')");

    }

    private void previewGraph(){
        //It informs the viewer that it can use rendering algorithms that are more time consuming to favor quality instead of speed
        this.serviceGraph.addAttribute("ui.quality");

        //Adding label to each node
        //this.serviceGraph.getEachNode().forEach(n -> n.addAttribute("ui.label"," Service "+n.getId()+" "));

        Viewer viewer = new Viewer( this.serviceGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        //Create a view within a JFrame
        View view = viewer.addDefaultView(false);
        //Add a custom mouse event manager
        //view.setMouseManager(new Clicks());

        SwingNode swingNode = new SwingNode();
        swingNode.setContent((JComponent)view);
        swingNode.resize(200,400);

        this.visualisationPane.setTopAnchor(swingNode, 0d);
        this.visualisationPane.setBottomAnchor(swingNode, 0d);
        this.visualisationPane.setRightAnchor(swingNode, 0d);
        this.visualisationPane.setLeftAnchor(swingNode, 0d);

        this.visualisationPane.getChildren().add(swingNode);

    }

    private void addProvidedServiceToGraphe(ArrayList<OCService> providedServices) {
        for (OCService service : providedServices) {
            MockupService myMService = (MockupService)service;
            Node node = this.serviceGraph.addNode(myMService.getName()+myMService.getMatchingID()+myMService.getOwner()+myMService.getWay());

            node.addAttribute("ui.label"," "+myMService.getName()+"-"+myMService.getMatchingID()+" Of " + myMService.getOwner());
        }
    }

    private void addRequiredServiceToGraphe(ArrayList<OCService> requiredServices) {
        for (OCService service : requiredServices) {
            MockupService myMService = (MockupService)service;
            Node node = this.serviceGraph.addNode(myMService.getName()+myMService.getMatchingID()+myMService.getOwner()+myMService.getWay());
            node.addAttribute("ui.label",""+myMService.getName()+"-"+myMService.getMatchingID()+" Of " + myMService.getOwner());
            node.addAttribute("ui.class","Required");
        }
    }

    private void addEdge(String idService1, String idService2){
        if(this.serviceGraph.getEdge(""+idService1+idService2)==null && this.serviceGraph.getEdge(""+idService2+idService1)==null ) {
            // We add only one time the edge between two services
            this.serviceGraph.addEdge(""+idService1+"-"+idService2, idService1, idService2);
        }
    }

    private void deleteServicesFromGraph(ArrayList<OCService> listServices) {

        for (OCService service : listServices) {
            MockupService myMService = (MockupService)service;
            String serviceID = ""+ myMService.getName()+myMService.getMatchingID()+myMService.getOwner()+myMService.getWay();
            //Delete all the edges where this service figure as one endpoint
            for (int i=0;i< this.serviceGraph.getEdgeCount();i++){
                if(this.serviceGraph.getEdge(i).getId().contains(serviceID)){
                    this.serviceGraph.removeEdge(i);
                }
            }
            this.serviceGraph.removeNode(serviceID);
        }
    }

//    private void deleteProvidedServiceFromGraphe(ArrayList<OCService> providedServices) {
//
//        for (OCService service : providedServices) {
//            MockupService myMService = (MockupService)service;
//            String serviceID = ""+ myMService.getName()+myMService.getMatchingID()+myMService.getOwner()+myMService.getWay();
//            //Delete all the edges where this service figure as one endpoint
//            for (int i=0;i< this.serviceGraph.getEdgeCount();i++){
//                if(this.serviceGraph.getEdge(i).getId().contains(serviceID)){
//                    this.serviceGraph.removeEdge(i);
//                }
//            }
//            this.serviceGraph.removeNode(serviceID);
//        }
//    }

//    private void deleteRequiredServiceFromGraphe(ArrayList<OCService> requiredServices) {
//        for (OCService service : requiredServices) {
//            MockupService myMService = (MockupService)service;
//            String serviceID = ""+ myMService.getName()+myMService.getMatchingID()+myMService.getOwner()+myMService.getWay();
//            //Delete all the edges where this service figure as one endpoint
//            for (int i=0;i< this.serviceGraph.getEdgeCount();i++){
//                if(this.serviceGraph.getEdge(i).getId().contains(serviceID)){
//                    this.serviceGraph.removeEdge(i);
//                }
//            }
//            this.serviceGraph.removeNode(serviceID);
//        }
//    }

    public synchronized void updateLog(String message){
        String pastContent = this.UILog.getText();
        this.UILog.setText(pastContent+""+ message+"\n");
    }
    @FXML
    private void showPopup(MouseEvent event){
        if(event.getButton().equals(MouseButton.SECONDARY)){// Detect right button click
            this.popup.show(componentsList, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            //JFXListView list = (JFXListView) event.getSource();
        }

    }

    /**
     *  Create a PopUp with delete / update
     */
    private void initPopup(){
        this.popup = new JFXPopup();
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton detailButton = new JFXButton("Detail");
        deleteButton.setPadding(new Insets(10));
        detailButton.setPadding(new Insets(10));
        ImageView deleteImage = new ImageView("/delete.png");
        ImageView detailImage = new ImageView("/detail.png");
        deleteButton.setGraphic(deleteImage);
        detailButton.setGraphic(detailImage);

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               String nameComp = componentsList.getSelectionModel().getSelectedItem().getText();
               //System.out.println(nameComp);
               deleteComponentFromMockup(nameComp);
               componentsList.getItems().remove(componentsList.getSelectionModel().getSelectedIndex());
            }
        });

        detailButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nameComp = componentsList.getSelectionModel().getSelectedItem().getText();
                System.out.println(nameComp);
                Set<OCComponent> mycomponents = mockupFacadeAdapter.getComponents().stream().filter(c -> ((MockupComponent)c).getName().equalsIgnoreCase(nameComp)).collect(Collectors.toSet());
                // Display the component's detail on a window
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                //customise CSS of the new window
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/custumAlert.css");
                dialogPane.getStyleClass().add("myAlert");

                alert.setTitle("Component's Detail");
                alert.setHeaderText("The component * " + nameComp + " * contains ");
                String content="";
                for (OCComponent compo : mycomponents) {
                    content = content + "Provided services : \n";
                    // Concat the content of all services of the component in a single string
                    content = content + ((MockupComponent)compo).getProvidedServices().stream()
                                                                                        .map(s -> "\t - "+((MockupService)s).toString()+"\n")
                                                                                            .collect(Collectors.joining());
                    // content = content + ""+((MockupComponent)compo).getProvidedServices()+"\n";
                    content = content + "Required services : \n";
                    // content = content + ""+((MockupComponent)compo).getRequiredServices();
                    content = content + ((MockupComponent)compo).getRequiredServices().stream()
                                                                                        .map(s -> "\t - "+((MockupService)s).toString()+"\n")
                                                                                            .collect(Collectors.joining());
                    System.out.println("Provided services = "+ ((MockupComponent)compo).getProvidedServices()+"\n");
                    System.out.println(" Required services = "+ ((MockupComponent)compo).getRequiredServices()+"\n");
                }
                alert.setContentText(content);
                alert.show();
                //componentsList.getItems().remove(componentsList.getSelectionModel().getSelectedIndex());
            }
        });

        VBox popUpContent = new VBox( detailButton,deleteButton);
        this.popup.setPopupContent(popUpContent);
    }


    @FXML
    public void deleteConnexion(ActionEvent event){
        String edgeToDelete =  this.serviceGraph.getEdge(0).getId();
        String[] idServices = edgeToDelete.split("-");
        System.out.println("idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
        this.physicalDeviceBinder.deleteConnexion(idServices[0],idServices[1]);

        this.serviceGraph.removeEdge(0);
    }

    /**
     * Get the value typed in the textField corresponding to the number cycles agent per OCE cycle and update in the infrastructure
     */
    private void updateMaxCycleAgent(){
        //Get the value typed in the textField
        String stringValue = this.NbCyclesAgent.getText();
        //convert the value to an integer
        try {
            int intValue = Integer.parseInt(stringValue);
            this.infrastructure.setMaxCycleAgent(intValue);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Parsing error !");
            alert.setContentText("The value must be an integer ! ");
            alert.show();
        }
    }

    @FXML
    public void oneStepExecution(ActionEvent event){
        //Reset to 0 the value of the current agent cycle to restart the OCE cycle
//        try {
//            this.opportunisticCompositionEngine.join();
//            this.myOCE.oneStepExecution();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        this.myOCE.oneStepExecution();
    }
}
