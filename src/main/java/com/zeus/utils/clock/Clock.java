package com.zeus.utils.clock;

import javafx.scene.Parent;
import javafx.scene.SubScene;

public class Clock {

    public static void timer(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        System.out.printf("Method execution take: %f seconds\n", (end - start)/1000000000.0f);
    }
}
