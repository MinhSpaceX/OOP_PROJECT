package com.zeus.App.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Button BlitzMode;

    @FXML
    private Button ClassicMode;

    @FXML
    private AnchorPane GameMenuCard;

    @FXML
    private Button InfinityMode;

    @FXML
    private Button PlayButton;

    String gameMode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGameMode();
    }

    public void setGameMode(){
        SceneContainer sc = SceneContainer.sceneContainer;
        BlitzMode.setOnMouseClicked(event -> {
            BlitzMode.getStyleClass().clear();
            ClassicMode.getStyleClass().clear();
            InfinityMode.getStyleClass().clear();
            BlitzMode.getStyleClass().add("game-mode-choosen");
            ClassicMode.getStyleClass().add("game-mode-button");
            InfinityMode.getStyleClass().add("game-mode-button");
            gameMode = "Blitz";
        });
        ClassicMode.setOnMouseClicked(event -> {
            BlitzMode.getStyleClass().clear();
            ClassicMode.getStyleClass().clear();
            InfinityMode.getStyleClass().clear();
            ClassicMode.getStyleClass().add("game-mode-choosen");
            BlitzMode.getStyleClass().add("game-mode-button");
            InfinityMode.getStyleClass().add("game-mode-button");
            gameMode = "Classic";
        });
        InfinityMode.setOnMouseClicked(event -> {
            BlitzMode.getStyleClass().clear();
            ClassicMode.getStyleClass().clear();
            InfinityMode.getStyleClass().clear();
            InfinityMode.getStyleClass().add("game-mode-choosen");
            BlitzMode.getStyleClass().add("game-mode-button");
            ClassicMode.getStyleClass().add("game-mode-button");
            gameMode = "Classic";
        });
        PlayButton.setOnMouseClicked(e -> {
            sc.changeView("/com/zeus/fxml/GameScene2.fxml");
        });
    }

}