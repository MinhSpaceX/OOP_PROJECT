package com.zeus.App.Controller;

import com.zeus.DatabaseManager.SQLite;
import com.zeus.utils.background.BackgroundTask;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.SystemManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController2 implements Initializable {

    @FXML
    private VBox AnswerContainer;

    @FXML
    private AnchorPane GameMenuCard;

    @FXML
    private Label questionDisplay;

    SQLite sql = null;

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
    private int timeRemaining = 30;
    private List<Pair<String, String>> list = new ArrayList<>();


    GameController gc = GameController.gameController;
    SceneContainer sc = SceneContainer.sceneContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sql = SystemManager.getManager(SQLite.class);
        list = sql.getRandomWords(1, 8);
        System.out.println(sql);
        backToGameMenu.setOnMouseClicked(e->{
            sc.changeView("/com/zeus/fxml/GameScene.fxml");
        });
        setGameMode();
    }

    public void setGameMode(){
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

    public void toClassicMode(){
        scoreDisplay.setText( "Score: " + Score);
        clock.setVisible(false);
        questIndex.setText(index + "/10");
        setUpAnsAndQues();
    }

    public void toInfinityMode(){
        scoreDisplay.setText( "Score: " + Score);
        clock.setVisible(false);
        questIndex.setVisible(false);
        setUpAnsAndQues();
    }

    public void toBlitzMode(){
        scoreDisplay.setText( "Score: " + Score);
        questIndex.setVisible(false);
        if(timeRemaining == 30){
            timeThread();
        }
        if(timeRemaining > 0) {
            setUpAnsAndQues();
        }
    }

    public void timeThread(){
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                clockCounter.setText(String.format("Time: %d",timeRemaining));

                if (timeRemaining <= 0) {
                    ((Timer) e.getSource()).stop();
                    ResultCard.setVisible(true);
                }
            }
        });
        timer.start();
    }

    public void setUpAnsAndQues(){
        if (list.size() <= 8) {
            Clock.timer(() -> BackgroundTask.perform(()->list.addAll(sql.getRandomWords(1, 40*4))));
        }
        for(int i = 0; i < 4; i++){
            switch (i){
                case 0:
                    Answer1.setText(list.get(i).getValue());
                    Answer1.setOnMouseClicked(e->{
                        checkCorrectness(0);
                    });
                case 1:
                    Answer2.setText(list.get(i).getValue());
                    Answer2.setOnMouseClicked(e->{
                        checkCorrectness(1);
                    });
                case 2:
                    Answer3.setText(list.get(i).getValue());
                    Answer3.setOnMouseClicked(e->{
                        checkCorrectness(2);
                    });
                case 3:
                    Answer4.setText(list.get(i).getValue());
                    Answer4.setOnMouseClicked(e->{
                        checkCorrectness(3);
                    });
            }
        }
        list.subList(0, 3).clear();
        Random ran = new Random();
        answer = ran.nextInt(4);
        questionDisplay.setText("What is the meaning of \"" + list.get(answer).getKey() + "\"");
    }

    public void checkCorrectness(int playerOption){
        if(playerOption == answer){
            Score++;
            if(gameMode.equals("Classic")){
                index++;
                toClassicMode();
            };
            if(gameMode.equals("Infinity")){
                toInfinityMode();
            }
            if(gameMode.equals("Blitz")){
                toBlitzMode();
            }
        }
        else {
            if(gameMode.equals("Classic")){
                index++;
                toClassicMode();
            };
            if(gameMode.equals("Infinity")){
                toInfinityMode();
            }
            if(gameMode.equals("Blitz")){
                toBlitzMode();
            }
        }
        if((index == 11 && gameMode.equals("Classic")) ||
                (timeRemaining == 0 && gameMode.equals("Blitz"))){
            ResultCard.setVisible(true);
            finalScore.setText(String.format("%d", Score));
            backToMenu.setOnMouseClicked(e->{
                sc.changeView("/com/zeus/fxml/GameScene.fxml");
                Logger.info("call");
            });
            continuePlay.setOnMouseClicked(e->{
                sc.changeView("/com/zeus/fxml/GameScene2.fxml");
            });
        }
    }

}
