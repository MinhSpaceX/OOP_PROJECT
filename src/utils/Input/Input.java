package utils.Input;

import java.util.Scanner;

public class Input {
    static final Scanner sc = new Scanner(System.in);

    /**
     * Get the scanner instance.
     *
     * @return an instance of scanner class.
     */
    public static Scanner getScanner() {
        return sc;
    }

    /***
     * Close the scanner.
     */
    public static synchronized void closeScanner() {
        sc.close();
    }

    public static String getLine() {
        return sc.nextLine();
    }

    public static Integer getInteger() {
        return Integer.parseInt(sc.nextLine());
    }
}
