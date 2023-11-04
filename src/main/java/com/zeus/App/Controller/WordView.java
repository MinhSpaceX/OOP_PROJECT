package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.log.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private Text explainDisplay;

    @FXML
    VBox resultDisplay2 = new VBox();

    private List<String> temp = new ArrayList<>();

    MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MediaHandler();
        searchWord();
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
        BackgroundTask.perform(() -> mediaPlayer = APIHandler.getAudio(label.getText()));
        wordTargetDisplay.setText(label.getText());
        setToDefault();
        searchWord();
    }

}
