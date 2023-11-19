package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.api.APIHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
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
    public void Translate() {
        translate.setOnMouseClicked(e -> {
            if (word.getText().length() == 0) {
                result.setVisible(true);
                result.setText("Sa rang hê ô");
            } else {
                if (SearchManager.search(word.getText())) {
                    SceneContainer sc = SceneContainer.sceneContainer;
                    Label label = new Label(word.getText().toLowerCase());
                    WordView.setMenuLabel(label);
                    sc.changeView("/com/zeus/fxml/WordView.fxml");
                } else {
                    result.setVisible(true);
                    result.setText(APIHandler.translate(word.getText()).get(0).toString());
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
