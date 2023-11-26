package com.zeus.utils.clock;

import com.zeus.utils.log.Logger;

/**
 * Clock class to calculate time.
 */
public class Clock {
    private static long staticStart = 0;
    private static long staticEnd = 0;

    /**
     * Start the count, call {@link #Tock()} to end the count
     * then call {@link #printTime(String)} to see the time elapsed
     * or {@link #elapse()} to get the time.
     */
    public static void Tick() {
        staticStart = System.nanoTime();
    }

    /**
     * End the count. For further reference, see {@link #Tick()}.
     */
    public static void Tock() {
        staticEnd = System.nanoTime();
    }

    /**
     * Get the elapsed time. For further reference, see {@link #printTime(String)}.
     *
     * @return
     */
    public static long elapse() {
        return staticEnd - staticStart;
    }

    /**
     * Print the time elapsed from the last call of {@link #Tick()} and the last call of {@link #Tock()}.
     *
     * @param message Message to display together with elapsed time.
     */
    public static void printTime(String message) {
        System.out.printf("%s Method execution take: %f seconds\n", message, (staticEnd - staticStart) / 1000000000.0f);
    }

    /**
     * Count how long it takes to run a method.
     *
     * @param method method class {@link Runnable} to count time.
     */
    public static void timer(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        Logger.printStackTrace(String.format("Method execution take: %f seconds", (end - start) / 1000000000.0f));
    }

    /**
     * Count how long it takes to run a method.
     *
     * @param method method class {@link CustomRunnableClass} with extra
     *               parameter message to count time and print message.
     */
    public static void timer(CustomRunnableClass method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        Logger.printStackTrace(String.format("%s Method execution take: %f seconds", method.message, (end - start) / 1000000000.0f));
    }

    /**
     * Custom class extends from {@link Runnable} with parameter message.
     */
    abstract public static class CustomRunnableClass implements Runnable {
        private final String message;

        /**
         * Constructor of custom Runnable class.
         *
         * @param message Message to pass in.
         */
        public CustomRunnableClass(String message) {
            this.message = message;
        }
    }
}
