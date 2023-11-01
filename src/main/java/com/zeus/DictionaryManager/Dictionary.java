package com.zeus.DictionaryManager;

import java.util.ArrayList;
import com.zeus.utils.log.Logger;

public class Dictionary {
    private final ArrayList<Word> dictionary = new ArrayList<>();

    /**
     * Function to add a word into dictionary.
     *
     * @param word the word to add.
     */
    public void addWord(Word word) {
        dictionary.add(word);
    }

    /**
     * Function to get the dictionary.
     *
     * @return the dictionary.
     */
    public ArrayList<Word> getDictionary() {
        return dictionary;
    }
}
