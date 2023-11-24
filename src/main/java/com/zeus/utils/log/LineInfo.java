package com.zeus.utils.log;

class LineInfo {
    private final String fileName;
    private final int lineNumber;
    private final String method;

    /**
     * constructor.
     *
     * @param fileName   name of file
     * @param lineNumber line error occurred
     * @param method     method name
     */
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
