package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.DatabaseManager.MongoManager;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.log.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordView implements Initializable {
    @FXML
    private TextField searchBar2 = new TextField();

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
    VBox resultDisplay2 = new VBox();

    @FXML
    Label pronounDisplay;

    Map<String, List<SingleWord>> result;

    private List<String> temp = new ArrayList<>();

    private static Label menuLabel;
    private Map<String, Boolean> getClickCss = new HashMap<>();

    public static void setMenuLabel(Label label) {
        menuLabel = label;
    }

    MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MediaHandler();
        searchWord();
        searchBar2.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                resultDisplay2.getChildren().get(0).requestFocus();
            }
        });
        resultDisplay2.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                int currentIndex = resultDisplay2.getChildren().indexOf(resultDisplay2.getScene().getFocusOwner());
                int nextIndex = (currentIndex + (event.getCode() == KeyCode.DOWN ? 1 : -1) + resultDisplay2.getChildren().size()) % resultDisplay2.getChildren().size();
                resultDisplay2.getChildren().get(nextIndex).requestFocus();
                event.consume();
            }
        });
        displayLabelContent(menuLabel);
        Logger.info("WordView init -----------------");
    }

    public void setToDefault(){
        searchBar2.clear();
    }

    private void filterData(String input){
        temp = SearchManager.searchFilter(input).stream().distinct().collect(Collectors.toList());
        if(temp.isEmpty()){
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
            resultDisplay2.getChildren().add(label);
            return;
        }
        if(temp.size() == 2){
            temp.remove(0);
        }
        for(var i : temp){
            Label label = new Label(i);
            label.getStyleClass().add("label-style");
            label.setOnMouseClicked(e -> displayLabelContent(label));
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) displayLabelContent(label);
            });
            resultDisplay2.getChildren().add(label);
        }
    }

    public void searchWord(){
        resultDisplay2.setVisible(false);
        Logger.info("called");
        searchBar2.setOnKeyReleased(keyEvent -> {
            temp.clear();
            resultDisplay2.getChildren().clear();
            if (!searchBar2.getText().isEmpty()) {
                resultDisplay2.setVisible(true);
                String input = searchBar2.getText();
                filterData(input);
            } else {
                resultDisplay2.setVisible(false);
            }
            if((keyEvent.getTarget() instanceof TextField)){
                searchBar2.requestFocus();
            }
        });
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
        typeContainer.getChildren().clear();
        BackgroundTask.perform(() -> mediaPlayer = APIHandler.getAudio(label.getText()));
        wordTargetDisplay.setText(label.getText());
        result = SearchManager.getWordInstance(label.getText());
        boolean getFirst = true;
        for(var i : result.keySet()){
            Label temp = new Label(i);
            if(getFirst) {
                temp.getStyleClass().add("tab-label");
                DisplayMeaning(temp.getText());
                getClickCss.put(temp.getText(), true);
                getFirst = false;
            }
            else{
                temp.getStyleClass().add("unchoose-tab-label");
                getClickCss.put(temp.getText(), false);
            }
            temp.setOnMouseClicked(e -> DisplayMeaning(temp.getText()));
            typeContainer.getChildren().add(temp);
        }
        setToDefault();
        searchWord();
    }

    public void DisplayMeaning(String type){
        int count = 0;
        getClickCss.put(type, true);
        for (javafx.scene.Node node : typeContainer.getChildren()) {
            if (node instanceof Label) {
                Label temp = (Label) node;
                if (temp.getText().equals(type)) {
                    temp.getStyleClass().remove("unchoose-tab-label");
                    temp.getStyleClass().add("tab-label");
                } else {
                    if(temp.getStyleClass().contains("tab-label")) {
                        temp.getStyleClass().remove("tab-label");
                        temp.getStyleClass().add("unchoose-tab-label");
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
