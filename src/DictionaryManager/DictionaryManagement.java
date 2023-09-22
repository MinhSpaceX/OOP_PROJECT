package DictionaryManager;

import java.util.Scanner;

public class DictionaryManagement {

    public DictionaryManagement(){};
    /****
     *
     */
    public Word insertFromCommandLine() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your input with foramt: word_target <enter> word_explain");
        String word_target = sc.nextLine();
        String word_explain = sc.nextLine();

        Word word = new Word(word_target, word_explain);

        return word;
    }
}
