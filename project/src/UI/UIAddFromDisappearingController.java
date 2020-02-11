/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.OCPlateforme.OCComponent;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class UIAddFromDisappearingController implements Initializable {

    @FXML private JFXListView<MockupComponent> componentsViewList;
    private ObservableList<MockupComponent> mockupComponentsList;
    private Optional<MockupComponent> chosenComponent;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.chosenComponent = Optional.empty();
        this.mockupComponentsList = FXCollections.observableArrayList();
        //set the list of components as items for the listView
        this.componentsViewList.setItems(this.mockupComponentsList);

        //Add a custom Cell for the agent (with a tooltip containing its information)
        this.componentsViewList.setCellFactory(a -> new ComponentUIPersonalisedCell());
        this.componentsViewList.setExpanded(true);
    }

    /**Function used to send the data to visualise to this stage (window)
     *
     * @param listComponents : list of components to display in this window
     */
    public void initData(List<OCComponent> listComponents) {
        //Save  in the local variable the list of components received
        for (OCComponent component: listComponents ){
            this.mockupComponentsList.add((MockupComponent) component); // This will trigger automatically the update in the list view
        }
    }

    @FXML
    private void cancelAction(javafx.event.ActionEvent event){
        this.chosenComponent = Optional.empty();
        // close the dialog.
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void choseAction(ActionEvent event){
        //Get the selected component from the list
        this.chosenComponent = Optional.ofNullable(this.componentsViewList.getSelectionModel().getSelectedItem());

        // close the dialog.
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public Optional<MockupComponent> getChosenComponent() {
        return chosenComponent;
    }
}
