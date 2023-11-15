package com.zeus.App.Controller;

import com.zeus.DatabaseManager.SQLite;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class History implements Initializable {
    @FXML
    VBox historyDisplay = new VBox();
    public static ArrayList<String> historyList = new ArrayList<String>();

    private HBox createHBox(String i) {
        HBox hBox = new HBox();
        Label label = new Label();
        label.setText(i);
        label.getStyleClass().add("history");
        hBox.getChildren().add(label);
        Image image = new Image("com/zeus/icon/volume-high-solid.png");
        ImageView imageView = new ImageView(image);
        Button button = new Button();
        button.setGraphic(imageView);
        hBox.getChildren().add(button);
        return hBox;
    }
    public void displayHistory() {
        for(String i : historyList){

            HBox hbox = createHBox(i);
            hbox.getChildren().get(0).setOnMouseClicked(e -> {
                SceneContainer sc = SceneContainer.sceneContainer;
                WordView.setMenuLabel((Label) hbox.getChildren().get(0));
                historyList.remove(i);
                sc.changeView("/com/zeus/fxml/WordView.fxml");
            });
            historyDisplay.getChildren().add(hbox);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayHistory();
    }
}
