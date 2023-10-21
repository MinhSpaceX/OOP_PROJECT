package com.zeus.System;

import com.zeus.utils.file.FileManager;
import com.zeus.utils.trie.Trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        /*long startTime = System.currentTimeMillis();
        String url1 = "/com/zeus/data/dictionary.json";
        String url2 = "/com/zeus/data/english-vietnamese.json";
        int words = 10;
        Objects.requireNonNull(FileManager.loadWord(url2, words)).forEach(word -> System.out.println(word.toString()));
        long endTime = System.currentTimeMillis();
        System.out.printf("Execution Time: %s Millisecond\n",endTime-startTime);*/

        Trie trie = new Trie();
        trie.insert("cat");
        trie.insert("car");
        trie.insert("cats");
        trie.insert("catse");
        trie.insert("catses");
        trie.delete("cats");
        trie.search("cat");
        trie.search("cats");
        trie.search("catse");
        trie.search("catses");
        //trie.printAll();
    }
}
