package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.DatabaseManager.SQLite;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UpdateController implements Initializable {
    @FXML
    private Button addWordButton;

    @FXML
    private Label anotherEx;

    @FXML
    private Label anotherMeaning;

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
    private TextField searchBar;

    @FXML
    private VBox searchResultDisplay;

    @FXML
    private Button updateButton;

    @FXML
    private Label wordTargetDisplay;

    @FXML
    private VBox meaningDisplay;
    @FXML
    private ScrollPane meaningContainer;
    @FXML
    private ScrollPane exampleContainer;
    @FXML
    private VBox exampleDisplay;

    private List<String> searchPane = new ArrayList<>();
    private Map<String, List<SingleWord>> result;
    private List<SingleWord> singleWordList = new ArrayList<>();
    private boolean openMeaning = false;
    private boolean openExample = false;
    private String currentExample;
    private SingleWord oldSingleWord;
    private static Label updateLabel;
    SQLite sql = null;

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
                    getSingleWord((Label) searchResultDisplay.getChildren().get(0));
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
        setFunction();
        getSingleWord(updateLabel);
    }

    public static void SetUpdateLabel(Label label){
        updateLabel = label;
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
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
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
                getSingleWord(label);
            });
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    getSingleWord(label);
                }
            });
            searchResultDisplay.getChildren().add(label);
        }
    }

    public void getSingleWord(Label label){
        refreshInfo();
        boolean getFirst = true;
        //display word target
        wordTargetDisplay.setText(label.getText());
        result = sql.getWord(label.getText());
        // get all the single word meaning according to the type key
        for(var i : result.keySet()){
            singleWordList.addAll(result.get(i));
        }
        //display the meaning, type and engExample and vieExample
        for(var i : singleWordList){
            Label temp = new Label(i.getMeaning());
            if(getFirst){
                getType.setText(i.getType());
                getMeaning.setText(i.getMeaning());
                getFirst = false;
                if(!i.getExamples().isEmpty()){
                    displaySingleWord(i);
                }
                oldSingleWord = i;
            }
            temp.getStyleClass().add("label-style");
            meaningDisplay.getChildren().add(temp);
            // set example
            temp.setOnMouseClicked(e->{
                changeMeaningInfo(temp.getText());
            });
        }
        //display the pronoun
        getPronoun.setText(singleWordList.get(0).getPronoun());
    }

    public void setFunction(){
        anotherMeaning.getStyleClass().add("other-meaning-label");
        anotherEx.getStyleClass().add("other-meaning-label");
        anotherMeaning.setOnMouseClicked(e->{
            Logger.info(Boolean.toString(openMeaning));
            if(!openMeaning)
            {
                meaningContainer.setVisible(true);
                openMeaning = true;
                exampleContainer.setVisible(false);
                openExample = false;
            }
            else{
                meaningContainer.setVisible(false);
                openMeaning = false;
            }
        });
        anotherEx.setOnMouseClicked(e->{
            if(!openExample){
                exampleContainer.setVisible(true);
                openExample = true;
                meaningContainer.setVisible(false);
                openMeaning = false;
            }
            else {
                exampleContainer.setVisible(false);
                openExample = false;
            }
        });
    }

    public void changeMeaningInfo(String meaning){
        for(var i : singleWordList){
            if(i.getMeaning().equals(meaning)){
                displaySingleWord(i);
                return;
            }
        }
    }

    public void displaySingleWord(SingleWord word){
        oldSingleWord = word;
        meaningContainer.setVisible(false);
        openMeaning = false;
        getVieExample.clear();
        getEngExample.clear();
        getType.setText(word.getType());
        getMeaning.setText(word.getMeaning());
        getEngExample.clear();
        getVieExample.clear();
        exampleDisplay.getChildren().clear();
        if(!word.getExamples().isEmpty()){
            boolean getFirst = true;
            for(var i : word.getExamples()){
                Label temp = new Label(i.getKey());
                if(getFirst){
                    getEngExample.setText(i.getKey());
                    getVieExample.setText(i.getValue());
                    currentExample = getEngExample.getText();
                    getFirst = false;
                }
                temp.getStyleClass().add("label-style");
                temp.setOnMouseClicked(e->{
                    switchExample(i.getKey());
                });
                exampleDisplay.getChildren().add(temp);
            }
        }
    }

    public void switchExample(String engExample){
        currentExample = engExample;
        for(var i : oldSingleWord.getExamples()){
            if(i.getKey().equals(engExample)){
                getEngExample.setText(i.getKey());
                getVieExample.setText(i.getValue());
                exampleContainer.setVisible(false);
                openExample = false;
                return;
            }
        }
    }

    public SingleWord getNewSingleWord(String exampleKey){
        Pair<String, String> newExamplePair = new Pair<>(getEngExample.getText(), getVieExample.getText());
        List<Pair<String, String>> newExampleList = oldSingleWord.getExamples() == null ? null : new ArrayList<>(oldSingleWord.getExamples());
        for(int i = 0; i < newExampleList.size(); i++){
            if(newExampleList.get(i).getKey().equals(exampleKey)){
                newExampleList.set(i, newExamplePair);
            }
        }
        System.out.println(newExampleList);
        return new SingleWord(wordTargetDisplay.getText(), getPronoun.getText(),
                getType.getText(), getMeaning.getText(), newExampleList);
    }

    @FXML
    public void applyUserUpdate(ActionEvent event){
        if(!getType.getText().isEmpty()) {
            SingleWord newSingleWord = getNewSingleWord(currentExample);
            sql.updateWord(oldSingleWord, newSingleWord);
            getSingleWord(new Label(newSingleWord.getWordTarget()));
        }
    }

    public void refreshInfo() {
        getPronoun.clear();
        exampleDisplay.getChildren().clear();
        getEngExample.clear();
        getVieExample.clear();
        searchResultDisplay.setVisible(false);
        searchBar.clear();
        getType.clear();
        getMeaning.clear();
        meaningDisplay.getChildren().clear();
        meaningContainer.setVisible(false);
        singleWordList.clear();
    }

    @FXML
    public void backToAddScene(ActionEvent event){
        SceneContainer sc = SceneContainer.sceneContainer;
        sc.changeView("/com/zeus/fxml/UpdateLobby.fxml");
    }

}
