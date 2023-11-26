package com.zeus.utils.controller;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Template Controller with default search bar
 * and a VBox to display results of autofill.
 */
public abstract class SearchController implements Initializable {
    @FXML
    protected TextField searchBar;
    @FXML
    protected VBox resultDisplay;
    protected List<String> autoFillList;
    protected Trie trie;

    /**
     * Initialize search bar and display field.
     *
     * @param url            URl
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultDisplay.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                int currentIndex = resultDisplay.getChildren().indexOf(resultDisplay.getScene().getFocusOwner());
                int nextIndex = (currentIndex + (event.getCode() == KeyCode.DOWN ? 1 : -1) + resultDisplay.getChildren().size()) % resultDisplay.getChildren().size();
                resultDisplay.getChildren().get(nextIndex).requestFocus();
                event.consume();
            }
        });
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && !resultDisplay.getChildren().isEmpty()) {
                resultDisplay.getChildren().get(0).requestFocus();
            }

            if (event.getCode() == KeyCode.ENTER && !autoFillList.isEmpty()) {
                try {
                    resultDisplay.setVisible(false);
                    searchBar.clear();
                    displayWordFromLabel((Label) resultDisplay.getChildren().get(0));
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                }
            }
        });
        searchBar.setOnKeyReleased(keyEvent -> {
            if (autoFillList != null) autoFillList.clear();
            resultDisplay.getChildren().clear();
            if (!searchBar.getText().isEmpty()) {
                resultDisplay.setVisible(true);
                displayAutoFill(searchBar.getText());
            } else {
                resultDisplay.setVisible(false);
            }
            if ((keyEvent.getTarget() instanceof TextField)) {
                searchBar.requestFocus();
            }
        });
        initialize();
    }

    /**
     * Display result of auto fill.
     *
     * @param input The word to auto fill.
     */
    protected void displayAutoFill(String input) {
        autoFillList = SystemManager.getManager(SearchManager.class).autoFill(input, trie).stream().distinct().collect(Collectors.toList());
        if (autoFillList.isEmpty()) {
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
            resultDisplay.getChildren().add(label);
            return;
        }
        for (var i : autoFillList) {
            Label label = new Label(i);
            label.getStyleClass().add("label-style");
            label.setOnMouseClicked(e -> {
                resultDisplay.setVisible(false);
                searchBar.clear();
                displayWordFromLabel(label);
            });
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    resultDisplay.setVisible(false);
                    searchBar.clear();
                    displayWordFromLabel(label);
                }
            });
            resultDisplay.getChildren().add(label);
        }
    }

    /**
     * Display the word informations from chosen word.
     *
     * @param label The lable represent the word chosen.
     */
    protected abstract void displayWordFromLabel(Label label);

    /**
     * Initialize needed variables or execute methods.
     */
    protected abstract void initialize();
}
