package com.zeus.App.Controller;

import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.Managers.SystemApp.SystemManager;
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

import java.net.URL;
import java.util.ResourceBundle;

public class SceneContainer implements Initializable {
    public static SceneContainer sceneContainer;
    boolean MenuVisible = false;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneContainer = this;
        changeView(Menu.class);
        setMenuButtonFunction();
        sideNavSlide();
        Logger.info("Container init -----------------");
    }

    public void loadMenuScreen(ActionEvent event) {
        changeView(Menu.class);
    }

    public void sideNavSlide() {
        slider.setTranslateX(-170);
        menuButton.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));

            slide.setNode(slider);

            if (MenuVisible) {
                menuButton.setStyle("-fx-background-color: rgb(119, 82, 254)");
                slide.setToX(-170);
                MenuVisible = false;
            } else {
                menuButton.setStyle("-fx-background-color: rgb(142, 143, 250)");
                slide.setToX(0);
                MenuVisible = true;
            }
            slide.play();
        });
    }

    @FXML
    public void setGameView(ActionEvent event) {
        changeView(GameController.class);
    }

    public <T> void changeView(Class<T> tClass) {
        String FXMLurl = null;
        try {
            FXMLurl = SystemManager.getManager(FxmlManager.class).getPath(tClass);
            setChoosenIcon(tClass);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        viewWindow.getChildren().clear();
        AnchorPane view = null;
        try {
            view = (AnchorPane) FileManager.loadFXML(FXMLurl);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        viewWindow.getChildren().setAll(view);
        Logger.info("View changed!");
    }

    public void setMenuButtonFunction() {
        shortSearch.setOnMouseClicked(e -> changeView(Menu.class));
        shortTranslate.setOnMouseClicked(e -> {
            //changeView("");
        });
        shortFav.setOnMouseClicked(e -> changeView(FavoriteController.class));
        shortHistory.setOnMouseClicked(e -> changeView(History.class));
        shortGame.setOnMouseClicked(e -> changeView(GameController.class));
        shortUpdate.setOnMouseClicked(e -> changeView(AddController.class));
        shortTranslate.setOnMouseClicked(e -> changeView(Translate.class));
    }

    private <T> void setChoosenIcon(Class<T> tClass) {
        clearHighlight();
        if (tClass.equals(Menu.class)) {
            shortSearch.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        } else if (tClass.equals(GameController.class)) {
            shortGame.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        } else if (tClass.equals(AddController.class)) {
            shortUpdate.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        } else if (tClass.equals(History.class)) {
            shortHistory.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        } else if (tClass.equals(FavoriteController.class)) {
            shortFav.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        } else if (tClass.equals(Translate.class)) {
            shortTranslate.setStyle("-fx-border-color: rgb(194, 217, 255); " +
                    "-fx-border-width: 0 0 0 2;");
        }
    }

    private void clearHighlight() {
        shortTranslate.setStyle("-fx-border-width: 0");
        shortHistory.setStyle("-fx-border-width: 0");
        shortFav.setStyle("-fx-border-width: 0");
        shortGame.setStyle("-fx-border-width: 0");
        shortUpdate.setStyle("-fx-border-width: 0");
        shortSearch.setStyle("-fx-border-width: 0");
    }

    @FXML
    public void setAddWord(ActionEvent event) {
        changeView(AddController.class);
    }

    @FXML
    public void setHistory(ActionEvent event) {
        changeView(History.class);
    }

    @FXML
    public void setTranslate(ActionEvent event) {
        changeView(Translate.class);
    }

    @FXML
    public void setFavorite(ActionEvent event) {
        changeView(FavoriteController.class);
    }
}
