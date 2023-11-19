package com.zeus.App.Controller;

import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    private FontAwesomeIconView updateIcon;

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

    @FXML
    private Label shortFav;

    @FXML
    private Label shortGame;

    @FXML
    private Label shortHistory;

    @FXML
    private Label shortSearch;

    @FXML
    private Label shortTranslate;

    @FXML
    private Label shortUpdate;


    boolean MenuVisible = false;
    boolean openWordCard = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneContainer = this;
        changeView("/com/zeus/fxml/menu.fxml");
        setMenuButtonFunction();
        sideNavSlide();
        Logger.info("Container init -----------------");
    }

    public void loadMenuScreen(ActionEvent event) throws IOException {
        changeView("/com/zeus/fxml/menu.fxml");
    }

    public void sideNavSlide(){
        slider.setTranslateX(-170);
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

    @FXML
    public void setGameView(ActionEvent event){
        changeView("/com/zeus/fxml/GameScene.fxml");
    }

    public void changeView(String FXMLurl){
        setChoosenIcon(FXMLurl);
        viewWindow.getChildren().clear();
        AnchorPane view = null;
        try {
            view = (AnchorPane) FileManager.loadFXML(FXMLurl);
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
        viewWindow.getChildren().setAll(view);
        Logger.info("View changed!");
    }

    public void setMenuButtonFunction(){
        shortSearch.setOnMouseClicked(e->{
            changeView("/com/zeus/fxml/menu.fxml");
        });
        shortTranslate.setOnMouseClicked(e->{
            //changeView("");
        });
        shortFav.setOnMouseClicked(e->{
            changeView("/com/zeus/fxml/FavoriteScene.fxml");
        });
        shortHistory.setOnMouseClicked(e->{
            changeView("/com/zeus/fxml/history.fxml");
        });
        shortGame.setOnMouseClicked(e->{
            changeView("/com/zeus/fxml/GameScene.fxml");
        });
        shortUpdate.setOnMouseClicked(e->{
            changeView("/com/zeus/fxml/UpdateLobby.fxml");
        });
    }

    private void setChoosenIcon(String FXMLurl){
        switch (FXMLurl){
            case "/com/zeus/fxml/menu.fxml":
                shortSearch.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                        "-fx-border-width: 0 0 0 2;");
                shortGame.setStyle("-fx-border-width: 0");
                shortFav.setStyle("-fx-border-width: 0");
                shortTranslate.setStyle("-fx-border-width: 0");
                shortUpdate.setStyle("-fx-border-width: 0");
                shortHistory.setStyle("-fx-border-width: 0");
                break;
            case "/com/zeus/fxml/GameScene.fxml":
                shortGame.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                        "-fx-border-width: 0 0 0 2;");
                shortFav.setStyle("-fx-border-width: 0");
                shortTranslate.setStyle("-fx-border-width: 0");
                shortUpdate.setStyle("-fx-border-width: 0");
                shortHistory.setStyle("-fx-border-width: 0");
                shortSearch.setStyle("-fx-border-width: 0");
                break;
            case "/com/zeus/fxml/UpdateLobby.fxml":
                shortUpdate.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                        "-fx-border-width: 0 0 0 2;");
                shortFav.setStyle("-fx-border-width: 0");
                shortTranslate.setStyle("-fx-border-width: 0");
                shortGame.setStyle("-fx-border-width: 0");
                shortHistory.setStyle("-fx-border-width: 0");
                shortSearch.setStyle("-fx-border-width: 0");
                break;
            case "/com/zeus/fxml/history.fxml":
                shortHistory.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                        "-fx-border-width: 0 0 0 2;");
                shortFav.setStyle("-fx-border-width: 0");
                shortTranslate.setStyle("-fx-border-width: 0");
                shortGame.setStyle("-fx-border-width: 0");
                shortUpdate.setStyle("-fx-border-width: 0");
                shortSearch.setStyle("-fx-border-width: 0");
                break;
            case "/com/zeus/fxml/FavoriteScene.fxml":
                shortFav.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                        "-fx-border-width: 0 0 0 2;");
                shortHistory.setStyle("-fx-border-width: 0");
                shortTranslate.setStyle("-fx-border-width: 0");
                shortGame.setStyle("-fx-border-width: 0");
                shortUpdate.setStyle("-fx-border-width: 0");
                shortSearch.setStyle("-fx-border-width: 0");
                break;
            default:
                break;
        }
    }

    @FXML
    public void setAddWord(ActionEvent event) {
        changeView("/com/zeus/fxml/UpdateLobby.fxml");
    }

    @FXML
    public void setHistory(ActionEvent event) {
        changeView("/com/zeus/fxml/history.fxml");
    }
    @FXML
    public void setTranslate(ActionEvent event) {
        changeView("/com/zeus/fxml/Translate.fxml");
    }

    @FXML
    public void setFavorite(ActionEvent event){ changeView("/com/zeus/fxml/FavoriteScene.fxml");}
}
