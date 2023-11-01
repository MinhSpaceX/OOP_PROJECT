package com.zeus.System;

import com.zeus.App.App;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Initializer.terminate();
            }
        });
        App.run(args);
    }
}
