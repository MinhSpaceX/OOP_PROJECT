package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.zeus.utils.file.FileManager.getPathFromFile;

public class Menu implements Initializable {

    @FXML
    private TextField searchBar = new TextField();

    @FXML
    VBox resultDisplay = new VBox();

    boolean WordViewVisible = false;
    private  Parent root;
    private Stage stage;
    private List<String> temp = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchWord();
        Logger.info("Menu init -----------------");
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && !resultDisplay.getChildren().isEmpty()) {
                resultDisplay.getChildren().get(0).requestFocus();
            }
            if(event.getCode() == KeyCode.ENTER && !resultDisplay.getChildren().isEmpty()){
                try {
                    ChangeToWordView((Label) resultDisplay.getChildren().get(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        resultDisplay.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                int currentIndex = resultDisplay.getChildren().indexOf(resultDisplay.getScene().getFocusOwner());
                int nextIndex = (currentIndex + (event.getCode() == KeyCode.DOWN ? 1 : -1) + resultDisplay.getChildren().size()) % resultDisplay.getChildren().size();
                resultDisplay.getChildren().get(nextIndex).requestFocus();
                event.consume();
            }
        });
    }

    public void searchWord(){
        resultDisplay.setVisible(false);
        Logger.info("called");
        if(WordViewVisible){
            resultDisplay.setLayoutY(64);
        }
        else{
            resultDisplay.setLayoutY(208);
        }
        searchBar.setOnKeyReleased(keyEvent -> {
            temp.clear();
            resultDisplay.getChildren().clear();
            if (!searchBar.getText().isEmpty()) {
                resultDisplay.setVisible(true);
                String input = searchBar.getText();
                filterData(input);
            } else {
                resultDisplay.setVisible(false);
            }
            if((keyEvent.getTarget() instanceof TextField)){
                searchBar.requestFocus();
            }
        });
    }

    public void setToDefault(){
        searchBar.clear();
    }

    private void filterData(String input){
        temp = SearchManager.searchFilter(input).stream().distinct().collect(Collectors.toList());
        if(temp.isEmpty()){
            Label label = new Label("Hmm...what word is this?");
            label.getStyleClass().add("not-found-style");
            resultDisplay.getChildren().add(label);
            return;
        }
        if(temp.size() == 2){
            temp.remove(0);
        }
        for(var i : temp){
            Label label = new Label(i);
            label.getStyleClass().add("label-style");
            label.setOnMouseClicked(e -> {
                try {
                    ChangeToWordView(label);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        ChangeToWordView(label);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            resultDisplay.getChildren().add(label);
        }
    }

    public void ChangeToWordView(Label label) throws IOException {
        SceneContainer sc = SceneContainer.sceneContainer;
        WordView.setMenuLabel(label);
        sc.changeView("/com/zeus/fxml/WordView.fxml");
    }

}
