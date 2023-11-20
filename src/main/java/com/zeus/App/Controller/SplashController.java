package com.zeus.App.Controller;

import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.utils.ConfigFactory.ConfigFactory;
import com.zeus.Managers.Database.MongoManager;
import com.zeus.Managers.Database.SQLite;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.Managers.SystemApp.SystemManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private Label progressText;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane splashScreen;

    private static Label progressTextt;
    private MongoManager mgp;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressTextt = progressText;
    }

    public void init() {
        Logger.info("init");
        ManagerFactory.initAllManager(manager -> Platform.runLater(() -> progressTextt.setText(manager.getInitMessage())));
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) splashScreen.getScene().getWindow();
        stage.setX(event.getScreenX() - event.getSceneX());
        stage.setY(event.getScreenY() - event.getSceneY());
    }
}
