package com.zeus.App.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController2 implements Initializable {

    @FXML
    private VBox AnswerContainer;

    @FXML
    private AnchorPane GameMenuCard;

    @FXML
    private Label questionDisplay;

    @FXML
    private FontAwesomeIconView backToGameMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneContainer sc = SceneContainer.sceneContainer;
        backToGameMenu.setOnMouseClicked(e->{
            sc.changeView("/com/zeus/fxml/GameScene.fxml");
        });
    }

}
