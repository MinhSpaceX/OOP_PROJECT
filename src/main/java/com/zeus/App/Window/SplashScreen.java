package com.zeus.App.Window;

import com.zeus.utils.file.FileManager;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {
    private Stage loaderStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.loaderStage = stage;
        Parent root = FileManager.loadFXML("/com/zeus/fxml/splash.fxml");

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        App.getConfig(stage);
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            loaderStage.hide();
        }
    }
}
