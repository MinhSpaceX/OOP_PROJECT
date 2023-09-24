package DictionaryManager;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> dictionary = new ArrayList<Word>();
    private DictionaryID keyId;
    /**
     * Constructor for dictionary.
     * @param id Unique ID to each dictionary.
     */
    Dictionary(DictionaryID id) {
        keyId = id;
    }

    /**
     * Function to add a word into dictionary.
     * @param word the word to add.
     */
    void addWord(Word word) {
        dictionary.add(word);
    }

    /**
     * Get a dictionary ID.
     * @return unique ID.
     */
    DictionaryID getId() {
        return keyId;
    }

    /**
     * Function to get the dictionary.
     * @return the dictionary.
     */
    ArrayList<Word> getDictionary() {
        return dictionary;
    }
}
