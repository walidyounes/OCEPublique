package UI.PaneControllers;

import OCE.Agents.ServiceAgentPack.Learning.SituationUtility;
import OCE.Agents.ServiceAgentPack.ServiceAgent;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AgentParametersController implements Initializable {

    @FXML private JFXSlider noveltyCoefficientSlider;
    @FXML private JFXSlider learningRateSlider;
    @FXML private JFXSlider reinforcementSlider;
    @FXML private JFXSlider similarityThresholdSlider;
    @FXML private JFXSlider epsilonSlider;

    private AgentsPaneController agentsPaneController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.noveltyCoefficientSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SituationUtility.CSN = newValue.doubleValue();
            }
        });
        this.learningRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsPaneController.getAgentsListUI().getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setLearningRate(newValue.doubleValue()));
            }
        });
        this.reinforcementSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsPaneController.getAgentsListUI().getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setBeta(newValue.doubleValue()));
            }
        });
        this.similarityThresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsPaneController.getAgentsListUI().getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setSimilarityThreshold(newValue.doubleValue()));
            }
        });
        this.epsilonSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsPaneController.getAgentsListUI().getItems().stream().filter(agent-> agent instanceof ServiceAgent).forEach(agent -> ((ServiceAgent)agent).setEpsilon(newValue.doubleValue()));
            }
        });
    }

    public void setAgentsPaneController(AgentsPaneController agentsPaneController) {
        this.agentsPaneController = agentsPaneController;
    }
}
