package com.zeus.System;

import com.zeus.utils.file.FileManager;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        Objects.requireNonNull(FileManager.loadWord("/com/zeus/data/dictionary.json")).forEach(word -> System.out.println(word.toString()));
    }
}
