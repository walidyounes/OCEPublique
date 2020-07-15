/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.MockupFacadeAdapter.MockupFacadeAdapter;
import AmbientEnvironment.OCPlateforme.OCService;
import UI.PaneControllers.AgentParametersController;
import UI.PaneControllers.AgentsPaneController;
import UI.PaneControllers.ComponentListController;
import UI.PaneControllers.OCESettingsController;
import UPnPEnvironment.SystemNotification;
import Logger.OCELogger;
import MASInfrastructure.Infrastructure;
import MOICE.MOICE;
import MOICE.feedbackManager.FeedbackManager;
import OCE.DeviceBinder.PhysicalDeviceBinder;
import OCE.Medium.Medium;
import OCE.OCE;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class UPnPMockupController implements Initializable {

    @FXML private ComponentListController compListController;

    @FXML private OCESettingsController oceSettingsController;

    @FXML private AgentsPaneController agentsPaneController;

    @FXML private AgentParametersController agentsParametersController;

    private SystemNotification systemNotification = null;

    private MockupFacadeAdapter mockupFacadeAdapter;

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
    }

    /**
     * Initialise the system with the different attribute
     */
    private void initSystem(){

        // Initialize the Mockup
        this.mockupFacadeAdapter = new MockupFacadeAdapter();
        //Initialize the MAS Infrastructure
        this.infrastructure = new Infrastructure();

        // Create the thread for the execution
        this.myOCE = new OCE(this.mockupFacadeAdapter, this.infrastructure);
        this.opportunisticCompositionEngine = new Thread(this.myOCE);

        ((FeedbackManager)MOICE.getInstance().getMyFeedbackManager()).setMockupFacadeAdapter(mockupFacadeAdapter);
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
            }
        });

        try {
            systemNotification = new SystemNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }

        oceSettingsController.setUpnpMockupController(this);
        agentsPaneController.setUpnpMockupController(this);
        agentsParametersController.setAgentsPaneController(agentsPaneController);
    }

    public void lunchSimulation(ActionEvent event){
        if(oceSettingsController.isEditLearningSelected()){
            this.myOCE.setEditLearning(true);
        }else{
            this.myOCE.setEditLearning(false);
        }
        this.opportunisticCompositionEngine.start();
    }

    @FXML
    private void stopExecution(ActionEvent event){
        this.infrastructure.stopScheduling();
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
                    compListController.reset();
                    //Reactivate the launch button
                    //launchButton.setDisable(false); TODO : Maj reset system en prenant en compte le mode auto
                    //Clear list of details of the agent
                    agentsPaneController.reset();
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

    public void removeComponentFromEnv(MockupComponent mockupComponent) {
        compListController.removeMockupComponent(mockupComponent);

        this.mockupFacadeAdapter.removeComponent(mockupComponent);

        if(oceSettingsController.isAuto()){
            oceSettingsController.launchOrOneStepExecution(null);

            if( systemNotification != null ) {
                systemNotification.displayMessage(
                        mockupComponent.getName() + " disappeared",
                        "A new assembly proposition is available."
                );
            }
        }
    }

    public void addComponentToEnv(MockupComponent mockupComponent) {
        compListController.addMockupComponent(mockupComponent);

        this.mockupFacadeAdapter.addComponent(mockupComponent);

        if(oceSettingsController.isAuto()){
            oceSettingsController.launchOrOneStepExecution(null);
            if( systemNotification != null ) {
                systemNotification.displayMessage(
                        mockupComponent.getName() + " appeared.",
                        "A new assembly proposition is available."
                );
            }
        }
    }

    public OCE getMyOCE() {
        return myOCE;
    }

    public Infrastructure getInfrastructure() {
        return infrastructure;
    }

    public static TextFlow textFlowDisplayingComponent(MockupComponent component){

        TextFlow textFlow = new TextFlow();

        Text text = new Text();

        text.setText(component.getName());
        text.setFont(Font.font("System", FontWeight.BOLD,20));

        textFlow.getChildren().add(text);

        for(OCService providedService : component.getProvidedServices()){
            Text providedText = new Text("\n(Provided) ");
            providedText.setFill(Color.ROYALBLUE);
            providedText.setFont(Font.font("System",12));

            Text serviceText = new Text(((MockupService)providedService).getName());
            serviceText.setFont(Font.font("System", FontWeight.BOLD, 15));

            Text matchingIdText = new Text(" - " + ((MockupService)providedService).getMatchingID());
            matchingIdText.setFont(Font.font("System", 10));

            textFlow.getChildren().addAll(providedText, serviceText, matchingIdText);
        }

        for(OCService requiredService : component.getRequiredServices()){
            Text requiredText = new Text("\n(Required) ");
            requiredText.setFill(Color.TOMATO);
            requiredText.setFont(Font.font("System",12));

            Text serviceText = new Text(((MockupService)requiredService).getName());
            serviceText.setFont(Font.font("System", FontWeight.BOLD, 15));


            Text matchingIdText = new Text(" - " + ((MockupService)requiredService).getMatchingID());
            matchingIdText.setFont(Font.font("System", 10));

            textFlow.getChildren().addAll(requiredText, serviceText, matchingIdText);
        }

        return textFlow;
    }

}
