package com.zeus.App;

import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.DictionaryManager.Word;
import com.zeus.DictionaryManager.WordFactory;
import com.zeus.utils.trie.Trie;
import com.zeus.System.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchManager {

    private MongoPanel mgp = null;
    private Trie searchPath = null;

    public void loadDataFromBase(){
        mgp = System.getMongoPanel();
        mgp.fetchDatafromBase();
        searchPath = mgp.ReturnTrie();
    }

    /**
     * everytime user type a word to input, call this method
     */
    public List<String> searchFilter(String input) {
        return searchPath.autoFill(input, 7, 1);
    }

    public Map<String, List<SingleWord>> print(String wordtarget) {
        Word word = mgp.fetchWord(wordtarget);
        WordFactory wordFactory = new WordFactory(word);
        return wordFactory.getSingleWordMap();
    }
}
