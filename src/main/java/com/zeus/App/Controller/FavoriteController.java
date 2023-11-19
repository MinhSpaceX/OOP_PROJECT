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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FavoriteController implements Initializable {

    @FXML
    private VBox FavoriteDisplay;

    @FXML
    private Button remove;
    public static StackSet<String> FavoriteList = new StackSet<>();

    SceneContainer sc = SceneContainer.sceneContainer;

    //ListView<HBox> listView = new ListView<>();
    //ObservableList<HBox> items = FXCollections.observableArrayList();


    public void displayFavorite() {
        ArrayList<String> stringToRemove = new ArrayList<>();
        //listView.setItems(items);
        //listView.getStyleClass().add("listview-style");
        remove.setVisible(false);
        for(String i : FavoriteList) {
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
            FavoriteDisplay.getChildren().add(hBox);
            //items.add(hBox);
            //listView.refresh();
            MediaHandler(button, label);
            hBox.setOnMouseClicked(e -> {
                WordView.setMenuLabel(label);
                sc.changeView("/com/zeus/fxml/WordView.fxml");
            });
            ObservableList<HBox> itemsToRemove = FXCollections.observableArrayList();

            for (var hbox : FavoriteDisplay.getChildren()) {
                if (hbox instanceof HBox) {
                    HBox temp = (HBox) hbox;
                    ((CheckBox) temp.getChildren().get(0)).setOnAction(event -> {
                        // Kiểm tra xem có ít nhất một CheckBox được chọn hay không
                        boolean atLeastOneSelected = false;
                        for (var hb : FavoriteDisplay.getChildren()) {
                            if (hb instanceof HBox) {
                                HBox temp2 = (HBox) hb;
                                CheckBox cb = (CheckBox) temp2.getChildren().get(0);
                                if (cb.isSelected()) {
                                    atLeastOneSelected = true;
                                    break;
                                }
                            }
                        }

                        if (((CheckBox) temp.getChildren().get(0)).isSelected()) {
                            itemsToRemove.add((HBox) hbox);
                            stringToRemove.add(((Label) temp.getChildren().get(1)).getText());
                        }

                        remove.setVisible(atLeastOneSelected);
                    });
                }
            }

            remove.setOnMouseClicked(event -> {
                FavoriteList.removeAll(stringToRemove);
                FavoriteDisplay.getChildren().removeAll(itemsToRemove);
                stringToRemove.clear();
                //items.removeAll(itemsToRemove);
                //listView.refresh();
                remove.setVisible(false);
            });

        }

        //FavoriteDisplay.getChildren().addAll(listView);
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
        displayFavorite();
    }
}
