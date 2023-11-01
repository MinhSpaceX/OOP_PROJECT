package com.zeus.App;

import com.zeus.App.Config.Config;
import com.zeus.utils.file.FileManager;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    /**
     * Main method to start the app.
     * @param args User arguments.
     */
    public static void run(String[] args) {
        launch(args);
    }

    /**
     * Start the stage, this method doesn't need to be called manually.
     * @param stage The stage to run.
     * @throws IOException Exceptions handle.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FileManager.loadFXML("/com/zeus/fxml/index.fxml");
        Scene scene = new Scene(root, 787, 492);
        scene.getStylesheets().add(getClass().getResource("/com/zeus/css/index.css").toExternalForm());
        initialize(stage);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialize method use to init elements needed to run.
     * @param stage The main stage.
     */
    private void initialize(Stage stage) {
        getConfig(stage);
    }

    /**
     * Get configs from JSON config file.
     * @param stage The main stage.
     */
    private void getConfig(Stage stage) {
        //Create a config from the target string to get config from config.json then extract properties
        Config window = FileManager.getConfig("WindowConfig", "/com/zeus/config/config.json");
        assert window != null;
        String title = window.getProperties().getProperties().get("title").toString();
        String iconPath = window.getProperties().getProperties().get("iconPath").toString();
        int WIDTH = Integer.parseInt(window.getProperties().getProperties().get("WIDTH").toString());
        int HEIGHT = Integer.parseInt(window.getProperties().getProperties().get("HEIGHT").toString());
        boolean resizable = Boolean.parseBoolean(window.getProperties().getProperties().get("resizable").toString());
        boolean fullScreen = Boolean.parseBoolean(window.getProperties().getProperties().get("fullScreen").toString());

        /*
         * Set properties: title, iconPath, WIDTH, HEIGHT, resizable, fullScreen
         */
        stage.setTitle(title);
        stage.getIcons().add(FileManager.loadImage(iconPath));
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(resizable);
        stage.setFullScreen(fullScreen);
    }
}