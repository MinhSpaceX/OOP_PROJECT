package com.zeus.utils.trie;

import com.zeus.DictionaryManager.Word;
import com.zeus.utils.log.Logger;

import java.util.*;


public class Trie extends UtilTrie{
    public Trie() {
        super();
    }

    public Trie(List<Word> words) {
        super(words);
    }

    public boolean delete(String word) {
        if (isEmpty()) {
            Logger.warn("UtilTrie is empty.");
            return false;
        }
        if (delete(root, word, 0)) {
            Logger.info("Delete successfully.");
            return true;
        }
        return false;
    }

    public void print() {
        print(root, "");
    }

}
