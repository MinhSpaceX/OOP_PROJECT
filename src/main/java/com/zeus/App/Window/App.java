package com.zeus.App.Window;

import com.sun.javafx.application.LauncherImpl;
import com.zeus.App.Controller.SplashController;
import com.zeus.App.SearchManager;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.config.Config;
import com.zeus.utils.file.FileManager;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class App extends Application {
    private static Config window = null;
    public static void setWindow(Config config) {
        window = config;
    }

    @Override
    public void init() throws Exception {
        SplashController splashController = new SplashController();
        splashController.init();
    }

    /**
     * Main method to start the app.
     * @param args User arguments.
     */
    public static void run(String[] args) {
        LauncherImpl.launchApplication(App.class, SplashScreen.class, args);
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
        scene.getStylesheets().add(new File(FileManager.getPathFromFile("/com/zeus/css/index.css")).toURI().toURL().toExternalForm());
        initialize(stage);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initialize method use to init elements needed to run.
     * @param stage The main stage.
     */
    private void initialize(Stage stage) throws FileNotFoundException, UnsupportedEncodingException {
        getConfig(stage);
    }

    /**
     * Get configs from JSON config file.
     * @param stage The main stage.
     */
    static void getConfig(Stage stage) throws FileNotFoundException, UnsupportedEncodingException {
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