package com.zeus.utils.log;

public class Logger {

    /**
     * Function to log build message.
     * @param msg The message.
     */
    public static void info(String msg) {
        LineInfo info = getLineInfo();
        System.out.printf("[INFO]: %s. Method: %s executed at line: %d in file: %s.\n", msg, info.getMethod(), info.getLineNumber(), info.getFileName());
    }

    private static LineInfo getLineInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 0 is getStackTrace() | 1 is getLineInfo() | 2 is info()
        StackTraceElement callingMethod = stackTrace[3];

        String fileName = callingMethod.getFileName();
        int lineNumber = callingMethod.getLineNumber();
        String method = callingMethod.getMethodName();
        return new LineInfo(fileName, lineNumber, method);
    }
}

class LineInfo {
    private final String fileName;
    private final int lineNumber;
    private final String method;

    public LineInfo(String fileName, int lineNumber, String method) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
