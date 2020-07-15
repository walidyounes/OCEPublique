package UI.PaneControllers;

import OCE.Agents.BinderAgentPack.BinderAgent;
import UI.UPnPMockupController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OCESettingsController implements Initializable {

    @FXML private JFXButton launchOrOneStepActionButton;
    @FXML private JFXButton toggleAutoButton;
    @FXML private JFXTextField NbCyclesAgent;
    @FXML private JFXCheckBox editLearningCheckBox;

    private boolean started = false;

    private boolean auto = false;

    private UPnPMockupController upnpMockupController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.NbCyclesAgent.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { updateMaxCycleAgent(); } });

    }

    @FXML
    public void toggleAuto(ActionEvent actionEvent) {
        auto = !auto;
        launchOrOneStepActionButton.setDisable(auto);
        toggleAutoButton.setText(auto?"Disable Auto":"Enable Auto");
    }

    @FXML
    public void launchOrOneStepExecution(ActionEvent event) {
        if(!started) {
            upnpMockupController.lunchSimulation(event);
            launchOrOneStepActionButton.setText("Next");
            started = true;
        } else {
            upnpMockupController.getMyOCE().oneStepExecution();
        }
    }

    @FXML
    public void resetOCECycle(ActionEvent event){
        Alert myAlert = new Alert(Alert.AlertType.CONFIRMATION);
        myAlert.setTitle("OCE connection delete");
        myAlert.setContentText("Are you sure of deleting all the connections ?");
        Optional<ButtonType> result = myAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Reset all agent's attributes to factory mode settings (for service agent we don't delete the knowledge base)
            upnpMockupController.getMyOCE().gteAllAgents().stream().forEach(agent-> agent.resetToFactoryDefaultSettings());
            //Delete the binder agents
            upnpMockupController.getMyOCE().gteAllAgents().stream().filter(agent -> agent instanceof BinderAgent).forEach(agentB -> ((BinderAgent) agentB).suicide());
        }
    }

    private void updateMaxCycleAgent(){
        //Get the value typed in the textField
        String stringValue = this.NbCyclesAgent.getText();
        //convert the value to an integer
        try {
            int intValue = Integer.parseInt(stringValue);
            upnpMockupController.getInfrastructure().setMaxCycleAgent(intValue);
            System.out.println("New max cycle agent = " + intValue);
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Parsing error !");
            alert.setContentText("The value must be an integer ! ");
            alert.show();
        }
    }

    public boolean isEditLearningSelected() {
        return editLearningCheckBox.isSelected();
    }

    public void setUpnpMockupController(UPnPMockupController upnpMockupController) {
        this.upnpMockupController = upnpMockupController;
    }

    public boolean isAuto() {
        return auto;
    }
}
