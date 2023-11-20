package com.zeus.utils.controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import com.zeus.utils.trie.Trie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public abstract class SearchController implements Initializable {
    @FXML
    protected TextField searchBar;

    @FXML
    protected VBox resultDisplay;
    
    protected List<String> autoFillList;

    protected static String input;

    protected Trie trie;

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

            if(event.getCode() == KeyCode.ENTER && !autoFillList.isEmpty()){
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
            if((keyEvent.getTarget() instanceof TextField)){
                searchBar.requestFocus();
            }
        });
        initialize();
    }

    protected void displayAutoFill(String input) {
        autoFillList = SystemManager.getManager(SearchManager.class).autoFill(input, trie).stream().distinct().collect(Collectors.toList());
        if(autoFillList.isEmpty()){
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
            resultDisplay.getChildren().add(label);
            return;
        }
        if(autoFillList.size() == 2){
            autoFillList.remove(0);
        }
        for(var i : autoFillList){
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

    protected abstract void displayWordFromLabel(Label label);
    
    protected abstract void initialize();
}
