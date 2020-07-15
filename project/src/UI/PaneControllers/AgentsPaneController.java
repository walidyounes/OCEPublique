package UI.PaneControllers;

import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.Learning.ReferenceSituationEntry;
import OCE.Agents.ServiceAgentPack.Learning.Situation;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import UI.AgentUIPersonalisedCell;
import UI.UIMockupController;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AgentsPaneController implements Initializable {

    private UIMockupController uiMockupController;

    @FXML
    private JFXListView<OCEAgent> agentsListUI;

    @FXML private VBox agentDetailsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Add a custom Cell for the agent (with a tooltip containing its information)
        this.agentsListUI.setCellFactory(a -> new AgentUIPersonalisedCell());
        this.agentsListUI.setExpanded(true);

        this.agentsListUI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OCEAgent>() {
            @Override
            public void changed(ObservableValue<? extends OCEAgent> observable, OCEAgent oldValue, OCEAgent newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(agentsListUI.getSelectionModel().getSelectedItem() != null)
                {
                    TitledPane titledPaneAgentDetails;
                    OCEAgent selectedOCEAgent = agentsListUI.getSelectionModel().getSelectedItem();
                    if(selectedOCEAgent instanceof ServiceAgent){
                        titledPaneAgentDetails = createTitledPaneContentServiceAgent((ServiceAgent) selectedOCEAgent);
                    }else {
                        if(selectedOCEAgent instanceof BinderAgent){
                            titledPaneAgentDetails = createTitledPaneContentBinderAgent((BinderAgent) selectedOCEAgent);
                        }else{
                            titledPaneAgentDetails = new TitledPane();
                        }
                    }
                    //Add the label to the agent's details visualisation Pane
                    agentDetailsPane.getChildren().clear();
                    agentDetailsPane.getChildren().add(titledPaneAgentDetails);
                }
            }
        });
    }

    public void reset() {
        agentDetailsPane.getChildren().clear();
    }

    /**
     * Get the service agent's information and create a label to display it on the UI
     * @param serviceAgent  : the service agent in question
     * @return  the information of the agent in a UI TitledPane
     */
    private TitledPane createTitledPaneContentServiceAgent(ServiceAgent serviceAgent){
        TitledPane agentTitledPane = new TitledPane();
        agentTitledPane.setText(" SERVICE AGENT");
        agentTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox contentSA = new VBox();

        Label idAgent = new Label("IDAgent = " + serviceAgent.getMyID());
        Label handledService = new Label("Handled Service  = " + serviceAgent.getHandledService().toString());
        Label agentState = new Label("Agent's state  = " + serviceAgent.getMyConnexionState().toString());
        Label agentConnectedTo = new Label("Agent is connected to  = " + serviceAgent.getConnectedTo().toString());
        Label agentBinderAgent = new Label("Agent's binder Agent  = " + serviceAgent.getMyBinderAgent().toString());

        TitledPane agentKnowledgeBaseTitledPane = new TitledPane();
        agentKnowledgeBaseTitledPane.setText(" Knowledge Base ");
        agentKnowledgeBaseTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
        VBox contentKB = new VBox();

        //iterate over the situationEntries and printThem
        Iterator<Situation<ReferenceSituationEntry>> iterator = serviceAgent.getMyKnowledgeBase().iterator();
        while (iterator.hasNext()){
            Situation<ReferenceSituationEntry> currentSit = iterator.next();
            HBox hBoxKB = new HBox();
            ScrollPane scrollPaneKB = new ScrollPane(hBoxKB);
            Label rSDebut = new Label( " RS = {");
            hBoxKB.getChildren().add(rSDebut);
            currentSit.getAgentSituationEntries().forEach((k,v) -> hBoxKB.getChildren().add(new Label(""+v+" - ")));
            Label rSFin = new Label( " }");
            hBoxKB.getChildren().add(rSFin);
            contentKB.getChildren().add(scrollPaneKB);
        }
        contentKB.getChildren().forEach(node -> node.setStyle("-fx-font-weight: bold; -fx-font-size: 12;"));
        agentKnowledgeBaseTitledPane.setContent(contentKB);

        contentSA.getChildren().add(idAgent);
        contentSA.getChildren().add(handledService);
        contentSA.getChildren().add(agentState);
        contentSA.getChildren().add(agentConnectedTo);
        contentSA.getChildren().add(agentBinderAgent);
        contentSA.getChildren().add(agentKnowledgeBaseTitledPane);

        contentSA.getChildren().forEach( node -> node.setStyle("-fx-font-size: 12;"));
        agentTitledPane.setContent(contentSA);

        return agentTitledPane;
    }

    /**
     * Get the service agent's information and create a label to display it on the UI
     * @param binderAgent  : the service agent in question
     * @return  the information of the agent in a UI TitledPane
     */
    private TitledPane createTitledPaneContentBinderAgent(BinderAgent binderAgent){
        TitledPane agentTitledPane = new TitledPane();
        agentTitledPane.setText(" BINDER AGENT ");
        agentTitledPane.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox contentSA = new VBox();

        Label idAgent = new Label("IDAgent = " + binderAgent.getMyID());
        contentSA.getChildren().add(idAgent);
        if(binderAgent.getFirstService().isPresent() && binderAgent.getSecondService().isPresent()){
            Label firstService = new Label("First service = " + binderAgent.getFirstService().get());
            Label secondService = new Label("Second service = " + binderAgent.getSecondService().get());
            contentSA.getChildren().add(firstService);
            contentSA.getChildren().add(secondService);
        }
        contentSA.getChildren().forEach( node -> node.setStyle("-fx-font-size: 12;"));
        agentTitledPane.setContent(contentSA);

        return agentTitledPane;
    }

    public void setUiMockupController(UIMockupController uiMockupController) {
        this.uiMockupController = uiMockupController;
        System.out.println(agentsListUI == null);
        System.out.println(uiMockupController.getMyOCE() == null);
        this.agentsListUI.setItems(uiMockupController.getMyOCE().gteAllAgents());
    }

    public JFXListView<OCEAgent> getAgentsListUI() {
        return agentsListUI;
    }
}
