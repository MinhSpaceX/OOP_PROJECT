package com.zeus.DictionaryManager;

import java.util.ArrayList;
import com.zeus.utils.log.Logger;

public class Dictionary {
    private final ArrayList<Word> dictionary = new ArrayList<>();
    private final DictionaryID dictId;

    /**
     * Constructor for dictionary.
     *
     * @param id Unique ID to each dictionary.
     */
    Dictionary(DictionaryID id) {
        dictId = id;
        Logger.info(String.format("Dictionary created. ID: %s.\n", dictId));
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
