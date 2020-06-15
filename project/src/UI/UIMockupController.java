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
import MOICE.MOICE;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.IDAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.ReferenceSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;
import OCE.Agents.ServiceAgentPack.Learning.SituationUtility;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.DeviceBinder.PhysicalDeviceBinder;
import OCE.Medium.Medium;
import OCE.OCE;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class UIMockupController implements Initializable {
    @FXML private JFXTextField designationComponent;
    @FXML private JFXTextField nameService;
    @FXML private JFXTextField matchingIDService;
    @FXML private JFXListView<Label> servicesList;
    @FXML private JFXListView<Label> componentsList;
    @FXML private JFXRadioButton providedR,requiredR,singleR,multipleR;
    @FXML private JFXButton launchButton;
    @FXML private JFXButton nextOCECycleButton;
    @FXML private AnchorPane visualisationPane;
    @FXML private VBox agentDetailsPane;
    @FXML private VBox graphVisualisationPane;

   // @FXML private AnchorPane Terminal;
    @FXML private JFXTextField NbCyclesAgent;
    //@FXML private JFXTextField coefficientNovelty;
    @FXML private JFXCheckBox editLearningCheckBox;

    @FXML private JFXSlider noveltyCoefficientSlider;
    @FXML private JFXSlider learningRateSlider;
    @FXML private JFXSlider reinforcementSlider;
    @FXML private JFXSlider similarityThresholdSlider;
    @FXML private JFXSlider epsilonSlider;

    @FXML private JFXListView<OCEAgent> agentsListUI;
//    private TextArea UILog;
    private JFXPopup popup;

    private ArrayList<OCService> providedByC;
    private ArrayList<OCService> requiredByC;
    private Graph serviceGraph;

    private MockupFacadeAdapter mockupFacadeAdapter;
    private List<OCComponent> disappearedComponents;

    private Infrastructure infrastructure;
//    private Thread simulation;
    private OCE myOCE;
    private Thread opportunisticCompositionEngine;
    private PhysicalDeviceBinder physicalDeviceBinder;
    private final int defaultMaxCycleAgent = 400;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialise the System
        this.initSystem();
        //Initialise the UI of the system
        this.initSystemUI();
    }

    /**
     * Initialise the system with the different attribute
     */
    private void initSystem(){

        // Initialize the Mockup
        this.mockupFacadeAdapter = new MockupFacadeAdapter();
        //Initialize the list of disappearing components
        this.disappearedComponents = new ArrayList<>();
        //Initialize the MAS Infrastructure
        this.infrastructure = new Infrastructure();

        // Create the list of services
        this.requiredByC = new ArrayList<>();
        this.providedByC = new ArrayList<>();

        // Create the thread for the execution
        this.myOCE = new OCE(this.mockupFacadeAdapter, this.infrastructure);
        this.opportunisticCompositionEngine = new Thread(this.myOCE);

        // Init log
        // Create and configure the logger service
        OCELogger logger = new OCELogger();
        OCELogger.init();
        this.physicalDeviceBinder = PhysicalDeviceBinder.getInstance();
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
    }

    /**
     * Initialise the UI of the system
     */
    private void initSystemUI(){
        this.servicesList.setExpanded(true);
        this.componentsList.setExpanded(true);
        this.agentsListUI.setExpanded(true);
        //Put by default the radios button selected
        this.providedR.setSelected(true);
        this.singleR.setSelected(true);
        //Associate the text fields to the function called when we press "Enter"
        this.NbCyclesAgent.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { updateMaxCycleAgent(); } });

        //Set the binding between the list of agents and the ListView in the UI
        this.agentsListUI.setItems(this.myOCE.gteAllAgents());
        //Add a custom Cell for the agent (with a tooltip containing its information)
        this.agentsListUI.setCellFactory(a -> new AgentUIPersonalisedCell());
        this.agentsListUI.setExpanded(true);

        //Add a selection listener to the  list view of agents
        this.agentsListUI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OCEAgent>() {
            @Override
            public void changed(ObservableValue<? extends OCEAgent> observable, OCEAgent oldValue, OCEAgent newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(agentsListUI.getSelectionModel().getSelectedItem() != null)
                {
                    TitledPane titledPaneAgentDetails;
                    OCEAgent selectedOCEAgent = agentsListUI.getSelectionModel().getSelectedItem();
                    if(selectedOCEAgent instanceof ServiceAgent){
                        titledPaneAgentDetails = createTitledPaneContentServiceAgent((ServiceAgent) selectedOCEAgent);
                    }else {
                        if(selectedOCEAgent instanceof BinderAgent){
                            titledPaneAgentDetails = createTitledPaneContentBinderAgent((BinderAgent) selectedOCEAgent);
                        }else{
                            titledPaneAgentDetails = new TitledPane();
                        }
                    }
                    //Add the label to the agent's details visualisation Pane
                    agentDetailsPane.getChildren().clear();
                    agentDetailsPane.getChildren().add(titledPaneAgentDetails);
                }
            }
        });

        //Add the function to be called when the value of each slider for the learning parameters changes in value
        this.noveltyCoefficientSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SituationUtility.CSN = newValue.doubleValue();
            }
        });
        this.learningRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsListUI.getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setLearningRate(newValue.doubleValue()));
            }
        });
        this.reinforcementSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsListUI.getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setBeta(newValue.doubleValue()));
            }
        });
        this.similarityThresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsListUI.getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setSimilarityThreshold(newValue.doubleValue()));
            }
        });
        this.epsilonSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsListUI.getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setEpsilon(newValue.doubleValue()));
            }
        });

        // Initialise popUp that we show in the components list
        initPopup();

        // Initialise and preview the graph the graph
        initGraph();
        //Set hidden the visualisation pane fo the graph
        this.graphVisualisationPane.setVisible(false);
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
        //Add the component to the list of disappearing components
        this.disappearedComponents.add(C1);

        // Update the graph visualisation
        deleteServicesFromGraph(C1.getProvidedServices());
        deleteServicesFromGraph(C1.getRequiredServices());

    }

    /**
     * Initialize the graph visualizer and preview the graph
     */
    private void initGraph(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        this.serviceGraph = new MultiGraph("serviceGraph");
        // ...
        this.serviceGraph.removeAttribute("ui.stylesheet");
        this.serviceGraph.addAttribute("ui.stylesheet", "url('UI\\grapheStyleSheet.css')");
        //Preview the graph
        previewGraph();
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
        try {
            if(this.serviceGraph.getEdge(""+idService1+idService2)==null && this.serviceGraph.getEdge(""+idService2+idService1)==null ) {
                // We add only one time the edge between two services
                if(this.serviceGraph.getNode(idService1)!=null && this.serviceGraph.getNode(idService2)!=null){
                    this.serviceGraph.addEdge(""+idService1+"-"+idService2, idService1, idService2);
                }
            }
        }catch (Exception e){
            //ignore exceptions in this Graph stream function
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

//    public synchronized void updateLog(String message){
//        String pastContent = this.UILog.getText();
//        this.UILog.setText(pastContent+""+ message+"\n");
//    }

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
                                                                                        .map(s -> "\t - "+((MockupService)s).getName() + "-" + ((MockupService)s).getMatchingID() +"\n")
                                                                                            .collect(Collectors.joining());
                    // content = content + ""+((MockupComponent)compo).getProvidedServices()+"\n";
                    content = content + "Required services : \n";
                    // content = content + ""+((MockupComponent)compo).getRequiredServices();
                    content = content + ((MockupComponent)compo).getRequiredServices().stream()
                                                                                        .map(s -> "\t - "+((MockupService)s).getName() + "-" + ((MockupService)s).getMatchingID()+"\n")
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
    private void lunchSimulation(ActionEvent event){
        //this.simulation.start();
        //if(!this.opportunisticCompositionEngine.isInterrupted()){
        if(this.editLearningCheckBox.isSelected()){
            this.myOCE.setEditLearning(true);
        }else{
            this.myOCE.setEditLearning(false);
        }
        this.opportunisticCompositionEngine.start();
        //disable the launch button
        ((JFXButton)event.getSource()).setDisable(true);
        //}
    }

//    @FXML
//    private void pauseResumeExecution(ActionEvent event){
//        this.runExecution = !this.runExecution;
//        System.out.println("Run Execution = " + this.runExecution);
//        if(!this.runExecution){
//            this.infrastructure.pauseScheduling();
//            ((JFXButton)event.getSource()).setText("Reprise");
//        }else{
//            this.infrastructure.restartScheduling();
//            ((JFXButton)event.getSource()).setText("Pause");
//        }
//
//    }


    @FXML
    private void stopExecution(ActionEvent event){
        this.infrastructure.stopScheduling();
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
            System.out.println("New max cycle agent = " + intValue);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Parsing error !");
            alert.setContentText("The value must be an integer ! ");
            alert.show();
        }
    }

    @FXML
    public void oneStepExecution(ActionEvent event){
        this.myOCE.oneStepExecution();
    }


    /**
     * Reset the OCE cycle (reset agent's settings to factory default)
     * @param event
     */
    @FXML
    public void resetOCECycle(ActionEvent event){
        Alert myAlert = new Alert(Alert.AlertType.CONFIRMATION);
        myAlert.setTitle("OCE connection delete");
        myAlert.setContentText("Are you sure of deleting all the connections ?");
        Optional<ButtonType> result = myAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Reset all agent's attributes to factory mode settings (for service agent we don't delete the knowledge base)
            this.myOCE.gteAllAgents().stream().forEach(agent-> agent.resetToFactoryDefaultSettings());
            //Delete the binder agents
            this.myOCE.gteAllAgents().stream().filter(agent -> agent instanceof BinderAgent).forEach(agentB -> ((BinderAgent) agentB).suicide());
            this.serviceGraph.getEdgeSet().stream().forEach(edge -> this.serviceGraph.removeEdge(edge));
        }
    }

    @FXML
    public void resetSystem(ActionEvent event){
        Alert myAlert = new Alert(Alert.AlertType.CONFIRMATION);
        myAlert.setTitle("System reset");
        myAlert.setContentText("Are you sure of willing to reset the system?");
        Optional<ButtonType> result = myAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Delete components and services
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    componentsList.getItems().clear();
                    //Reactivate the launch button
                    launchButton.setDisable(false);
                    //Clear list of details of the agent
                    agentDetailsPane.getChildren().clear();
                }
            });
            //Delete components and services from the Mockup
            mockupFacadeAdapter.getComponents().clear();
            //Delete agents from OCE
            Medium medium = this.myOCE.getMedium();
            this.myOCE.gteAllAgents().forEach(agent -> medium.unregisterOCEAgent(agent));
            //Stop the scheduler
            this.infrastructure.stopScheduling();
            //Reinitialise the counter og agent's cycle in the scheduler
            this.infrastructure.resetCurrentCycleAgent();

            //Reinitialise the system and the system UI
            this.initSystem();
            this.initSystemUI();

            //reset settings in MOICE, which automatically clear the content of ICE's File for visualisation
            MOICE.getInstance().resetToDefaultSettings();

