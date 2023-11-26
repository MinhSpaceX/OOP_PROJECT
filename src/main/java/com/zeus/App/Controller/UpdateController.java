package com.zeus.App.Controller;

import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.DictionaryUtil.SingleWord;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.log.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * controller for the update scene
 */
public class UpdateController extends SearchController {
    private static Label updateLabel;
    private final List<SingleWord> singleWordList = new ArrayList<>();
    SQLite sql = null;
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
    @FXML
    private Button deleteButton;
    private boolean openMeaning = false;
    private boolean openExample = false;
    private String currentExample;
    private SingleWord oldSingleWord;

    /**
     * set the label that will deter which word is being displayed
     *
     * @param label index label
     */
    public static void SetUpdateLabel(Label label) {
        updateLabel = label;
    }

    /**
     * set up trie, sqlite and function in the scene
     * display word content in {@link #getSingleWord(Label)}
     */
    @Override
    public void initialize() {
        appTrie = SearchManager.userTrie;
        sql = SystemManager.getManager(SQLite.class);
        setFunction();
        getSingleWord(updateLabel);
    }

    /**
     * call to {@link #getSingleWord(Label)} to display word content
     *
     * @param label index label
     */
    @Override
    protected void displayWordFromLabel(Label label) {
        getSingleWord(label);
    }

    /**
     * get the single word info to display on the screen
     *
     * @param label index label
     */
    public void getSingleWord(Label label) {
        refreshInfo();
        boolean getFirst = true;
        //display word target
        wordTargetDisplay.setText(label.getText());
        Map<String, List<SingleWord>> result = sql.getWord(label.getText());
        // get all the single word meaning according to the type key
        for (var i : result.keySet()) {
            singleWordList.addAll(result.get(i));
        }
        //display the meaning, type and engExample and vieExample
        for (var i : singleWordList) {
            Label temp = new Label(i.getMeaning());
            if (getFirst) {
                getType.setText(i.getType());
                getMeaning.setText(i.getMeaning());
                getFirst = false;
                if (!i.getExamples().isEmpty()) {
                    displaySingleWord(i);
                }
                oldSingleWord = i;
            }
            temp.getStyleClass().add("label-style");
            meaningDisplay.getChildren().add(temp);
            // set example
            temp.setOnMouseClicked(e -> changeMeaningInfo(temp.getText()));
        }
        //display the pronoun
        getPronoun.setText(singleWordList.get(0).getPronoun());
    }

    /**
     * set function for other meaning and other example
     */
    public void setFunction() {
        anotherMeaning.getStyleClass().add("other-meaning-label");
        anotherEx.getStyleClass().add("other-meaning-label");
        anotherMeaning.setOnMouseClicked(e -> {
            Logger.info(Boolean.toString(openMeaning));
            if (!openMeaning) {
                meaningContainer.setVisible(true);
                openMeaning = true;
                exampleContainer.setVisible(false);
                openExample = false;
            } else {
                meaningContainer.setVisible(false);
                openMeaning = false;
            }
        });
        anotherEx.setOnMouseClicked(e -> {
            if (!openExample) {
                exampleContainer.setVisible(true);
                openExample = true;
                meaningContainer.setVisible(false);
                openMeaning = false;
            } else {
                exampleContainer.setVisible(false);
                openExample = false;
            }
        });
    }

    /**
     * change to the desire meaning
     *
     * @param meaning the meaning that user want to see
     */
    public void changeMeaningInfo(String meaning) {
        for (var i : singleWordList) {
            if (i.getMeaning().equals(meaning)) {
                displaySingleWord(i);
                return;
            }
        }
    }

    /**
     * display word content
     *
     * @param word word to display
     */
    public void displaySingleWord(SingleWord word) {
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
        if (!word.getExamples().isEmpty()) {
            boolean getFirst = true;
            for (var i : word.getExamples()) {
                Label temp = new Label(i.getKey());
                if (getFirst) {
                    getEngExample.setText(i.getKey());
                    getVieExample.setText(i.getValue());
                    currentExample = getEngExample.getText();
                    getFirst = false;
                }
                temp.getStyleClass().add("label-style");
                temp.setOnMouseClicked(e -> switchExample(i.getKey()));
                exampleDisplay.getChildren().add(temp);
            }
        }
    }

    /**
     * switch to the desired example
     *
     * @param engExample the english example will come with a translation in vietnamese
     */
    public void switchExample(String engExample) {
        currentExample = engExample;
        for (var i : oldSingleWord.getExamples()) {
            if (i.getKey().equals(engExample)) {
                getEngExample.setText(i.getKey());
                getVieExample.setText(i.getValue());
                exampleContainer.setVisible(false);
                openExample = false;
                return;
            }
        }
    }

    /**
     * this method will create a new example word for the word
     *
     * @param exampleKey deter the example that user want to change
     * @return Single word.
     */
    public SingleWord getNewSingleWord(String exampleKey) {
        Pair<String, String> newExamplePair = new Pair<>(getEngExample.getText(), getVieExample.getText());
        List<Pair<String, String>> newExampleList = oldSingleWord.getExamples() == null ? null : new ArrayList<>(oldSingleWord.getExamples());
        if (newExampleList != null) {
            for (int i = 0; i < newExampleList.size(); i++) {
                if (newExampleList.get(i).getKey().equals(exampleKey)) {
                    newExampleList.set(i, newExamplePair);
                }
            }
        }
        return new SingleWord(wordTargetDisplay.getText(), getPronoun.getText(),
                getType.getText(), getMeaning.getText(), newExampleList);
    }

    /**
     * update the word to the user's database when click
     *
     * @param event event handler
     */
    @FXML
    public void applyUserUpdate(ActionEvent event) {
        if (!getType.getText().isEmpty()) {
            SingleWord newSingleWord = getNewSingleWord(currentExample);
            sql.updateWord(oldSingleWord, newSingleWord);
            getSingleWord(new Label(newSingleWord.getWordTarget()));
        }
    }

    @FXML
    public void deleteWord(ActionEvent event){
        System.out.println(updateLabel.getText());
        sql.delete(updateLabel.getText().trim().toLowerCase());
        History.historyList.remove(updateLabel.getText());
        FavoriteController.FavoriteList.remove(updateLabel.getText());
        wordTargetDisplay.setText("The word has been deleted!");
        refreshInfo();
    }

    /**
     * refresh input box
     */
    public void refreshInfo() {
        getPronoun.clear();
        exampleDisplay.getChildren().clear();
        getEngExample.clear();
        getVieExample.clear();
        resultDisplay.setVisible(false);
        searchBar.clear();
        getType.clear();
        getMeaning.clear();
        meaningDisplay.getChildren().clear();
        meaningContainer.setVisible(false);
        singleWordList.clear();
    }

    /**
     * when clicked, change bakc to add scene
     *
     * @param event event handler
     */
    @FXML
    public void backToAddScene(ActionEvent event) {
        SceneContainer sc = SceneContainer.sceneContainer;
        sc.changeView(AddController.class);
    }

}
