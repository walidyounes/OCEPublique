/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;


public class UIMockup extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UIMockup-2.fxml"));
        Parent root = fxmlLoader.load();
        UIMockupController uiMockupController =  fxmlLoader.getController();
        */
        Parent root = FXMLLoader.load(getClass().getResource("UIMockup_V3.fxml"));
        primaryStage.setTitle("Mockup");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
