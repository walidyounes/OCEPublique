/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import AmbientEnvironment.MockupCompo.MockupService;
import OCE.Agents.BinderAgentPack.BinderAgent;
import OCE.Agents.OCEAgent;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import com.jfoenix.controls.JFXListCell;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class AgentUIPersonalisedCell extends JFXListCell<OCEAgent> {

    private final GridPane gridPane = new GridPane();
    private final AnchorPane content = new AnchorPane();

    private final Label agentType = new Label();
    private final ImageView agentIcon = new ImageView();
    private final Tooltip agentTooltip = new Tooltip();


    public AgentUIPersonalisedCell() {
        agentIcon.setFitWidth(50);
        agentIcon.setPreserveRatio(true);

        GridPane.setConstraints(agentIcon, 0, 0, 1, 3);
        GridPane.setValignment(agentIcon, VPos.TOP);
        //
        agentType.setStyle("-fx-font-weight: bold; -fx-font-size: 1.5em;");
        GridPane.setConstraints(agentType, 1, 0);


        //
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().setAll(agentIcon, agentType);
        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
        content.getChildren().add(gridPane);
    }

    @Override
    protected void updateItem(OCEAgent item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!empty && item != null) {
            if (item instanceof ServiceAgent) {
                String serviceID = item.getMyID().getVisualizingName();
                this.agentType.setText(serviceID);
                this.agentIcon.setImage(new Image("/AgentNC.png"));
                String toolTipContent = createToolTipContentServiceAgent((ServiceAgent) item);
                this.agentTooltip.setText(toolTipContent);
            }
            if (item instanceof BinderAgent){
                this.agentType.setText("Binder Agent");
                this.agentIcon.setImage(new Image("/AgentC.png"));
                String toolTipContent = createToolTipContentBinderAgent((BinderAgent) item);
                this.agentTooltip.setText(toolTipContent);
            }
            this.agentTooltip.setStyle("-fx-font-size: 16px;");
            setTooltip(this.agentTooltip);
            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private String createToolTipContentServiceAgent(ServiceAgent serviceAgent){
        String content=" SERVICE AGENT \n";
        content += "IDAgent = " + serviceAgent.getMyID() + "\n";
        content += "Handled service = " + ((MockupService)serviceAgent.getHandledService()).toString() +"\n";
//        content += "Type = " + ((MockupService) serviceAgent.getHandledService()).getType() +"\n";
        content += "Knowledge Base = " + serviceAgent.getMyKnowledgeBase() + "\n";
        return content;
    }

    private String createToolTipContentBinderAgent(BinderAgent binderAgent){
        String content=" BINDER AGENT \n";
        content += "IDAgent = " + binderAgent.getMyID() + "\n";
        if(binderAgent.getFirstService().isPresent() && binderAgent.getSecondService().isPresent()){
            content += "First service = " + binderAgent.getFirstService().get() + "\n";
            content += "Second service = " + binderAgent.getSecondService().get() + "\n";
        }

        return content;
    }
}
