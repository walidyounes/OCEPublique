package UI.PaneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VisualizationOptionsController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void launchICEVisualisationProgram(ActionEvent event){
        try {
            // Command to create an external process
            String command = "C:\\GemocStudio";
            // Running the above command
            Runtime run  = Runtime.getRuntime();
            Process proc = run.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
