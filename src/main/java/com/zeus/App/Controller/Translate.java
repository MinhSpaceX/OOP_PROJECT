package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.log.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Translate implements Initializable {
    @FXML
    TextArea word;
    @FXML
    TextArea result;
    @FXML
    Button translate;
    @FXML
    Button erase;
    final String[] meaning = new String[1];
    public void Translate() {
        word.setOnKeyReleased(keyEvent -> {
            BackgroundTask.perform(()->{
                List<String> translations = APIHandler.translate(word.getText());
                if (translations.isEmpty()) {
                    meaning[0] = "Sa rang hê ô";
                } else {
                    meaning[0] = translations.get(0).toString();
                }
            });
        });
        translate.setOnMouseClicked(e -> {
            if (word.getText().length() == 0) {
                result.setVisible(true);
                result.setText("Sa rang hê ô");
            } else {
                if (SearchManager.search(word.getText())) {
                    Platform.runLater(() -> {
                        SceneContainer sc = SceneContainer.sceneContainer;
                        Label label = new Label(word.getText().toLowerCase());
                        WordView.setMenuLabel(label);
                        sc.changeView("/com/zeus/fxml/WordView.fxml");
                    });
                } else {
                    Platform.runLater(() -> {
                        //List<String> translations = APIHandler.translate(word.getText());
                        //meaning[0] = translations.get(0).toString();
                        result.setVisible(true);
                        result.setText(meaning[0]);
                    });
                }
            }
        });
        erase.setOnMouseClicked(e -> {
            word.clear();
            result.clear();
            result.setVisible(false);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Translate();
    }
}
