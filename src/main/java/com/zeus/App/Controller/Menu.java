package com.zeus.App.Controller;

import com.zeus.App.SearchManager;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Menu implements Initializable {

    @FXML
    private TextField searchBar = new TextField();

    @FXML
    VBox resultDisplay = new VBox();

    boolean WordViewVisible = false;
    private  Parent root;
    private Stage stage;
    SearchManager sm = new SearchManager();
    private List<String> temp = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundTask.perform(this::loadData);
        searchWord();
        Logger.info("finish");
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                resultDisplay.getChildren().get(0).requestFocus();
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

    public void OpenWordCard(ActionEvent event){

    }

    public void loadData(){
        System.out.println("LOADING DATABASE");
        Clock.timer(() -> sm.loadDataFromBase());
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
        temp = sm.searchFilter(input).stream().distinct().collect(Collectors.toList());
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
            label.setOnMouseClicked(e -> ChangeToWordView(label));
            label.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) ChangeToWordView(label);
            });
            resultDisplay.getChildren().add(label);
        }
    }

    public void ChangeToWordView(Label label){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zeus/fxml/index.fxml"));

    }

}
