package com.zeus.utils.log;

import jdk.jfr.StackTrace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Stack;

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
        printLineWithColor(String.format("[%s][INFO]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), green);
    }
    public static void warn(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[%s][WARNING]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), yellow);
    }

    public static void error(String msg) {
        LineInfo info = getLineInfo();
        printLineWithColor(String.format("[%s][ERROR]: %s Method: %s executed at line: %d in file: %s.", getCurrentTime(), msg, info.getMethod(), info.getLineNumber(), info.getFileName()), red);
    }

    public static void printStackTrace(Exception e) {
        printLineWithColor(String.format("[%s][ERROR] %s: %s", LocalDateTime.now().toString().replace("T", " "), e.getClass().getSimpleName(), e.getMessage()), red);
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            printLineWithColor(String.format("     at %s", stackTraceElements[i].toString()), red);
        }
        if (e.getCause() == null) return;
        printLineWithColor(String.format("  Caused by %s: %s", e.getCause().getClass().getSimpleName(), e.getCause().getMessage()), red);
        for (StackTraceElement stackTraceElement : e.getCause().getStackTrace()) {
            printLineWithColor(String.format("     at %s", stackTraceElement), red);
        }
    }

    public static void printStackTrace(String message) {
        StackTraceElement[] stackTraceElements = getStackTrace();
        printLineWithColor(String.format("[%s][INFO]: %s", getCurrentTime(), message), green);
        for (int i = 3; i < stackTraceElements.length; i++) {
            printLineWithColor(String.format("     at %s", stackTraceElements[i].toString()), green);
        }
    }

    private static String getCurrentTime() {
        return LocalDateTime.now().toString().replace("T", " ");
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
