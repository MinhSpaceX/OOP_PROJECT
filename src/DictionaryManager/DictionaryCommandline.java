package DictionaryManager;

import java.util.ArrayList;

public class DictionaryCommandline {
    void headMatch() {

    }

    /**
     * Function to print all contents of a dictionary.
     * @param dict dictionary to print out.
     */
    public void ShowAllWords(ArrayList<Word> dict) {
        for (Word w : dict) {
            System.out.format("| %s | %s", w.GetWordTarget(), w.GetWordExplain());
        }
    }

    /**
     * Function to search for words start with the key value.
     * @param key : value to pass in.
     * @return 0: No word found.
     * <p>
     * 1: One or many words found.
     */
    public static int dictionarySearcher(String key) {
        
        return 0;
    }
}
