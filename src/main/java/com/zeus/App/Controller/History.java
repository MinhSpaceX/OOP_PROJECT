package com.zeus.App.Controller;

import com.zeus.Managers.ImageIcon.ImageIcon;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.log.Logger;
import com.zeus.utils.stackset.StackSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the History Scene.
 */
public class History implements Initializable {
    public static StackSet<String> historyList = new StackSet<>();
    private final SceneContainer sc = SceneContainer.sceneContainer;
    @FXML
    VBox historyDisplay = new VBox();
    @FXML
    Button remove;

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     *
     * @param url            points to the FXML file that corresponds to the controller class.
     * @param resourceBundle optional parameter.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayHistory();
    }

    /**
     * this method display all the words that user have searched.
     */
    public void displayHistory() {
        ArrayList<String> stringToRemove = new ArrayList<>();
        remove.setVisible(false);
        //create word card for each word in the history list.
        for (String i : historyList) {
            HBox hBox = new HBox(15);
            hBox.getStyleClass().add("hbox-style");
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("checkbox");
            hBox.getChildren().add(checkBox);
            Label label = new Label(i);
            label.getStyleClass().add("history");
            hBox.getChildren().add(label);//HERE
            Button button = new Button();
            try {
                ImageView imageView = new ImageView(SystemManager.getManager(ImageIcon.class).getImage("volume"));
                imageView.setFitWidth(80); // set width
                imageView.setFitHeight(20); // set height
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
            button.getStyleClass().add("audioHistory");
            hBox.getChildren().add(button);//HERE
            historyDisplay.getChildren().add(hBox);
            MediaHandler(button, label);
            hBox.setOnMouseClicked(e -> {
                WordView.setMenuLabel(label);
                sc.changeView(WordView.class);
            });
            ObservableList<HBox> itemsToRemove = FXCollections.observableArrayList();

            //get condition of the checkbox to know if the word is chosen or not
            for (var hbox : historyDisplay.getChildren()) {
                if (hbox instanceof HBox) {
                    HBox temp = (HBox) hbox;
                    ((CheckBox) temp.getChildren().get(0)).setOnAction(event -> {
                        // Check weather there are any selected checkbox
                        boolean atLeastOneSelected = false;
                        for (var hb : historyDisplay.getChildren()) {
                            if (hb instanceof HBox) {
                                HBox temp2 = (HBox) hb;
                                CheckBox cb = (CheckBox) temp2.getChildren().get(0);
                                if (cb.isSelected()) {
                                    atLeastOneSelected = true;
                                    break;
                                }
                            }
                        }
                        //add word that user want to remove into a list
                        if (((CheckBox) temp.getChildren().get(0)).isSelected()) {
                            itemsToRemove.add((HBox) hbox);
                            stringToRemove.add(((Label) temp.getChildren().get(1)).getText());
                        }
                        //the remove only appear when there are at least one word selected
                        remove.setVisible(atLeastOneSelected);
                    });
                }
            }
            //set function for remove button
            remove.setOnMouseClicked(event -> {
                historyList.removeAll(stringToRemove);
                historyDisplay.getChildren().removeAll(itemsToRemove);
                stringToRemove.clear();
                remove.setVisible(false);
            });

        }

        //historyDisplay.getChildren().addAll(listView);
    }

    /**
     * providing pronunciation sound when click on the media button
     *
     * @param button target the media button
     * @param label  get the pronunciation sound of the word corresponding to this label.
     */
    public void MediaHandler(Button button, Label label) {
        final MediaPlayer[] tempMedia = {null};
        BackgroundTask.perform(() -> tempMedia[0] = APIHandler.getAudio(label.getText()));
        button.setOnMouseClicked(event -> {
            try {
                if (tempMedia[0] == null) throw new NullPointerException("Media is null, change word or add data.");
                tempMedia[0].pause();
                tempMedia[0].seek(tempMedia[0].getStartTime());
                tempMedia[0].play();
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
        });
    }

}