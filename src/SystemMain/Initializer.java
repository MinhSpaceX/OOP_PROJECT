package SystemMain;

import Database.SQLiteManager;
import DictionaryManager.DictionaryCommandline;
import DictionaryManager.DictionaryManagement;
import utils.Input.Input;

public class Initializer {
    static protected final String txtPath;
    static protected final String dbPath;

    static protected final DictionaryManagement manager;
    static protected final DictionaryCommandline dictCom;
    static protected final SQLiteManager sqliteManager;

    /**
     * Static block to ensure the order of variable declaration
     */
    static {
        txtPath = Default.DATABASE_PATH_TXT;
        dbPath = Default.DATABASE_PATH_DB;

        manager = new DictionaryManagement();
        dictCom = new DictionaryCommandline(manager);
        sqliteManager = new SQLiteManager(dbPath);
    }

    static void terminate() {
        Input.closeScanner();
    }
}
