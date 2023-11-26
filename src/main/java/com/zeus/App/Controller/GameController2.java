package com.zeus.App.Controller;

import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.clock.Clock;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller of the game scene
 */
public class GameController2 implements Initializable {

    SQLite sql = null;
    GameController gc = GameController.gameController;
    SceneContainer sc = SceneContainer.sceneContainer;
    @FXML
    private VBox AnswerContainer;
    @FXML
    private AnchorPane GameMenuCard;
    @FXML
    private Label questionDisplay;
    @FXML
    private FontAwesomeIconView backToGameMenu;
    @FXML
    private Text scoreDisplay;
    @FXML
    private Label Answer1;
    @FXML
    private Label Answer2;
    @FXML
    private Label Answer3;
    @FXML
    private Label Answer4;
    @FXML
    private Label questIndex;
    private int Score = 0;
    private int answer = 0;
    private int index = 1;
    private String gameMode;
    @FXML
    private Button backToMenu;
    @FXML
    private Button continuePlay;
    @FXML
    private Text finalScore;
    @FXML
    private AnchorPane ResultCard;
    @FXML
    private Text clockCounter;
    @FXML
    private Label clock;
    private double timeRemaining = 30;
    private List<Pair<String, String>> list = new ArrayList<>();

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     *
     * @param url            points to the FXML file that corresponds to the controller class.
     * @param resourceBundle optional parameter.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sql = SystemManager.getManager(SQLite.class);
        list = sql.getRandomWords(1, 4 * 8);
        BackgroundTask.perform(() -> list.addAll(sql.getRandomWords(1, 100 * 4)));
        backToGameMenu.setOnMouseClicked(e -> sc.changeView(GameController.class));
        setGameMode();
    }

    /**
     * set the suitable scene for each game mode
     */
    public void setGameMode() {
        gameMode = gc.getGameMode();
        //Logger.info(gameMode);
        switch (gameMode) {
            case "Classic":
                toClassicMode();
                break;
            case "Infinity":
                toInfinityMode();
                break;
            case "Blitz":
                toBlitzMode();
                break;
        }
    }

    /**
     * provide components for classic mode
     * {@link #setUpAnsAndQues()} get new question
     */
    public void toClassicMode() {
        scoreDisplay.setText("Score: " + Score);
        clock.setVisible(false);
        questIndex.setText(index + "/10");
        setUpAnsAndQues();
    }

    /**
     * provide components for infinity mode
     */
    public void toInfinityMode() {
        scoreDisplay.setText("Score: " + Score);
        clock.setVisible(false);
        questIndex.setVisible(false);
        setUpAnsAndQues();
    }

    /**
     * provide components for blitz mode
     * {@link #timeThread()} clock in blitz mode
     */
    public void toBlitzMode() {
        scoreDisplay.setText("Score: " + Score);
        questIndex.setVisible(false);
        if (timeRemaining == 30) {
            timeThread();
        }
        if (timeRemaining > 0) {
            setUpAnsAndQues();
        }
    }

    /**
     * time counter of the blitz mode with 30 second interval
     */
    public void timeThread() {
        BackgroundTask.perform(() -> {
            timeRemaining = 29.99999999f;
            Clock.Tick();
            Clock.Tock();
            double elapse = Clock.elapse() / 1000000000.0f;
            while (elapse <= 30) {
                Clock.Tock();
                elapse = Clock.elapse() / 1000000000.0f;
                if (elapse >= 0.1) {
                    Clock.Tick();
                    if (timeRemaining < 0) break;
                    timeRemaining -= elapse;
                    clockCounter.setText(String.format("Time: %d", (int) timeRemaining));
                }

            }
            timeRemaining = 30;
            openEndScene();

        });
    }

    /**
     * generate answers and questions
     */
    public void setUpAnsAndQues() {
        //generate 400 question samples in other thread
        if (list.size() <= 4 * 10 && !gameMode.equals("Classic")) {
            BackgroundTask.perform(() -> list.addAll(sql.getRandomWords(1, 100 * 4)));
        }

        //adding data to the answer box
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    Answer1.setText(list.get(i).getValue());
                    Answer1.setOnMouseClicked(e -> checkCorrectness(0));
                case 1:
                    Answer2.setText(list.get(i).getValue());
                    Answer2.setOnMouseClicked(e -> checkCorrectness(1));
                case 2:
                    Answer3.setText(list.get(i).getValue());
                    Answer3.setOnMouseClicked(e -> checkCorrectness(2));
                case 3:
                    Answer4.setText(list.get(i).getValue());
                    Answer4.setOnMouseClicked(e -> checkCorrectness(3));
            }
        }
        list.subList(0, 3).clear();
        //random the questions
        Random ran = new Random();
        answer = ran.nextInt(4);
        questionDisplay.setText("What is the meaning of \"" + list.get(answer).getKey() + "\"");
    }

    /**
     * check if the player have chosen the correct answer
     *
     * @param playerOption get the player option
     *                     {@link #toClassicMode()} continue in classic mode
     *                     {@link #toInfinityMode()} continue in infinity mode
     *                     {@link #toBlitzMode()} continue in blitz mode
     */
    public void checkCorrectness(int playerOption) {
        if (playerOption == answer) {
            Score++;
        }
        switch (gameMode) {
            case "Classic":
                index++;
                toClassicMode();
                if (index == 11) {
                    openEndScene();
                }
                break;
            case "Infinity":
                toInfinityMode();
                break;
            case "Blitz":
                toBlitzMode();
                break;
        }
    }

    /**
     * method called once the game end.
     */
    public void openEndScene() {
        ResultCard.setVisible(true);
        finalScore.setText(String.format("%d", Score));
        //set function for two buttons in the ending scene
        backToMenu.setOnMouseClicked(e -> sc.changeView(GameController.class));
        continuePlay.setOnMouseClicked(e -> sc.changeView(GameController2.class));
    }

}
