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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticText = progressText;
    }

    public void init() {
        Logger.info("init");
        ManagerFactory.initAllManager(manager -> Platform.runLater(() -> staticText.setText(manager.getInitMessage())));
    }
}
