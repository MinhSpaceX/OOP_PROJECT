package com.zeus.GameManager;

import com.zeus.DictionaryManager.Dictionary;
import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.DictionaryManager.Word;
import com.zeus.System.Initializer;
import com.zeus.utils.input.Input;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameManagement  {
    private final ArrayList<Word> dictionary;
    private final int[] wrongAnswer = new int[3]; // the index in the dictionary of wrong answer
    private int rightAnswer; // the index in the dictionary of right answer
    private boolean gameState = true;
    private char correct;

    public GameManagement(DictionaryManagement management) {
        dictionary = management.getDictionary().getDictionary();
    }

    public void StartGame() {
        randomAnswer();
        generateQuestion();
        while (gameState) {
            System.out.println("Enter your answer: [UPPER CASE]");
            String input = Input.getLine();
            char char_input = input.charAt(0);
            if (char_input == correct) {
                System.out.println("Correct!");
                gameState = false;
            } else {
                System.out.println("Wrong!");
            }
        }
    }


    public void generateQuestion() {
        int[] answers = randomAnswer();
        char answer_index = 'A';
        System.out.println("What is the meaning of: " + dictionary.get(rightAnswer).getWordTarget());
        for (int i = 0; i < 4; i++) {
            System.out.println(answer_index + "." + dictionary.get(answers[i]).getDescription().getTypes().get(0).getMeanings().get(0));
            if (answers[i] == rightAnswer) {
                correct = answer_index;
            }
            answer_index++;
        }
    }

    /**
     * @return an array that contain answers in random order
     */
    private int[] randomAnswer() {
        int index = 0; //use for array wrongAnswer to debug duplicating answer
        //int index_answers = 1; //use as the index for answers array
        int[] answers = new int[4];
        Random ran = new Random();

        //get random 4 answers to create question
        rightAnswer = ran.nextInt((dictionary.size() - 1) + 1);
        Set<Integer> usedIndexes = new HashSet<>();
        answers[0] = rightAnswer;
        usedIndexes.add(rightAnswer);
        for (int i = 1; i < 4; ) {
            wrongAnswer[index] = ran.nextInt((dictionary.size() - 1) + 1);
            if (wrongAnswer[index] != rightAnswer && !usedIndexes.contains(wrongAnswer[index])) {
                //debug duplicating answer
                //your code here:
                usedIndexes.add(wrongAnswer[index]);
                answers[i] = wrongAnswer[index];
                index++;
                i++;
            }
        }

        //shuffle the array to create random question format
        int length = 4;
        for (int i = length - 1; i > 0; i--) {
            int randomIndex = ran.nextInt(i + 1);

            int temp = answers[i];
            answers[i] = answers[randomIndex];
            answers[randomIndex] = temp;
        }

        return answers;
    }
}