package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.DatabaseManager.MongoManager;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.log.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordView extends SearchController {
    @FXML
    private AnchorPane wordCard;

    @FXML
    private Text wordTargetDisplay;

    @FXML
    private Button audio;

    @FXML
    private HBox typeContainer;

    @FXML
    private Text meaningDisplay;

    @FXML
    Label pronounDisplay;

    @FXML
    Label userBelongLabel;

    Map<String, List<SingleWord>> result;

    private static Label menuLabel;
    private boolean belongToUser;
    @Override
    protected void initialize() {
        trie = SearchManager.searchPath;
        MediaHandler();
        Platform.runLater(() -> displayLabelContent(menuLabel));
    }
    

    public static void setMenuLabel(Label label) {
        menuLabel = label;
    }

    MediaPlayer mediaPlayer;

    @Override
    protected void displayWordFromLabel(Label label) {
        displayLabelContent(label);
    }

    public void MediaHandler(){
        audio.setOnMouseClicked(event -> {
            try {
                if (mediaPlayer == null) throw new NullPointerException("Media is null, change word or add data.");
                mediaPlayer.pause();
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
        });
    }

    public void displayLabelContent(Label label){
        typeContainer.setPrefWidth(1000);
        History.historyList.add(label.getText());
        typeContainer.getChildren().clear();
        BackgroundTask.perform(() -> mediaPlayer = APIHandler.getAudio(label.getText()));
        wordTargetDisplay.setText(label.getText());
        result = SearchManager.getWordInstance(label.getText());
        if(SearchManager.getUserTrie().search(label.getText())){
            belongToUser = true;
            userBelongLabel.setVisible(true);
        }
        else {
            belongToUser = false;
            userBelongLabel.setVisible(false);
        }
        //System.out.println(result);
        boolean getFirst = true;
        for(var i : result.keySet()) {
            Label autoFillList = new Label(i);
            if (getFirst) {
                autoFillList.getStyleClass().add("tab-label");
                DisplayMeaning(autoFillList.getText());
                //getClickCss.put(autoFillList.getText(), true);
                getFirst = false;
            } else {
                autoFillList.getStyleClass().add("unchoose-tab-label");
                //getClickCss.put(autoFillList.getText(), false);
            }
            autoFillList.setOnMouseClicked(e -> DisplayMeaning(autoFillList.getText()));
            typeContainer.getChildren().add(autoFillList);
        }
        Platform.runLater(() -> {
            double width = 0;
            for (Node child : typeContainer.getChildren()) {
                if (child.isVisible()) {
                    width += child.getBoundsInParent().getWidth();
                }
            }
            typeContainer.setPrefWidth(width);
        });
    }

    public void DisplayMeaning(String type){
        int count = 0;
        //getClickCss.put(type, true);
        for (javafx.scene.Node node : typeContainer.getChildren()) {
            if (node instanceof Label) {
                Label autoFillList = (Label) node;
                if (autoFillList.getText().equals(type)) {
                    autoFillList.getStyleClass().clear();
                    autoFillList.getStyleClass().add("tab-label");
                } else {
                    if(autoFillList.getStyleClass().contains("tab-label")) {
                        autoFillList.getStyleClass().clear();
                        autoFillList.getStyleClass().add("unchoose-tab-label");
                    }
                }
            }
        }
        StringBuilder str = new StringBuilder();
        meaningDisplay.setText("");
        pronounDisplay.setText("");
        pronounDisplay.setText("/" + result.get(type).get(0).getPronoun() + "/");
        for(var i : result.get(type)) {
            str.append(i.toString());
        }
        meaningDisplay.setText(str.toString());
    }
}
