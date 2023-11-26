package com.zeus.utils.log;

/**
 * Class contain info line of code.
 */
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

    /**
     * Get method's name.
     *
     * @return Method's name.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get file's name.
     *
     * @return File's name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get line number.
     *
     * @return Line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }
}
