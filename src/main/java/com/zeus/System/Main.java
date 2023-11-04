package com.zeus.System;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

            }
        });
        System system = new System();
        system.run(args);
    }
}