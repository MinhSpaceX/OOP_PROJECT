package com.zeus.App.Controller;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.log.Logger;
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

    /**
     * set up translate function through API
     * @param url ...
     * @param resourceBundle ...
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translate.setOnMouseClicked(e -> {
            if (word.getText().isEmpty()) {
                result.setVisible(true);
                result.setText("Không có bản dịch.");
            } else {
                if (SearchManager.search(word.getText())) {
                    SceneContainer sc = SceneContainer.sceneContainer;
                    Label label = new Label(word.getText().toLowerCase());
                    WordView.setMenuLabel(label);
                    sc.changeView(WordView.class);
                } else {
                    result.setVisible(true);
                    List<String> translateResults = APIHandler.translate(word.getText());
                    if (translateResults == null) try {
                        throw new Exception("Translate result list return null");
                    } catch (Exception ex) {
                        result.setText("");
                        Logger.printStackTrace(ex);
                        return;
                    }
                    result.setText(translateResults.get(0));
                }
            }
        });
        erase.setOnMouseClicked(e -> {
            word.clear();
            result.clear();
        });
        result.setEditable(false);
    }
}