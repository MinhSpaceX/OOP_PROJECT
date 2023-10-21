package com.zeus.System;

import com.zeus.utils.file.FileManager;
import com.zeus.utils.trie.Trie;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        long startTime = System.currentTimeMillis();
        String url1 = "/com/zeus/data/dictionary.json";
        String url2 = "/com/zeus/data/english-vietnamese.json";
        int words = 10;
        Trie trie = FileManager.loadTrie(url2, words);
        long endTime = System.currentTimeMillis();
        System.out.printf("Execution Time: %s Millisecond\n",endTime-startTime);
        trie.printAll();
    }
}
