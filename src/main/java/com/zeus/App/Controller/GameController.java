package com.zeus.App.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the game menu
 */
public class GameController implements Initializable {

    public static GameController gameController;
    //game mode string with default is "Classic"
    String gameMode = "Classic";
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

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     *
     * @param url            points to the FXML file that corresponds to the controller class.
     * @param resourceBundle optional parameter.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameController = this;
        setGameMode();
    }

    /**
     * get the game mode that user want to play and change
     * the button's style to determine which button is chosen.
     */
    public void setGameMode() {
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
            gameMode = "Infinity";
        });
        PlayButton.setOnMouseClicked(e -> sc.changeView(GameController2.class));
    }

    /**
     * this method will be used in {@link GameController2} to display
     * scene for each game mode.
     *
     * @return game mode.
     */
    public String getGameMode() {
        //Logger.info(gameMode);
        return gameMode;
    }

}
