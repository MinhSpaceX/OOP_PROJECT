package com.zeus.Managers.Search;

import com.zeus.Managers.Database.MongoManager;
import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.DictionaryUtil.SingleWord;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.trie.Trie;

import java.util.List;
import java.util.Map;

public class SearchManager extends Manager {
    public static Trie searchPath = new Trie();
    public static Trie userTrie = new Trie();
    private static MongoManager mgp = null;
    private static SQLite sqLite = null;

    /**
     * Searches the user database using a Trie for autocompletion suggestions.
     *
     * @param input The input string to search for.
     * @return A list of autocompletion suggestions based on the input.
     */
    public static List<String> searchFilterUserDb(String input) {
        return userTrie.autoFill(input, 7, 1);
    }

    /**
     * Retrieves word instances from database based on the target word.
     *
     * @param wordTarget The target word to retrieve instances for.
     * @return A map containing word instances
     * grouped by type, including information from databases.
     */
    public static Map<String, List<SingleWord>> getWordInstance(String wordTarget) {
        Map<String, List<SingleWord>> result = sqLite.getWordFromDb(wordTarget);
        try {
            sqLite.getWord(wordTarget).forEach((k, v) -> {
                List<SingleWord> value = result.get(k);
                if (value != null) value.addAll(v);
                else result.put(k, v);
            });
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        return result;
    }

    /**
     * Searches for a word in the search path.
     *
     * @param word The word to search for.
     * @return True if the word is found, false otherwise.
     */
    public static boolean search(String word) {
        return searchPath.search(word.toLowerCase());
    }

    /**
     * Retrieves the Trie instance associated with user data.
     *
     * @return The Trie instance containing user data.
     */
    public Trie getUserTrie() {
        return userTrie;
    }

    /**
     * Retrieves the Trie instance associated with main data.
     *
     * @return The Trie instance containing main data.
     */
    public Trie getSearchPathTrie() {
        return searchPath;
    }

    /**
     * everytime user type a word to input, call this method
     */
    public List<String> autoFill(String input, Trie trie) {
        return trie.autoFill(input, 7, 1);
    }

    @Override
    public void init() {
        mgp = SystemManager.getManager(MongoManager.class);
        sqLite = SystemManager.getManager(SQLite.class);
        sqLite.loadTrieFromUserDb(searchPath, userTrie);
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

    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("Database");
    }

}
