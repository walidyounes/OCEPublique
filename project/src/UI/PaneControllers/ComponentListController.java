package UI.PaneControllers;

import AmbientEnvironment.MockupCompo.MockupComponent;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

import static UI.UIMockupController.textFlowDisplayingComponent;

public class ComponentListController implements Initializable {

    @FXML
    private JFXListView<TextFlow> envComponentsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.envComponentsList.setExpanded(true);
    }

    public void addMockupComponent(MockupComponent mockupComponent) {
        envComponentsList.getItems().add(textFlowDisplayingComponent(mockupComponent));
    }

    public void removeMockupComponent(MockupComponent mockupComponent)  {
        envComponentsList.getItems().removeIf(
                textFlow -> mockupComponent.getName().equals( ((Text) textFlow.getChildren().get(0)).getText() )
        );
    }

    public void reset() {
        envComponentsList.getItems().clear();
    }
}
