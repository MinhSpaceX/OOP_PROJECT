package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.DatabaseManager.SQLite;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UpdateController implements Initializable {
    @FXML
    private Button addWordButton;

    @FXML
    private Label anotherEx;

    @FXML
    private TextField getEnglishExample;

    @FXML
    private TextField getMEaning;

    @FXML
    private TextField getPronoun;

    @FXML
    private TextField getType;

    @FXML
    private TextField getVieExample;

    @FXML
    private Label pronounDisplay;

    @FXML
    private Label pronounDisplay2;

    @FXML
    private Label pronounDisplay21;

    @FXML
    private Label pronounDisplay211;

    @FXML
    private TextField searchBar;

    @FXML
    private Label wordTargetDisplay;

    @FXML
    private Label anotherType;
    @FXML
    private VBox searchResultDisplay;
    private List<String> temp = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchWord();
        if(!searchResultDisplay.getChildren().isEmpty()) {
            searchBar.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DOWN) {
                    searchResultDisplay.getChildren().get(0).requestFocus();
                }
            });
            searchResultDisplay.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                    int currentIndex = searchResultDisplay.getChildren().indexOf(searchResultDisplay.getScene().getFocusOwner());
                    int nextIndex = (currentIndex + (event.getCode() == KeyCode.DOWN ? 1 : -1) + searchResultDisplay.getChildren().size()) % searchResultDisplay.getChildren().size();
                    searchResultDisplay.getChildren().get(nextIndex).requestFocus();
                    event.consume();
                }
            });
        }
    }

    public void searchWord(){
        searchResultDisplay.setVisible(false);
        Logger.info("called");
        searchBar.setOnKeyReleased(keyEvent -> {
            temp.clear();
            searchResultDisplay.getChildren().clear();
            if (!searchBar.getText().isEmpty()) {
                searchResultDisplay.setVisible(true);
                String input = searchBar.getText();
                filterData(input);
            } else {
                searchResultDisplay.setVisible(false);
            }
            if((keyEvent.getTarget() instanceof TextField)){
                searchBar.requestFocus();
            }
        });
    }

    private void filterData(String input){
        temp = SearchManager.searchFilter(input).stream().distinct().collect(Collectors.toList());
        if(temp.isEmpty()){
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
            searchResultDisplay.getChildren().add(label);
            return;
        }
        if(temp.size() == 2){
            temp.remove(0);
        }
        for(var i : temp){
            Label label = new Label(i);
            label.getStyleClass().add("result-display-label-style");
            label.setOnMouseClicked(e -> {
                setOldSingleWord();
            });
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    setOldSingleWord();
                }
            });
            searchResultDisplay.getChildren().add(label);
        }
    }

    public void setOldSingleWord(){

    }
}
