/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import OCE.DeviceBinder.PhysicalDeviceBinder;
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
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class UIMockupController implements Initializable {
    @FXML private JFXTextField designationComponent;
    @FXML private JFXTextField nameService;
    @FXML private JFXListView<Label> servicesList;
    @FXML private JFXListView<Label> componentsList;
    @FXML private JFXRadioButton providedR,requiredR,singleR,multipleR;
    @FXML private AnchorPane visualisationPane;
    @FXML private AnchorPane Terminal;
    private MockupFacadeAdapter mockupFacadeAdapter;
    private ArrayList<OCService> providedByC;
    private ArrayList<OCService> requiredByC;
    private Graph ServiceGraphe;
    private TextArea UILog;
    private Thread simulation;
    private JFXPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.servicesList.setExpanded(true);
        this.componentsList.setExpanded(true);
        this.providedR.setSelected(true);
        this.singleR.setSelected(true);

        this.UILog = new TextArea();
        this.Terminal.setTopAnchor( this.UILog, 0d);
        this.Terminal.setBottomAnchor( this.UILog, 0d);
        this.Terminal.setRightAnchor( this.UILog, 0d);
        this.Terminal.setLeftAnchor( this.UILog, 0d);
        this.Terminal.getChildren().add( this.UILog);
        // Initialise the Mockup
        this.mockupFacadeAdapter = new MockupFacadeAdapter();
        this.requiredByC = new ArrayList<>();
        this.providedByC = new ArrayList<>();

        this.simulation = new Thread( new Simulation(this.mockupFacadeAdapter));
        // Initialise popUp
        initPopup();

        //Initilise the graphe
        initGraphe();
        previewGraph();

        //Init log
        //Create and configure the logger service
        MyLogger logger = new MyLogger();
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
        MyLogger.init();
        PhysicalDeviceBinder physicalDeviceBinder = new PhysicalDeviceBinder();
        physicalDeviceBinder.UIBinderServicesProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String[] idServices = newValue.split("-");
                        System.out.println("idSer1 = "+ idServices[0]+ " idSer2 = "+idServices[1]);
                        addEdge(idServices[0], idServices[1]);
                    }
                });
            }
        });
    }

    @FXML
    private void addService(ActionEvent event){
        System.out.println("adding services " + this.nameService.getText().length());
        Label label = new Label();
        OCService serviceToAdd;
        int providedORrequired = -1;
        int singleORmultiple = -1;
        if(this.designationComponent.getText().length()>0) {
            if (this.nameService.getText().length() > 0) {
                String textToAdd = "Name = " + this.nameService.getText();
                    if (this.providedR.isSelected()) {
                        // providedImage = new ImageView(new Image(new FileInputStream("/provided.png")));
                        label.setGraphic(new ImageView("/provided.png"));
                        providedORrequired = 0; //ProvideService
                    } else {
                        if (this.requiredR.isSelected()) {
                            // requiredImage = new ImageView(new Image(new FileInputStream("/required.png")));
                            label.setGraphic(new ImageView("/required.png"));
                            providedORrequired = 1; //RequiredService
                        }
                    }

                if (this.singleR.isSelected()) {
                    textToAdd += " -- Type = SINGLE";
                    singleORmultiple = 0; // Single Service
                } else {
                    if (this.multipleR.isSelected()) {
                        textToAdd += " -- Type = MULTIPLE";
                        singleORmultiple = 1; // Multiple Service
                    }
                }
                if (singleORmultiple == 0) {
                    if (providedORrequired == 0) {
                        serviceToAdd = new SingleLinkMockupService(this.nameService.getText(), this.designationComponent.getText(), Way.PROVIDED);
                        this.providedByC.add(serviceToAdd);
                    } else {
                        if (providedORrequired == 1) {
                            serviceToAdd = new SingleLinkMockupService(this.nameService.getText(), this.designationComponent.getText(), Way.REQUIRED);
                            this.requiredByC.add(serviceToAdd);
                        }
                    }

                } else {
                    if (singleORmultiple == 1) {
                        if (providedORrequired == 0) {
                            serviceToAdd = new MultiLinkMockupService(this.nameService.getText(), this.designationComponent.getText(), Way.PROVIDED);
                            this.providedByC.add(serviceToAdd);
                        } else {
                            if (providedORrequired == 1) {
                                serviceToAdd = new MultiLinkMockupService(this.nameService.getText(), this.designationComponent.getText(), Way.REQUIRED);
                                this.requiredByC.add(serviceToAdd);
                            }
                        }
                    }
                }
                label.getStyleClass().add("label-list");
                label.setText(textToAdd);
                this.servicesList.getItems().add(label);
                this.nameService.setText("");
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
                String textToAdd = "Component : " + this.designationComponent.getText();
                label.setText(textToAdd);
                this.componentsList.getItems().add(label);
                //Add component to mockup
                addComponentToMockup();
                //Reset UI Elements
                deleteUIElements();

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

       this.simulation.start();
    }

    @FXML
    private void stopSimulation(ActionEvent event){
        this.simulation.interrupt();
    }

    private void deleteUIElements(){
        this.servicesList.getItems().clear();
        this.requiredByC.clear();
        this.providedByC.clear();
        this.designationComponent.setText("");
        this.nameService.setText("");
    }

    /**
     *
     */
    private void addComponentToMockup(){
        // Creation of the composant "C"
        MockupComponent C1 = new MockupComponent(this.designationComponent.getText(), this.providedByC, this.requiredByC);
        // Add the compoenent to the mockup container
        this.mockupFacadeAdapter.addComponent(C1);
        System.out.println(this.mockupFacadeAdapter.getComponents().toString());
        // Update the graphe visulisation
        addProvidedServiceToGraphe(this.providedByC);
        addRequiredServiceToGraphe(this.requiredByC);
        // clear the provided and required services list
        this.providedByC.clear();
        this.requiredByC.clear();
    }

    private void deleteComponentFromMockup(String nameComp){
        // search for the component
        List<OCComponent> components = this.mockupFacadeAdapter.getComponents().stream()
                                            .filter(p -> ((MockupComponent)p).getName().equalsIgnoreCase(nameComp)).collect(Collectors.toList());

        //get the component
        MockupComponent C1 = (MockupComponent) components.get(0);
        // Add the compoenent to the mockup container
        this.mockupFacadeAdapter.addComponent(C1);
        System.out.println("Removing : " + this.mockupFacadeAdapter.getComponents().toString());
        // Update the graphe visulisation
        deleteProvidedServiceFromGraphe(C1.getProvidedServices());
        deleteRequiredServiceFromGraphe(C1.getRequiredServices());
        // remove component from the Mockup
        this.mockupFacadeAdapter.removeComponent(C1);
    }

    private void initGraphe(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        this.ServiceGraphe = new MultiGraph("ServiceGraphe");
        // ...
        this.ServiceGraphe.removeAttribute("ui.stylesheet");
        this.ServiceGraphe.addAttribute("ui.stylesheet", "url('UI\\grapheStyleSheet.css')");

    }

    private void previewGraph(){
        // It informs the viewer that it can use rendering algorithms that are more time consuming to favor quality instead of speed
        this.ServiceGraphe.addAttribute("ui.quality");

        // Adding label to each node done by walid ^^
        //this.ServiceGraphe.getEachNode().forEach(n -> n.addAttribute("ui.label"," Service "+n.getId()+" "));

        Viewer viewer = new Viewer( this.ServiceGraphe, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        // create a view *without* a JFrame
        View view = viewer.addDefaultView(false);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent((JComponent)view);
        //swingNode.resize(400,400);

        this.visualisationPane.setTopAnchor(swingNode, 0d);
        this.visualisationPane.setBottomAnchor(swingNode, 0d);
        this.visualisationPane.setRightAnchor(swingNode, 0d);
        this.visualisationPane.setLeftAnchor(swingNode, 0d);

        this.visualisationPane.getChildren().add(swingNode);

    }

    private void addProvidedServiceToGraphe(ArrayList<OCService> providedServices) {

        for (OCService service : providedServices) {
            MockupService myMService = (MockupService)service;
            Node node = this.ServiceGraphe.addNode(myMService.getName()+myMService.getOwner()+myMService.getWay());

            node.addAttribute("ui.label"," "+myMService.getName()+" Of " + myMService.getOwner());

        }

    }

    private void addRequiredServiceToGraphe(ArrayList<OCService> requiredServices) {
        for (OCService service : requiredServices) {
            MockupService myMService = (MockupService)service;
            Node node = this.ServiceGraphe.addNode(myMService.getName()+myMService.getOwner()+myMService.getWay());
            node.addAttribute("ui.label",""+myMService.getName()+" Of " + myMService.getOwner());
            node.addAttribute("ui.class","Required");
        }
    }

    private void addEdge(String idService1, String idService2){

        this.ServiceGraphe.addEdge(""+idService1+idService2, idService1, idService2);
    }

    private void deleteProvidedServiceFromGraphe(ArrayList<OCService> providedServices) {

        for (OCService service : providedServices) {
            MockupService myMService = (MockupService)service;
            this.ServiceGraphe.removeNode(myMService.getName()+myMService.getOwner()+myMService.getWay());
        }

    }

    private void deleteRequiredServiceFromGraphe(ArrayList<OCService> requiredServices) {
        for (OCService service : requiredServices) {
            MockupService myMService = (MockupService)service;
            this.ServiceGraphe.removeNode(myMService.getName()+myMService.getOwner()+myMService.getWay());
        }
    }

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
               String nameComp = componentsList.getSelectionModel().getSelectedItem().getText().split(" : ")[1];
               System.out.println(nameComp);
               deleteComponentFromMockup(nameComp);
               componentsList.getItems().remove(componentsList.getSelectionModel().getSelectedIndex());
            }
        });

        detailButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nameComp = componentsList.getSelectionModel().getSelectedItem().getText().split(" : ")[1];
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
}
