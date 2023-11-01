package com.zeus.App;

import com.zeus.utils.config.Config;
import com.zeus.App.Controller.Menu;
import com.zeus.utils.file.FileManager;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.URISyntaxException;

public class App extends Application {
    private static Config window = null;
    public static void setWindow(Config config) {
        window = config;
    }

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
    public void start(Stage stage) throws IOException, URISyntaxException {
        Parent root = FileManager.loadFXML("/com/zeus/fxml/index.fxml");
        Scene scene = new Scene(root, 787, 492);
        scene.getStylesheets().add(FileManager.getPathFromFile("/com/zeus/css/index.css"));
        handleKeyEvent(scene);
        initialize(stage);
        stage.setScene(scene);
        stage.show();
    }

    public void handleKeyEvent(Scene sc){

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
        String title = window.getProperty("title", String.class);
        String iconPath = window.getProperty("iconPath", String.class);
        int WIDTH = window.getProperty("WIDTH", Integer.class);
        int HEIGHT = window.getProperty("HEIGHT", Integer.class);
        boolean resizable = window.getProperty("resizable", Boolean.class);
        boolean fullScreen = window.getProperty("fullScreen", Boolean.class);

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