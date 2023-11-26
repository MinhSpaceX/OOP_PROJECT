package com.zeus.utils.log;

import java.time.LocalDateTime;

/**
 * Custom log class to print messages to console.
 */
public class Logger {
    private static final String reset = "\u001B[0m";
    private static final String red = "\u001B[31m";
    private static final String green = "\u001B[32m";
    private static final String yellow = "\u001B[33m";

    /**
     * Function to log build message.
     *
     * @param msg The message.
     */
    public static void info(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[%s][INFO]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), green);
    }

    /**
     * Warn log method.
     *
     * @param msg Message to print.
     */
    public static void warn(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[%s][WARNING]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), yellow);
    }

    /**
     * Error log method.
     *
     * @param msg Message to print.
     */
    public static void error(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[%s][ERROR]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), red);
    }

    /**
     * Similar to {@link Exception#printStackTrace()}.
     *
     * @param e {@link Exception} to print.
     */
    public static void printStackTrace(Exception e) {
        printLineWithColor(String.format("[%s][ERROR] %s: %s", LocalDateTime.now().toString().replace("T", " "), e.getClass().getSimpleName(), e.getMessage()), red);
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (StackTraceElement traceElement : stackTraceElements) {
            printLineWithColor(String.format("     at %s", traceElement.toString()), red);
        }
        if (e.getCause() == null) return;
        printLineWithColor(String.format("  Caused by %s: %s", e.getCause().getClass().getSimpleName(), e.getCause().getMessage()), red);
        for (StackTraceElement stackTraceElement : e.getCause().getStackTrace()) {
            printLineWithColor(String.format("     at %s", stackTraceElement), red);
        }
    }

    /**
     * Print stack trace from the called position.
     *
     * @param message Message to print.
     */
    public static void printStackTrace(String message) {
        StackTraceElement[] stackTraceElements = getStackTrace();
        printLineWithColor(String.format("[%s][INFO]: %s", getCurrentTime(), message), green);
        for (int i = 3; i < stackTraceElements.length; i++) {
            printLineWithColor(String.format("     at %s", stackTraceElements[i].toString()), green);
        }
    }

    /**
     * Get current time.
     *
     * @return Current time.
     */
    private static String getCurrentTime() {
        return LocalDateTime.now().toString().replace("T", " ");
    }

    /**
     * Print line with color in console.
     *
     * @param message Message.
     * @param color   Color.
     */
    private static void printLineWithColor(String message, String color) {
        System.out.printf("%s%s%s\n", color, message, reset);
    }

    /**
     * Function take information about Line-info.
     *
     * @return {@link LineInfo}.
     */
    private static LineInfo getLineInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 0 is getStackTrace() | 1 is getLineInfo() | 2 is info()
        StackTraceElement callingMethod = stackTrace[3];

        String fileName = callingMethod.getFileName();
        int lineNumber = callingMethod.getLineNumber();
        String method = callingMethod.getMethodName();
        return new LineInfo(fileName, lineNumber, method);
    }

    /**
     * Get stack trace.
     *
     * @return {@link StackTraceElement} array.
     */
    private static StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }
}
