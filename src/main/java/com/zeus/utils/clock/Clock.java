package com.zeus.utils.clock;

import javafx.scene.Parent;
import javafx.scene.SubScene;

public class Clock {
    private static long staticStart = 0;
    private static long staticEnd = 0;
    public static void Tick() {
        staticStart = System.nanoTime();
    }

    public static void Tock() {
        staticEnd = System.nanoTime();
    }

    public static void printTime() {
        System.out.printf("Method execution take: %f seconds\n", (staticEnd - staticStart)/1000000000.0f);
    }

    public static void timer(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        System.out.printf("Method execution take: %f seconds\n", (end - start)/1000000000.0f);
    }
}
