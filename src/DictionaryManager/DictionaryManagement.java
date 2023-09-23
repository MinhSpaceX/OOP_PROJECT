package DictionaryManager;

import Input.Input;

import java.util.Scanner;

public class DictionaryManagement {
    static Scanner sc = Input.getScanner();

    public DictionaryManagement() {
    }

    /***
     * Function to get input from terminal.
     * @return inserted word.
     */
    public static Word insertFromCommandLine() {
        System.out.println("Enter your input with format: word_target <enter> word_explain");
        String word_target = sc.nextLine();
        String word_explain = sc.nextLine();

        Word word = new Word(word_target, word_explain);

        return word;
    }
}
