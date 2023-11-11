package com.zeus.App.Controller;

import com.zeus.DatabaseManager.SQLite;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.managerfactory.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class addWord {
    @FXML
    Label status;
    @FXML
    TextField wordTarget;
    @FXML
    TextField pronoun;
    @FXML
    TextField type;
    @FXML
    TextField meaning;
    @FXML
    Button addButton;

    public void addWord(ActionEvent event) {
        String wordtarget = wordTarget.getText();
        String Pronoun = pronoun.getText();
        String Type = type.getText();
        String Meaning = meaning.getText();
        if (wordtarget.length() == 0 || Pronoun.length() == 0 || Type.length() == 0 || Meaning.length() == 0) {
            status.setText("ERORR!!");
        } else {
            status.setText("SUCCESSFUL");
            wordTarget.clear();
            pronoun.clear();
            type.clear();
            meaning.clear();
            SingleWord word = new SingleWord(wordtarget, Pronoun, Type, Meaning, null);
            SystemManager.getManager(SQLite.class).insert(word);
        }

    }


}
