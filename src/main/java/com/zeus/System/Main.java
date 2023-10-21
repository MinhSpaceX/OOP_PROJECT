package com.zeus.System;

import com.zeus.utils.file.FileManager;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        long startTime = System.currentTimeMillis();
        String url1 = "/com/zeus/data/dictionary.json";
        String url2 = "/com/zeus/data/english-vietnamese.json";
        int words = 10;
        Objects.requireNonNull(FileManager.loadWord(url2, words)).forEach(word -> System.out.println(word.toString()));
        long endTime = System.currentTimeMillis();
        System.out.printf("Execution Time: %s Millisecond",endTime-startTime);
    }
}
