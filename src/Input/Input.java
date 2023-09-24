package Input;

import java.util.Scanner;

public class Input {
    static Scanner sc = new Scanner(System.in);

    /**
     * Get the scanner instance.
     * @return an instance of scanner class.
     */
    public static Scanner getScanner() {
        return sc;
    }

    /***
     * Close the scanner.
     */
    public static void closeScanner() {
        sc.close();
    }
}
