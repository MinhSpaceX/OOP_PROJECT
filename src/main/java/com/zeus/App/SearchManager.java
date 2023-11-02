package com.zeus.App;

import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.utils.trie.Trie;

import java.util.ArrayList;
import java.util.List;

public class SearchManager {

    private MongoPanel mgp = new MongoPanel();
    private Trie searchPath = new Trie();

    public void loadDataFromBase(){
        mgp.fetchDatafromBase();
        searchPath = mgp.ReturnTrie();
    }

    /**
     * everytime user type a word to input, call this method
     */
    public List<String> searchFilter(String input) {
        return searchPath.autoFill(input, 7, 1);
    }

}
