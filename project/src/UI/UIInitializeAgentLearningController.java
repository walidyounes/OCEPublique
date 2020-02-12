/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.ReferenceSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Unifieur.Matching;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UIInitializeAgentLearningController implements Initializable {

    @FXML
    private JFXListView<OCEAgent> agentsViewList;
    @FXML
    private JFXListView<ReferenceSituationEntry> currentlyConstructedRSViewList;
    @FXML
    private JFXListView<Situation<ReferenceSituationEntry>> RSViewList;
    @FXML
    private JFXComboBox<OCEAgent> agentComboBox;
    @FXML
    private JFXTextField agentScoreTextField;

    private ObservableList<OCEAgent> oceAgentList;
    private ObservableList<OCEAgent> filteredOCEAgentList;

    private Optional<ServiceAgent> selectedServiceAgentKnowledge;
    //Variables used to create one entry of a reference situation
    private Optional<ServiceAgent> selectedServiceAgentEntry;
    private Optional<Double> scoreEntry;

    private Matching matcher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialise the matcher
        this.matcher = new Matching();

        this.selectedServiceAgentKnowledge = Optional.empty();
        //Initialize the variables used to create one entry of a reference situation
        this.selectedServiceAgentEntry = Optional.empty();
        this.scoreEntry = Optional.empty();

        //Initialize the lists
        this.oceAgentList = FXCollections.observableArrayList();
        this.filteredOCEAgentList = FXCollections.observableArrayList();

        //set the list of agents as items for the listView
        this.agentsViewList.setItems(this.oceAgentList);

        //Add a custom Cell for the agent
        this.agentsViewList.setCellFactory(a -> new AgentUIPersonalisedCell());
        this.agentsViewList.setExpanded(true);

        //Add a selection listener to the  list view of agents
        this.agentsViewList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OCEAgent>() {
            @Override
            public void changed(ObservableValue<? extends OCEAgent> observable, OCEAgent oldValue, OCEAgent newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(agentsViewList.getSelectionModel().getSelectedItem() != null)
                {
                    //Clear all the fields when we select another agent
                    clearAllFields();
                    clearKnowledgeUIFields();
                    ServiceAgent selectedOCEServiceAgent = (ServiceAgent) agentsViewList.getSelectionModel().getSelectedItem(); // cast the selected item to Service agent (we can do this because the list of agents contains only the service agents)
                    selectedServiceAgentKnowledge = Optional.ofNullable(selectedOCEServiceAgent); // Put the reference of the selected agent
                    //Clear the filtered list of agent (cause we selected a new agent)
                    filteredOCEAgentList.clear();
                    //Add to the combo box the list of agent that are compatible with th selected agent
                    filterMatchingAgent(selectedOCEServiceAgent, oceAgentList, filteredOCEAgentList);
                    agentComboBox.getItems().setAll(filteredOCEAgentList);
                }
            }
        });
        //Add a change listener to the comboBox where we show agent compatible with our selected one
        this.agentComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OCEAgent>() {
            @Override
            public void changed(ObservableValue<? extends OCEAgent> observable, OCEAgent oldValue, OCEAgent newValue) {
                System.out.println("ComboBox changed from " + oldValue + " to " + newValue);
                selectedServiceAgentEntry = Optional.ofNullable((ServiceAgent)newValue);
            }
        });
        //Add a listener for the value of score TextField
        this.agentScoreTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                System.out.println("textfield changed from " + oldValue + " to " + newValue); scoreEntry = Optional.ofNullable(Double.parseDouble(newValue));
            }

        });
    }

    /**
     * Function used to send the data to visualise to this stage (window)
     * @param listOCEAgent : list of the agent present in OCE
     */
    public void initData(List<OCEAgent> listOCEAgent) {
        //Save in the local variable the list of agents received
        for (OCEAgent oceAgent: listOCEAgent){
            if(oceAgent instanceof ServiceAgent){ // Keep only the list of service Agent since they are the only ones which have Knowledge
                this.oceAgentList.add(oceAgent); // This will trigger automatically the update in the list view
            }
        }
    }

    /**
     * Filter the list of all the agent and keep only the service agents that matches with the one send as parameter (i.e : the service agents that handle a service matching the one of the agent send as parameter)
     * @param serviceAgent  :   the service agent
     * @param oceAgentList  :   the list of all service agents
     * @param filteredOCEAgentList  :  the filtered list that will contain the set of service agent that matches to the service agent send as a parameter
     */
    public void filterMatchingAgent(ServiceAgent serviceAgent, ObservableList<OCEAgent> oceAgentList, ObservableList<OCEAgent> filteredOCEAgentList){

        for (OCEAgent currentOCEAgent: oceAgentList) {
            //Cast to a service agent
            ServiceAgent currentOCEServiceAgent = (ServiceAgent) currentOCEAgent;
            if(this.matcher.match(serviceAgent.getHandledService(), currentOCEServiceAgent.getHandledService())){
                filteredOCEAgentList.add(currentOCEAgent);
            }

        }
    }

    @FXML
    public void addRSEntry(ActionEvent event){
        if(this.selectedServiceAgentEntry.isPresent() && this.scoreEntry.isPresent()){
            ReferenceSituationEntry referenceSituationEntry = new ReferenceSituationEntry(this.selectedServiceAgentEntry.get().getMyID(), this.scoreEntry.get());
            this.currentlyConstructedRSViewList.getItems().add(referenceSituationEntry);
            //Clear the fields
            clearFieldsAddEntry();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The selected service field or the score field must not be empty !");
            alert.show();
        }
    }

    @FXML
    public void addRS(ActionEvent event){
        if(this.currentlyConstructedRSViewList.getItems().size()>0){
            Situation<ReferenceSituationEntry> situationToAdd = new Situation<>();
            for(ReferenceSituationEntry currentEntry : this.currentlyConstructedRSViewList.getItems()){
                situationToAdd.addSituationEntry(currentEntry.getAgent(), currentEntry);
            }
            this.currentlyConstructedRSViewList.getItems().clear();
            this.RSViewList.getItems().add(situationToAdd);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Their is no reference situations entries to be added !");
            alert.show();
        }
    }

    @FXML
    public void UpdateKnowledge(ActionEvent event){
        if(this.selectedServiceAgentKnowledge.isPresent()){
            if(this.RSViewList.getItems().size()>0){
                for (Situation<ReferenceSituationEntry> referenceSituation: this.RSViewList.getItems()) {
                    this.selectedServiceAgentKnowledge.get().getMyKnowledgeBase().add(referenceSituation);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" The knowledge base of the agent was updated successfully !");
                alert.show();
                //Clear the fields
                clearAllFields();
                clearKnowledgeUIFields();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The list of reference situations must not be empty !");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select an agent from the list to update it's knowledge !");
            alert.show();
        }
    }

    @FXML
    private void closeWindow(ActionEvent actionEvent){
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.showAndWait();
        alertConfirm.setTitle("Close edit knowledge window");
        alertConfirm.setContentText("Are you sure of willing to close the edit knowledge window?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            // close the dialog.
            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        }


    }

    @FXML
    public void cancelAddingEntry(ActionEvent event){
        clearFieldsAddEntry();
    }

    @FXML
    public void deleteTheCurrentRS(ActionEvent event){
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.showAndWait();
        alertConfirm.setTitle("Delete current RS");
        alertConfirm.setContentText("Are you sure of willing to delete the reference situation under construction?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.clearFieldsAddEntry();
            this.currentlyConstructedRSViewList.getItems().clear();
        }
    }

    @FXML
    public void deleteListRS(ActionEvent event){
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.showAndWait();
        alertConfirm.setTitle("Delete the list of RS");
        alertConfirm.setContentText("Are you sure of willing to delete the reference situations?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.clearFieldsAddEntry();
            this.currentlyConstructedRSViewList.getItems().clear();
            this.RSViewList.getItems().clear();
        }
    }

    /**
     * Clear all the fields of the Add Entry panel (UI and the variables)
     */
    private void clearFieldsAddEntry(){
        this.selectedServiceAgentEntry = Optional.empty();
        this.scoreEntry = Optional.empty();
        this.agentScoreTextField.setText("");
        this.agentComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Clear all the variables (except the agent list)
     */
    private void clearAllFields(){
        selectedServiceAgentKnowledge = Optional.empty();
        this.selectedServiceAgentEntry = Optional.empty();
        this.scoreEntry = Optional.empty();
        this.filteredOCEAgentList.clear();
    }

    /**
     * Clear all the UI fields of the knowledge panel  (except the agent list)
     */
    private void clearKnowledgeUIFields(){
        this.RSViewList.getItems().clear();
        this.currentlyConstructedRSViewList.getItems().clear();
        this.agentComboBox.getItems().clear();
        this.agentScoreTextField.setText("");
    }


}
