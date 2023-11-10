package com.zeus.utils.log;

import jdk.jfr.StackTrace;

import java.util.Arrays;

public class Logger {
    private static final String reset = "\u001B[0m";
    private static final String red = "\u001B[31m";
    private static final String green = "\u001B[32m";
    private static final String yellow = "\u001B[33m";

    /**
     * Function to log build message.
     * @param msg The message.
     */
    public static void info(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[INFO]: %s Method: %s executed at line: %d in file: %s.", msg, info.getMethod(), info.getLineNumber(), info.getFileName()), green);
    }
    public static void warn(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[WARNING]: %s Method: %s executed at line: %d in file: %s.", msg, info.getMethod(), info.getLineNumber(), info.getFileName()), yellow);
    }

    public static void error(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[ERROR]: %s Method: %s executed at line: %d in file: %s.", msg, info.getMethod(), info.getLineNumber(), info.getFileName()), red);
    }

    public static void printStackTrace(Exception e) {
        printLineWithColor(String.format("[ERROR] %s: %s", e.getClass().getSimpleName(), e.getMessage()), red);
        StackTraceElement[] stackTraceElements = getStackTrace();
        for (int i = 3; i < stackTraceElements.length; i++) {
            printLineWithColor(String.format("     at %s", stackTraceElements[i].toString()), red);
        }
    }

    public static void printStackTrace(String message) {
        StackTraceElement[] stackTraceElements = getStackTrace();
        printLineWithColor(String.format("[INFO]: %s", message), green);
        for (int i = 3; i < stackTraceElements.length; i++) {
            printLineWithColor(String.format("     at %s", stackTraceElements[i].toString()), green);
        }
    }

    public static void main(String[] args) {
        try {
                throw new Exception("Damn bro");
        } catch (Exception e) {
            printStackTrace(e);
            e.printStackTrace();
        }
    }

    private static void printLineWithColor(String message, String color) {
        System.out.printf("%s%s%s\n", color, message, reset);
    }

    /**
     * Function take information about Line-info.
     * @return lineinfo.
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

    private static StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }
}
