/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class UIMockupController implements Initializable {
    @FXML private JFXTextField designationComponent;
    @FXML private JFXTextField nameService;
    @FXML private JFXListView<Label> servicesList;
    @FXML private JFXListView<Label> componentsList;
    @FXML private JFXRadioButton providedR,requiredR,singleR,multipleR;

    private ImageView requiredImage,providedImage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.servicesList.setExpanded(true);
        this.componentsList.setExpanded(true);
        this.providedR.setSelected(true);
        this.singleR.setSelected(true);
        this.servicesList.getItems().add(new Label("service "));
    }

    @FXML
    private void addService(ActionEvent event){
        System.out.println("adding services " + this.nameService.getText().length());
        Label label = new Label();

        if(this.nameService.getText().length()>0){
            String textToAdd = "Name = " + this.nameService.getText();
            try {
                if(this.providedR.isSelected()){
                    providedImage =new ImageView(new Image(new FileInputStream("C:\\Users\\wyounes\\Desktop\\provided.png")));
                    label.setGraphic(providedImage);
                }else{
                    if(this.requiredR.isSelected()){
                        requiredImage =new ImageView(new Image(new FileInputStream("C:\\Users\\wyounes\\Desktop\\required.png")));
                        label.setGraphic(requiredImage);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(this.singleR.isSelected()){
                textToAdd += " -- Type = SINGLE";
            }else{
                if(this.multipleR.isSelected()){
                    textToAdd += " -- Type = MULTIPLE";
                }
            }
            label.getStyleClass().add("label-list");
            label.setText(textToAdd);
            this.servicesList.getItems().add(label);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill the name of the service first !!");
            alert.show();
        }
    }

}
