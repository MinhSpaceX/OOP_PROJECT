package com.zeus.App.Window;

import com.zeus.App.Controller.SplashController;
import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.utils.file.FileManager;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.log.Logger;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {
    private Stage loaderStage;

    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("init");
        this.loaderStage = stage;
        Parent root = FileManager.loadFXML(SystemManager.getManager(FxmlManager.class).getPath(SplashController.class));

        stage.setOnCloseRequest(event -> {
            Platform.exit(); // Close the JavaFX application immediately
            System.exit(0);
        });

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        App.getConfig(stage);
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            loaderStage.close();
        }
    }
}
