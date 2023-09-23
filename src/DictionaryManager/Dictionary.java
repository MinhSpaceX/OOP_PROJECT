package DictionaryManager;

import java.util.ArrayList;

public class Dictionary {
    static private final ArrayList<Word> dictionary = new ArrayList<Word>();

    public Dictionary() {
    }

    /****
     * Function to add a word into dictionary.
     * @param word the word to add.
     */
    public static void addWord(Word word) {
        dictionary.add(word);
    }

    /***
     * Function to get the dictionary.
     * @return the dictionary.
     */
    public static ArrayList<Word> getDictionary() {
        return dictionary;
    }
}
