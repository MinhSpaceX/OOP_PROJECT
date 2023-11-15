package com.zeus.App.Controller;

import com.zeus.DatabaseManager.SQLite;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

    public void displayHistory() {
        for(String i : historyList){
            HBox hBox = new HBox();
            Label label = new Label(i);
            hBox.getChildren().add(label);//HERE
            hBox.getChildren().add(new Label(i));//HERE
            label.setText(i);
            label.getStyleClass().add("label-style");
            label.setOnMouseClicked(e -> {
                SceneContainer sc = SceneContainer.sceneContainer;
                WordView.setMenuLabel(label);
                historyList.remove(i);
                sc.changeView("/com/zeus/fxml/WordView.fxml");
            });
            historyDisplay.getChildren().add(hBox);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayHistory();
    }
}
