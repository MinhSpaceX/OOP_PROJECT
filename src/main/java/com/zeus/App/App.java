package com.zeus.App;

import com.zeus.utils.file.FileManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static final String title = "Duelingo";
    private static final String iconPath = "/com/zeus/icon/icon.png";
    private static final int HEIGHT = 400;
    private static final int WIDTH = 600;
    private static final boolean resizable = false;
    private static final boolean fullScreen = false;

    public static void main(String[] args) {
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
        stage.setTitle(title);
        stage.getIcons().add(FileManager.loadImage(iconPath));
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(resizable);
        stage.setFullScreen(fullScreen);
    }
}