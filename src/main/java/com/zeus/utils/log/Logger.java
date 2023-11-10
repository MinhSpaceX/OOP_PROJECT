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
        System.out.printf("%s[INFO]: %s Method: %s executed at line: %d in file: %s%s.\n", green, msg, info.getMethod(), info.getLineNumber(), info.getFileName(), reset);
    }
    public static void warn(String msg) {
        LineInfo info = getLineInfo();
        System.out.printf("[WARNING]: %s Method: %s executed at line: %d in file: %s.\n", msg, info.getMethod(), info.getLineNumber(), info.getFileName());
    }

    public static void error(String msg) {
        LineInfo info = getLineInfo();
        System.out.printf("[ERROR]: %s Method: %s executed at line: %d in file: %s.\n", msg, info.getMethod(), info.getLineNumber(), info.getFileName());
    }

    public static void printStackTrace(Exception e) {
        System.out.printf("%s[ERROR] %s: %s%s\n", red, e.getClass().getSimpleName(), e.getMessage(), reset);
        StackTraceElement[] stackTraceElements = getStackTrace();
        for (int i = 3; i < stackTraceElements.length; i++) {
            System.out.printf("%s     at %s%s\n", red, stackTraceElements[i].toString(), reset);
        }
    }

    public static void printStackTrace(String message) {
        StackTraceElement[] stackTraceElements = getStackTrace();
        for (int i = 3; i < stackTraceElements.length; i++) {
            System.out.printf("%s     at %s%s\n", green, stackTraceElements[i].toString(), reset);
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
