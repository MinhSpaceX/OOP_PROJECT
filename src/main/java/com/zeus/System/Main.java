package com.zeus.System;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Initializer.terminate();
            }
        });
        DictionaryPanel p = new DictionaryPanel();
        p.CommandLineManager();
    }
}
