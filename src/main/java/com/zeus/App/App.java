package com.zeus.App;

import com.zeus.App.Config.Config;
import com.zeus.utils.file.FileManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private String title;
    private String iconPath;
    private int HEIGHT;
    private int WIDTH;
    private boolean resizable;
    private boolean fullScreen;

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FileManager.loadFXML("/com/zeus/fxml/menu.fxml");
        Scene scene = new Scene(root, 640, 480);
        inititialize(stage);
        stage.setScene(scene);
        stage.show();
    }

    private void inititialize(Stage stage) throws IOException {
        getConfig();
        stage.setTitle(title);
        stage.getIcons().add(FileManager.loadImage(iconPath));
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(resizable);
        stage.setFullScreen(fullScreen);
    }

    private void getConfig() {
        Config window = FileManager.getConfig("WindowConfig", "/com/zeus/config/config.json");
        assert window != null;
        title = window.getProperties().getProperties().get("title").toString();
        iconPath = window.getProperties().getProperties().get("iconPath").toString();
        WIDTH = Integer.parseInt(window.getProperties().getProperties().get("WIDTH").toString());
        HEIGHT = Integer.parseInt(window.getProperties().getProperties().get("HEIGHT").toString());
        resizable = Boolean.parseBoolean(window.getProperties().getProperties().get("resizable").toString());
        fullScreen = Boolean.parseBoolean(window.getProperties().getProperties().get("fullScreen").toString());
    }
}