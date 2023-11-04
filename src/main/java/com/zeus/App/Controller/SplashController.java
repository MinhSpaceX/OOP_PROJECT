package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoManager;
import com.zeus.System.System;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.utils.managerfactory.SystemManager;
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
    private AnchorPane splashScreen;

    private static Label progressTextt;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressTextt = progressText;
    }

    public void init() {
        ConfigFactory configFactory = SystemManager.getConfigFactory();
        Thread thread2 = new Thread(() -> {
            try {
                Platform.runLater(() -> progressTextt.setText("Loading mongodb database..."));
                ManagerFactory.createManager(MongoManager.class).init(configFactory.getConfig("Database"));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                Platform.runLater(() -> progressTextt.setText("Creating search manager..."));
                ManagerFactory.createManager(SearchManager.class).init(configFactory.getConfig(""));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            thread2.start();
            thread2.join();
            thread3.start();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) splashScreen.getScene().getWindow();
        stage.setX(event.getScreenX() - event.getSceneX());
        stage.setY(event.getScreenY() - event.getSceneY());
    }
}
