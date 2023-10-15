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
}
