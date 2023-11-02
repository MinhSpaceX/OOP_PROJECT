package com.zeus.utils.clock;

import javafx.scene.Parent;

public class Clock {
    private static long start = 0;
    private static long end = 0;

    public static void tick() {
        start = System.nanoTime();
    }

    public static void tock() {
        end = System.nanoTime();
    }

    public static void printTime() {
        System.out.println("Method execution take: " + (end - start)/1000000000.0f + " seconds.");
    }
}
