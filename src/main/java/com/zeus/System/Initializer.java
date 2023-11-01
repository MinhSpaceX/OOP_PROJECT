package com.zeus.System;

import com.zeus.DictionaryManager.DictionaryCommandline;
import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.utils.input.Input;

public class Initializer {
    static protected final DictionaryManagement manager;
    static protected final DictionaryCommandline dictCom;

    /*
      Static block to ensure the order of variable declaration
     */
    static {
        manager = new DictionaryManagement();
        dictCom = new DictionaryCommandline(manager);
    }

    static void terminate() {
        Input.closeScanner();
    }
}
