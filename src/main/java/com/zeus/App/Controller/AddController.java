package com.zeus.App.Controller;
import com.zeus.App.SearchManager;
import com.zeus.DatabaseManager.SQLite;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddController implements Initializable {
    @FXML
    private Button addWordButton;

    @FXML
    private TextArea getEngExample;

    @FXML
    private TextArea getMeaning;

    @FXML
    private TextField getPronoun;

    @FXML
    private TextField getType;

    @FXML
    private TextArea getVieExample;

    @FXML
    private TextField getWordTarget;

    @FXML
    private TextField searchBar;

    @FXML
    private VBox searchResultDisplay;

    private SQLite sql = null;
    private List<String> searchPane = new ArrayList<>();
    private Label UpdateLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchWord();
        sql = SystemManager.getManager(SQLite.class);
        if(!searchResultDisplay.getChildren().isEmpty()) {
            searchBar.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DOWN && !searchResultDisplay.getChildren().isEmpty()) {
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
        searchBar.setOnKeyReleased(keyEvent -> {
            searchPane.clear();
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
        searchPane = SearchManager.searchFilterUserDb(input).stream().distinct().collect(Collectors.toList());
        if(searchPane.isEmpty()){
            Label label = new Label("Maybe you want to add this word :D");
            label.getStyleClass().add("not-found-style-update");
            searchResultDisplay.getChildren().add(label);
            return;
        }
        if(searchPane.size() == 2){
            searchPane.remove(0);
        }
        for(var i : searchPane){
            Label label = new Label(i);
            label.getStyleClass().add("result-display-label-style");
            label.setOnMouseClicked(e -> {
                changeToUpdateView(label);
            });
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    changeToUpdateView(label);
                }
            });
            searchResultDisplay.getChildren().add(label);
        }
    }

    @FXML
    public void addWordToDB(ActionEvent event){
        if(!getWordTarget.getText().isEmpty()){
            String NewWordTarget = getWordTarget.getText();
            String NewPronoun = getPronoun.getText();
            String NewType = getType.getText();
            String NewMeaning = getMeaning.getText();
            Pair<String, String> NewExamplePair = new Pair<>(getEngExample.getText(), getVieExample.getText());
            List<Pair<String, String>> NewExample = new ArrayList<>();
            NewExample.add(NewExamplePair);
            SingleWord newSingleWord = new SingleWord(NewWordTarget, NewPronoun, NewType, NewMeaning, NewExample);
            sql.insert(newSingleWord);
        }
        else {
            return;
        }
        getWordTarget.clear();
        getPronoun.clear();
        getType.clear();
        getMeaning.clear();
        getEngExample.clear();
        getVieExample.clear();
    }

    public void changeToUpdateView(Label label){
        SceneContainer sc = SceneContainer.sceneContainer;
        UpdateController.SetUpdateLabel(label);
        sc.changeView("/com/zeus/fxml/UpdateScene.fxml");
    }

}
