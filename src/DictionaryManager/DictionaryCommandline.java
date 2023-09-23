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
        for (Word w : dict) {
            System.out.format("| %s | %s", w.GetWordTarget(), w.GetWordExplain());
        }
    }
}
