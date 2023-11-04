package com.zeus.App;

import com.zeus.DatabaseManager.MongoManager;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.DictionaryManager.Word;
import com.zeus.DictionaryManager.WordFactory;
import com.zeus.System.System;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.managerfactory.SystemManager;
import com.zeus.utils.trie.Trie;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.Map;

public class SearchManager extends Manager {

    private MongoManager mgp = null;
    private static Trie searchPath = null;

    /**
     * everytime user type a word to input, call this method
     */
    public static List<String> searchFilter(String input) {
        return searchPath.autoFill(input, 7, 1);
    }

    public Map<String, List<SingleWord>> print(String wordTarget) {
        Word word = mgp.fetchWord(wordTarget);
        WordFactory wordFactory = new WordFactory(word);
        return wordFactory.getSingleWordMap();
    }

    @Override
    public void init(Config config) {
        mgp = SystemManager.getManager(MongoManager.class);
        searchPath = mgp.ReturnTrie();
        if (mgp == null) {
            Logger.error("MongoManager is null.");
        }
        if (searchPath == null) {
            Logger.error("Trie is null.");
        }
    }
}
