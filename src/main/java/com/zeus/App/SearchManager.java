package com.zeus.App;

import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.DictionaryManager.WordFactory;
import com.zeus.utils.trie.Trie;
import com.zeus.System.System;
import java.util.List;

public class SearchManager {

    private MongoPanel mgp = null;
    private Trie searchPath = null;

    public void loadDataFromBase(){
        mgp = System.getMongoPanel();
        mgp.fetchDatafromBase();
        searchPath = mgp.ReturnTrie();
        WordFactory wordFactory = new WordFactory(mgp.fetchWord("run"));
        wordFactory.getSingleWordArray().forEach(singleWord -> {
            java.lang.System.out.printf("%s %s %s %s", singleWord.getWordTarget(), singleWord.getPronoun(), singleWord.getType(), singleWord.getMeaning());
        });
    }

    /**
     * everytime user type a word to input, call this method
     */
    public List<String> searchFilter(String input) {
        return searchPath.autoFill(input, 7, 1);
    }
}
