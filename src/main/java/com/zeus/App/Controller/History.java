package com.zeus.App.Controller;

import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.stackset.StackSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

public class History implements Initializable {
    @FXML
    VBox historyDisplay = new VBox();

    @FXML
    Button remove;
    public static StackSet<String> historyList = new StackSet<>();

    ListView<HBox> listView = new ListView<>();
    ObservableList<HBox> items = FXCollections.observableArrayList();


    public void displayHistory() {
        listView.setItems(items);
        listView.getStyleClass().add("scroll-pane-wordview");
        remove.setVisible(false);
        for(String i : historyList){
            HBox hBox = new HBox(15);
            hBox.getStyleClass().add("hbox");
            CheckBox checkBox = new CheckBox();
            checkBox.getStyleClass().add("checkbox");
            hBox.getChildren().add(checkBox);
            Label label = new Label(i);
            label.getStyleClass().add("history");
            hBox.getChildren().add(label);//HERE
            Button button = new Button();
            try {
                ImageView imageView = new ImageView(FileManager.loadImage("/com/zeus/icon/volume-high-solid.png"));
                imageView.setFitWidth(80); // Đặt chiều rộng
                imageView.setFitHeight(20); // Đặt chiều cao
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);
            } catch (FileNotFoundException e) {
                Logger.error(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                Logger.error(e.getMessage());
            }
            button.getStyleClass().add("audioHistory");
            hBox.getChildren().add(button);//HERE
            items.add(hBox);
            listView.refresh();
            MediaHandler(button, label);
            label.setOnMouseClicked(e -> {
                SceneContainer sc = SceneContainer.sceneContainer;
                WordView.setMenuLabel(label);
                sc.changeView("/com/zeus/fxml/WordView.fxml");
            });
            ObservableList<HBox> itemsToRemove = FXCollections.observableArrayList();

            for (HBox hbox : items) {
                ((CheckBox) hbox.getChildren().get(0)).setOnAction(event -> {
                    // Kiểm tra xem có ít nhất một CheckBox được chọn hay không
                    boolean atLeastOneSelected = false;
                    for (HBox hb : items) {
                        CheckBox cb = (CheckBox) hb.getChildren().get(0);
                        if (cb.isSelected()) {
                            atLeastOneSelected = true;
                            break;
                        }
                    }

                    if (((CheckBox)hbox.getChildren().get(0)).isSelected()) {
                        itemsToRemove.add(hbox);
                        historyList.remove(((Label)hbox.getChildren().get(1)).getText());
                    }

                    remove.setVisible(atLeastOneSelected);
                });
            }

            remove.setOnMouseClicked(event-> {

                items.removeAll(itemsToRemove);

                listView.refresh();
                remove.setVisible(false);
            });

        }

        historyDisplay.getChildren().addAll(listView);
    }

    public void MediaHandler(Button button, Label label){
        final MediaPlayer[] tempMedia = {null};
        BackgroundTask.perform(()-> {
            tempMedia[0] = APIHandler.getAudio(label.getText());
        });
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        displayHistory();
    }
}