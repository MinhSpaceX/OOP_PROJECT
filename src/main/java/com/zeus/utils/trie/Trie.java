package com.zeus.utils.trie;

import com.zeus.utils.DictionaryUtil.Word;
import com.zeus.utils.log.Logger;

import java.util.List;

/**
 * Class to increase readability of {@link UtilTrie}, similar to a tree structure.
 */
public class Trie extends UtilTrie {
    /**
     * Constructor.
     */
    public Trie() {
        super();
    }

    /**
     * Constructor with list of word, see {@link UtilTrie#UtilTrie(List)}.
     *
     * @param words List of {@link Word}.
     */
    public Trie(List<Word> words) {
        super(words);
    }

    /**
     * Delete a word from the trie.
     *
     * @param word Word target.
     * @return true if deleted.
     * <p>false if not.</p>
     */
    public boolean delete(String word) {
        if (isEmpty()) {
            Logger.warn("UtilTrie is empty.");
            return false;
        }
        delete(root, word.toLowerCase(), 0);
        if (!search(word)) {
            Logger.info("Delete successfully.");
            return true;
        }
        return false;
    }

    /**
     * Print all contents of a trie, see {@link UtilTrie#print(Node, String)}.
     */
    public void print() {
        print(root, "");
    }

}
