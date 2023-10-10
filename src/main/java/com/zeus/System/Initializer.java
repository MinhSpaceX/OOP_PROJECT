package com.zeus.System;

import com.zeus.DictionaryManager.DictionaryCommandline;
import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.utils.input.Input;

public class Initializer {
    static protected final String txtPath;
    static protected final String dbPath;

    static protected final DictionaryManagement manager;
    static protected final DictionaryCommandline dictCom;

    /*
      Static block to ensure the order of variable declaration
     */
    static {
        txtPath = Default.DATABASE_PATH_TXT;
        dbPath = Default.DATABASE_PATH_DB;

        manager = new DictionaryManagement();
        dictCom = new DictionaryCommandline(manager);
    }

    static void terminate() {
        Input.closeScanner();
    }
}
