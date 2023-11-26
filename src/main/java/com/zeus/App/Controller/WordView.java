package com.zeus.App.Controller;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.DictionaryUtil.SingleWord;
import com.zeus.utils.api.APIHandler;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.controller.SearchController;
import com.zeus.utils.log.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

/**
 * this controller where word's content is displayed.
 */
public class WordView extends SearchController {
    private static Label menuLabel;
    @FXML
    Label pronounDisplay;
    @FXML
    Label userBelongLabel;
    @FXML
    FontAwesomeIconView addToFavorite;
    Map<String, List<SingleWord>> result;
    MediaPlayer mediaPlayer;
    @FXML
    private AnchorPane wordCard;
    @FXML
    private Text wordTargetDisplay;
    @FXML
    private Button audio;
    @FXML
    private HBox typeContainer;
    @FXML
    private Text meaningDisplay;
    private boolean isFavoriteWord = false;

    /**
     * set value for the menuLabel which is the host label for the word user
     * want to lookup
     *
     * @param label index label
     */
    public static void setMenuLabel(Label label) {
        menuLabel = label;
    }

    /**
     * get media, search function and liking function button
     */
    @Override
    protected void initialize() {
        appTrie = SearchManager.searchPath;
        userTrie = SearchManager.userTrie;
        MediaHandler();
        Platform.runLater(() -> displayLabelContent(menuLabel));
        checkIfLiked(menuLabel.getText());
        getLikingFunction();
    }

    /**
     * call to {@link #displayLabelContent(Label)} to display the word
     *
     * @param label index label
     */
    @Override
    protected void displayWordFromLabel(Label label) {
        displayLabelContent(label);
    }

    /**
     * get pronoun sound for word
     */
    public void MediaHandler() {
        audio.setOnMouseClicked(event -> {
            try {
                if (mediaPlayer == null) throw new NullPointerException("Media is null, change word or add data.");
                mediaPlayer.pause();
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
        });
    }

    /**
     * check weather user have liked this word or not
     *
     * @param target the word target to find in a list
     */
    public void checkIfLiked(String target) {
        if (FavoriteController.FavoriteList.contains(target)) {
            addToFavorite.setFill(Color.rgb(142, 143, 250));
            isFavoriteWord = true;
            return;
        }
        isFavoriteWord = false;
        addToFavorite.setFill(Color.rgb(225, 221, 221));
    }

    /**
     * add/undo the word to/from favorite list
     */
    public void getLikingFunction() {
        addToFavorite.setOnMouseClicked(e -> {
            if (!isFavoriteWord) {
                FavoriteController.FavoriteList.add(menuLabel.getText());
                addToFavorite.setFill(Color.rgb(142, 143, 250));
                isFavoriteWord = true;
            } else {
                FavoriteController.FavoriteList.remove(menuLabel.getText());
                addToFavorite.setFill(Color.rgb(225, 221, 221));
                isFavoriteWord = false;
            }
        });
    }

    /**
     * display content of the word through the label
     *
     * @param label index label
     */
    public void displayLabelContent(Label label) {
        menuLabel = label;
        typeContainer.setPrefWidth(1000);
        History.historyList.add(label.getText());
        typeContainer.getChildren().clear();
        checkIfLiked(label.getText());
        getLikingFunction();
        BackgroundTask.perform(() -> mediaPlayer = APIHandler.getAudio(label.getText()));
        wordTargetDisplay.setText(label.getText());
        result = SearchManager.getWordInstance(label.getText());
        userBelongLabel.setVisible(SystemManager.getManager(SearchManager.class).getUserTrie().search(label.getText()));
        boolean getFirst = true;
        for (var i : result.keySet()) {
            Label autoFillList = new Label(i);
            if (getFirst) {
                autoFillList.getStyleClass().add("tab-label");
                DisplayMeaning(autoFillList.getText());
                //getClickCss.put(autoFillList.getText(), true);
                getFirst = false;
            } else {
                autoFillList.getStyleClass().add("unchoose-tab-label");
                //getClickCss.put(autoFillList.getText(), false);
            }
            autoFillList.setOnMouseClicked(e -> DisplayMeaning(autoFillList.getText()));
            typeContainer.getChildren().add(autoFillList);
        }
        Platform.runLater(() -> {
            double width = 0;
            for (Node child : typeContainer.getChildren()) {
                if (child.isVisible()) {
                    width += child.getBoundsInParent().getWidth();
                }
            }
            typeContainer.setPrefWidth(width);
        });
    }

    /**
     * display the meaning of the word
     *
     * @param type each type will have a meaning, this param will
     *             identify which meaning is referred in order to display the meaning
     */
    public void DisplayMeaning(String type) {
        for (javafx.scene.Node node : typeContainer.getChildren()) {
            if (node instanceof Label) {
                Label autoFillList = (Label) node;
                if (autoFillList.getText().equals(type)) {
                    autoFillList.getStyleClass().clear();
                    autoFillList.getStyleClass().add("tab-label");
                } else {
                    if (autoFillList.getStyleClass().contains("tab-label")) {
                        autoFillList.getStyleClass().clear();
                        autoFillList.getStyleClass().add("unchoose-tab-label");
                    }
                }
            }
        }
        StringBuilder str = new StringBuilder();
        meaningDisplay.setText("");
        pronounDisplay.setText("");
        pronounDisplay.setText("/" + result.get(type).get(0).getPronoun() + "/");
        for (var i : result.get(type)) {
            str.append(i.toString());
        }
        meaningDisplay.setText(str.toString());
    }
}
