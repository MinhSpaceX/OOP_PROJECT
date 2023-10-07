package DictionaryManager;

import java.util.ArrayList;

public class Dictionary {
    private final ArrayList<Word> dictionary = new ArrayList<Word>();
    private final DictionaryID dictId;

    /**
     * Constructor for dictionary.
     *
     * @param id Unique ID to each dictionary.
     */
    Dictionary(DictionaryID id) {
        dictId = id;
        System.out.printf("Dictionary created. ID: %s.\n", id.toString());
    }

    /**
     * Function to add a word into dictionary.
     *
     * @param word the word to add.
     */
    void addWord(Word word) {
        dictionary.add(word);
    }

    /**
     * Get a dictionary ID.
     *
     * @return unique ID.
     */
    DictionaryID getId() {
        return dictId;
    }

    /**
     * Function to get the dictionary.
     *
     * @return the dictionary.
     */
    ArrayList<Word> getDictionary() {
        return dictionary;
    }
}
