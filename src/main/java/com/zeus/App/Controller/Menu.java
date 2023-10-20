package com.zeus.App.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.DictionaryManager.Word;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.*;

public class Menu {

    @FXML
    public TextField getWordTarget;
    @FXML
    public TextField getWordExplain;
    @FXML
    public TextField getWordType;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button refresher;
    @FXML
    public TableView<Word> table;
    @FXML
    public TableColumn<Word, String> word_targetDisplay;
    @FXML
    public TableColumn<Word, String> word_explainDisplay;
    @FXML
    public TableColumn<Word, String> word_typeDisplay;
    @FXML
    public TextField searchInput;
    @FXML
    public Button search;

    private String word_target;
    private String word_explain;
    private String word_type;
    public MongoPanel mp = new MongoPanel();
    private ObservableList<Word> dictionary = FXCollections.observableArrayList();

    public void getNewWord(ActionEvent actionEvent) throws JsonProcessingException {
        word_target = getWordTarget.getText();
        word_explain = getWordExplain.getText();
        word_type = getWordType.getText();

        mp.addDocument(word_target, word_explain, word_type);
        clearBox();
        System.out.println("Add successfully!");
    }

    public void deleteWord(ActionEvent actionEvent){
        word_target = getWordTarget.getText();
        mp.deleteDocument(word_target);
        System.out.println("Delete successfully!");
        clearBox();
    }

    public void displayData(ActionEvent actionEvent){
        refresh();
        word_targetDisplay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWordTarget()));
        word_explainDisplay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWordExplain()));
        word_typeDisplay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWordType()));
        table.setItems(dictionary);
    }

    public void findWord(ActionEvent actionEvent) throws JsonProcessingException {
        String searchPattern = searchInput.getText();
        refresh();
        mp.findWord(searchPattern);
    }

    private void refresh(){
        dictionary.clear();
        dictionary = mp.FetchingData();
    }

    private void clearBox(){
        getWordTarget.clear();
        getWordExplain.clear();
        getWordType.clear();
    }
}
