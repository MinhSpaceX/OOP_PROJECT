package com.zeus.App.Controller;

import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneContainer implements Initializable {
    public static SceneContainer sceneContainer;
    @FXML
    private FontAwesomeIconView menuIcon;
    @FXML
    private FontAwesomeIconView searchIcon;
    @FXML
    private FontAwesomeIconView translateIcon;
    @FXML
    private FontAwesomeIconView gameIcon;

    @FXML
    private FontAwesomeIconView heartIcon;

    @FXML
    private FontAwesomeIconView historyIcon;

    @FXML
    private Button menuButton;

    @FXML
    private AnchorPane slider;

    @FXML
    private AnchorPane viewWindow = new AnchorPane();

    boolean MenuVisible = false;
    boolean openWordCard = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneContainer = this;
        changeView("/com/zeus/fxml/menu.fxml");
        sideNavSlide();
        Logger.info("Container init -----------------");
    }

    public void loadMenuScreen(ActionEvent event) throws IOException {
        changeView("/com/zeus/fxml/menu.fxml");
    }

    public void sideNavSlide(){
        slider.setTranslateX(-170);
        DenyIcon();
        menuButton.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));

            slide.setNode(slider);

            if(MenuVisible) {
                menuButton.setStyle("-fx-background-color: rgb(119, 82, 254)");
                slide.setToX(-170);
                MenuVisible = false;
            }
            else{
                menuButton.setStyle("-fx-background-color: rgb(142, 143, 250)");
                slide.setToX(0);
                MenuVisible = true;
            }
            slide.play();
        });
    }

    /**
     * Prevent icon from blocking user clicking the button
     */
    public void DenyIcon(){
        menuIcon.setMouseTransparent(true);
        searchIcon.setMouseTransparent(true);
        translateIcon.setMouseTransparent(true);
        gameIcon.setMouseTransparent(true);
        heartIcon.setMouseTransparent(true);
        historyIcon.setMouseTransparent(true);
    }

    @FXML
    public void setGameView(ActionEvent event){
        changeView("/com/zeus/fxml/GameScene.fxml");
    }

    public void changeView(String FXMLurl){
        viewWindow.getChildren().clear();
        AnchorPane view = new AnchorPane();
        try {
            view = (AnchorPane) FileManager.loadFXML(FXMLurl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        viewWindow.getChildren().setAll(view);
        Logger.info("View changed!");
    }

}
