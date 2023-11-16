package com.zeus.App;

import com.zeus.DatabaseManager.MongoManager;
import com.zeus.DatabaseManager.SQLite;
import com.zeus.DictionaryManager.SingleWord;
import com.zeus.DictionaryManager.Word;
import com.zeus.DictionaryManager.WordFactory;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.managerfactory.SystemManager;
import com.zeus.utils.trie.Trie;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.Map;

public class SearchManager extends Manager {

    private static MongoManager mgp = null;
    private static SQLite sqLite = null;
    private static Trie searchPath = null;
    private static Trie userTrie = null;

    /**
     * everytime user type a word to input, call this method
     */
    public static List<String> searchFilter(String input) {
        return searchPath.autoFill(input, 7, 1);
    }

    public static Map<String, List<SingleWord>> getWordInstance(String wordTarget) {
        Word word = mgp.fetchWord(wordTarget);
        WordFactory wordFactory = new WordFactory(word);
        return wordFactory.getSingleWordMap();
    }

    @Override
    public void init(Config config) {
        mgp = SystemManager.getManager(MongoManager.class);
        searchPath = mgp.ReturnTrie();
        sqLite = SystemManager.getManager(SQLite.class);
        userTrie = sqLite.loadTrieFromUserDb();
        if (sqLite == null) {
            Logger.error("SQLite is null.");
        }
        if (mgp == null) {
            Logger.error("MongoManager is null.");
        }
        if (searchPath == null) {
            Logger.error("Trie is null.");
        }
    }
}
