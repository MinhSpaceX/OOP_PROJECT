package com.zeus.utils.clock;

import com.zeus.utils.log.Logger;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import org.apache.commons.logging.Log;

public class Clock {
    private static long staticStart = 0;
    private static long staticEnd = 0;
    public static void Tick() {
        staticStart = System.nanoTime();
    }

    public static void Tock() {
        staticEnd = System.nanoTime();
    }

    public static long elapse() {
        return staticEnd - staticStart;
    }

    public static void printTime(String message) {
        System.out.printf("%s Method execution take: %f seconds\n", message, (staticEnd - staticStart)/1000000000.0f);
    }

    public static void timer(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        Logger.printStackTrace(String.format("Method execution take: %f seconds", (end - start)/1000000000.0f));
    }

    public static void timer(CustomRunnableClass method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        Logger.printStackTrace(String.format("%s Method execution take: %f seconds", method.message, (end - start)/1000000000.0f));
    }

    abstract public static class CustomRunnableClass implements Runnable{
        private String message;
        public CustomRunnableClass(String message) {
            this.message = message;
        }
    }
}
