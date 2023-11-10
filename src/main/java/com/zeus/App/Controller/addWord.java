package com.zeus.App.Controller;

import com.zeus.DictionaryManager.Word;
import com.zeus.utils.file.FileManager;
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
            Word.Description.Type.Meaning.Example example = new Word.Description.Type.Meaning.Example();
            example.setExample("", "");
            List<Word.Description.Type.Meaning.Example> exampleList = new ArrayList<>();
            Word.Description.Type.Meaning meaning1 = new Word.Description.Type.Meaning();
            meaning1.setMeaning(Meaning, exampleList);
            List<Word.Description.Type.Meaning> meaningList = new ArrayList<>();
            meaningList.add(meaning1);
            Word.Description.Type type1 = new Word.Description.Type();
            type1.setType(Type, meaningList);
            List<Word.Description.Type> typeList = new ArrayList<>();
            typeList.add(type1);
            Word.Description description = new Word.Description();
            description.setDescription(Pronoun, typeList);
            Word word = new Word(wordtarget, description);
            FileManager.dictionaryExportToFile(word);
        }

    }


}