//            //Delete the content of the directory where we save the files send by ICE
//            File ICEFileFolder = new File("ICEConfiguration");
//            File[] listFiles = ICEFileFolder.listFiles();
//            for (File file : listFiles){
//                if(!file.delete()){
//                    System.out.println("Failed to delete the file : "+file.getName());
//                }else{
//                    System.out.println("Success to delete the file : "+file.getName());
//                }
//            }
        }
    }

    @FXML
    public void quitter(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setContentText("Êtes vous sur de vouloir quitter le programme ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
           System.exit(0);
        }
    }

    /**
     * Show and hide the panel visualisation of services as a graph
     * @param event : the event detected (button on click)
     */
    @FXML
    public void showHideServiceGraphVisualisation(ActionEvent event){
        if(this.graphVisualisationPane.isVisible()){
            this.graphVisualisationPane.setVisible(false);
        }else{
            this.graphVisualisationPane.setVisible(true);
        }
    }

    @FXML
    public void launchICEVisualisationProgram(ActionEvent event){
        try {
            // Command to create an external process
            String command = "C:\\Users\\furet\\Downloads\\gemoc\\GemocStudio";
            // Running the above command
            Runtime run  = Runtime.getRuntime();
            Process proc = run.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Get the value typed in the textField corresponding to the sensibility coefficient of novelty of the user
//     * */
//    private void updateCoefficientNovelty(){
//        //Get the value typed in the textField
//        String stringValue = this.coefficientNovelty.getText();
//        //convert the value to an integer
//        try {
//            double doubleValue = Double.parseDouble(stringValue);
//            if(doubleValue <= 1 && doubleValue >= 0){
//                SituationUtility.CSN = doubleValue;
//                System.out.println("CSN Value = "+ SituationUtility.CSN);
//            }else{
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Parsing error !");
//                alert.setContentText("The value must be a double between [0, 1] ! ");
//                alert.show();
//            }
//
//        }catch (NumberFormatException e){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Parsing error !");
//            alert.setContentText("The value must be a double ! ");
//            alert.show();
//        }
//    }

    /**
     * Get the service agent's information and create a label to display it on the UI
     * @param serviceAgent  : the service agent in question
     * @return  the information of the agent in a UI TitledPane
     */
    private TitledPane createTitledPaneContentServiceAgent(ServiceAgent serviceAgent){
        TitledPane agentTitledPane = new TitledPane();
        agentTitledPane.setText(" SERVICE AGENT");
        agentTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox contentSA = new VBox();

        Label idAgent = new Label("IDAgent = " + serviceAgent.getMyID());
        Label handledService = new Label("\nHandled Service  = " + serviceAgent.getHandledService().toString());
        Label agentState = new Label("\nAgent's state  = " + serviceAgent.getMyConnexionState().toString());
        Label agentConnectedTo = new Label("\nAgent is connected to  = " + serviceAgent.getConnectedTo().toString());
        Label agentBinderAgent = new Label("\nAgent's binder Agent  = " + serviceAgent.getMyBinderAgent().toString());

        TitledPane agentKnowledgeBaseTitledPane = new TitledPane();
        agentKnowledgeBaseTitledPane.setText(" Knowledge Base ");
        agentKnowledgeBaseTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
        VBox contentKB = new VBox();

        //iterate over the situationEntries and printThem
        Iterator<Situation<ReferenceSituationEntry>> iterator = serviceAgent.getMyKnowledgeBase().iterator();
        int RSCpt = 1;
        while (iterator.hasNext()){
            Situation<ReferenceSituationEntry> currentSit = iterator.next();
            VBox vBoxKB = new VBox();
            ScrollPane scrollPaneKB = new ScrollPane(vBoxKB);
            Label rSDebut = new Label( " RS"+RSCpt+" = {\tAgent  \t\t\tScore");
            vBoxKB.getChildren().add(rSDebut);
            TreeMap<IDAgent, ReferenceSituationEntry> tree = new TreeMap<>(currentSit.getAgentSituationEntries());
            tree.forEach((k,v) -> vBoxKB.getChildren().add(new Label("\t"+k+" \t"+v.getScore())));
            Label rSFin = new Label( " }");
            vBoxKB.getChildren().add(rSFin);
            contentKB.getChildren().add(scrollPaneKB);
            RSCpt+=1;
        }
        contentKB.getChildren().forEach(node -> node.setStyle("-fx-font-weight: bold; -fx-font-size: 12;"));
        agentKnowledgeBaseTitledPane.setContent(contentKB);

        contentSA.getChildren().add(idAgent);
        contentSA.getChildren().add(handledService);
        contentSA.getChildren().add(agentState);
        contentSA.getChildren().add(agentConnectedTo);
        contentSA.getChildren().add(agentBinderAgent);
        contentSA.getChildren().add(agentKnowledgeBaseTitledPane);

        contentSA.getChildren().forEach( node -> node.setStyle("-fx-font-size: 12;"));
        agentTitledPane.setContent(contentSA);

        return agentTitledPane;
    }


    /**
     * Get the service agent's information and create a label to display it on the UI
     * @param binderAgent  : the service agent in question
     * @return  the information of the agent in a UI TitledPane
     */
    private TitledPane createTitledPaneContentBinderAgent(BinderAgent binderAgent){
        TitledPane agentTitledPane = new TitledPane();
        agentTitledPane.setText(" BINDER AGENT ");
        agentTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox contentSA = new VBox();

        Label idAgent = new Label("IDAgent = " + binderAgent.getMyID());
        contentSA.getChildren().add(idAgent);
        if(binderAgent.getFirstService().isPresent() && binderAgent.getSecondService().isPresent()){
            Label firstService = new Label("First service = " + binderAgent.getFirstService().get());
            Label secondService = new Label("Second service = " + binderAgent.getSecondService().get());
            contentSA.getChildren().add(firstService);
            contentSA.getChildren().add(secondService);
        }
        contentSA.getChildren().forEach( node -> node.setStyle("-fx-font-size: 12;"));
        agentTitledPane.setContent(contentSA);

        return agentTitledPane;
    }

    @FXML
    private void showChoseDisappearingComponent(ActionEvent event){
//        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "UIAddFromDisappearing.fxml"
                    )
            );


//            // cGet the parentStage
//            MenuItem source = (MenuItem) event.getSource();
//            Stage parentStage  = (Stage) (source.getParentPopup().getScene().getWindow());

            Stage secondaryStage = new Stage(StageStyle.UTILITY);
            secondaryStage.setTitle("Disappearing Components");
            secondaryStage.setScene(new Scene(loader.load(), 521, 650));
//            secondaryStage.initOwner(parentStage); //Add my main UI as parent for the secondary UI
            secondaryStage.initModality(Modality.APPLICATION_MODAL); // Block the interaction with the main UI until this secondary UI is closed
            secondaryStage.setAlwaysOnTop(true);
            secondaryStage.setMaximized(false);
            UIAddFromDisappearingController secondaryStageController = loader.getController();
            secondaryStageController.initData(this.disappearedComponents);
//            root = FXMLLoader.load(getClass().getResource("UIAddFromDisappearing.fxml"));
//
            secondaryStage.showAndWait();
            //if the user chose a component to make it reappear
            if(secondaryStageController.getChosenComponent().isPresent()){
                MockupComponent component = secondaryStageController.getChosenComponent().get();
                this.mockupFacadeAdapter.addComponent(component);
                //remove it from the disappearing components list
                this.disappearedComponents.remove(component);
                Label label = new Label();
                String textToAdd = "" + component.getName();
                label.setText(textToAdd);
                label.getStyleClass().add("label-list");
                label.setGraphic(new ImageView("/component.png"));
                this.componentsList.getItems().add(label);
            }
            System.out.println(secondaryStageController.getChosenComponent());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @FXML
//    private void showAgentEditKnowledgeStage(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource(
//                            "UIInitializeAgentLearning.fxml"
//                    )
//            );
//
//            Stage secondaryStage = new Stage(StageStyle.UTILITY);
//            secondaryStage.setTitle("Editing Agents' Knowledge");
//            secondaryStage.setScene(new Scene(loader.load(), 1130, 860));
//
////            secondaryStage.initOwner(parentStage); //Add my main UI as parent for the secondary UI
//            secondaryStage.initModality(Modality.APPLICATION_MODAL); // Block the interaction with the main UI until this secondary UI is closed
//            secondaryStage.setAlwaysOnTop(true);
//            secondaryStage.setMaximized(false);
//            UIInitializeAgentLearningController secondaryStageController = loader.getController();
//            secondaryStageController.initData(this.myOCE.gteAllAgents());
//            secondaryStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
