package com.zeus.App.Controller;

import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.ManagerFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    private static Label staticText;
    @FXML
    private Label progressText;

    /**
     * set the progress text in the splash screen
     *
     * @param url            ...
     * @param resourceBundle ...
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticText = progressText;
    }

    /**
     * start all the manager class
     */
    public void init() {
        Logger.info("init");
        ManagerFactory.initAllManager(manager -> Platform.runLater(() -> staticText.setText(manager.getInitMessage())));
    }
}
