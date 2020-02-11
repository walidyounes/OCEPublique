/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import OCE.Unifieur.Matching;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UIInitializeAgentLearningController implements Initializable {

    @FXML
    private JFXListView<OCEAgent> agentsViewList;
    @FXML
    private JFXComboBox<OCEAgent> agentComboBox;
    @FXML
    private JFXTextField agentScore;

    private ObservableList<OCEAgent> oceAgentList;
    private ObservableList<OCEAgent> filteredOCEAgentList;

    private Matching matcher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialise the matcher
        this.matcher = new Matching();
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
                    ServiceAgent selectedOCEServiceAgent = (ServiceAgent) agentsViewList.getSelectionModel().getSelectedItem(); // cast the selected item to Service agent (we can do this because the list of agents contains only the service agents)
                    //Add to the combo box the list of agent that are compatible with th selected agent
                    filterMatchingAgent(selectedOCEServiceAgent, oceAgentList, filteredOCEAgentList);
                    agentComboBox.getItems().setAll(filteredOCEAgentList);
                }
            }
        });

    }

    /**
     * Function used to send the data to visualise to this stage (window)
     * @param listOCEAgent : list of the agent present in OCE
     */
    public void initData(List<OCEAgent> listOCEAgent) {
        //Save in the local variable the list of agents received
        for (OCEAgent oceAgent: listOCEAgent ){
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
}
