package com.zeus.App.Controller;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.Database.SQLite;
import com.zeus.utils.DictionaryUtil.SingleWord;
import com.zeus.Managers.SystemApp.SystemManager;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchWord();
        sql = SystemManager.getManager(SQLite.class);
        if(!searchResultDisplay.getChildren().isEmpty()) {
            searchBar.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DOWN && !searchResultDisplay.getChildren().isEmpty()) {
                    searchResultDisplay.getChildren().get(0).requestFocus();
                }
                if(event.getCode() == KeyCode.ENTER && !searchResultDisplay.getChildren().isEmpty()){
                    changeToUpdateView((Label) searchResultDisplay.getChildren().get(0));
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
        for(var i : searchPane){
            Label label = new Label(i);
            label.getStyleClass().add("result-display-label-style");
            label.setOnMouseClicked(e -> changeToUpdateView(label));
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
        if(getWordTarget.getText().isEmpty()){
            return;
        }
        String NewWordTarget = getWordTarget.getText();
        SingleWord newSingleWord = getSingleWord(NewWordTarget);
        sql.insert(newSingleWord);
        getWordTarget.clear();
        getPronoun.clear();
        getType.clear();
        getMeaning.clear();
        getEngExample.clear();
        getVieExample.clear();
    }

    private SingleWord getSingleWord(String NewWordTarget) {
        String NewPronoun = getPronoun.getText();
        String NewType = getType.getText();
        String NewMeaning = getMeaning.getText();
        String engEx = getEngExample.getText().trim();
        String viEx = getVieExample.getText().trim();
        Pair<String, String> NewExamplePair = (engEx.isEmpty() || viEx.isEmpty()) ? null : new Pair<>(engEx, viEx);
        List<Pair<String, String>> NewExample = NewExamplePair == null ? null : new ArrayList<>();
        if (NewExample != null) NewExample.add(NewExamplePair);
        return new SingleWord(NewWordTarget, NewPronoun, NewType, NewMeaning, NewExample);
    }

    public void changeToUpdateView(Label label){
        SceneContainer sc = SceneContainer.sceneContainer;
        UpdateController.SetUpdateLabel(label);
        sc.changeView(UpdateController.class);
    }

}
