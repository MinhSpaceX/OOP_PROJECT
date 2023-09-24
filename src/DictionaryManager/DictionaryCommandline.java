package DictionaryManager;

import java.util.ArrayList;

public class DictionaryCommandline {

    public DictionaryCommandline() {
    }

    /****
     * Function to print all contents of a dictionary.
     * @param dict dictionary to print out.
     */
    public void ShowAllWords(ArrayList<Word> dict) {
        System.out.println("No | English | Vietnamese");
        int a=1;
        for (Word w : dict) {
            System.out.format("%s | %s | %s\n", a, w.GetWordTarget(), w.GetWordExplain());
            a++;
        }
    }
}
