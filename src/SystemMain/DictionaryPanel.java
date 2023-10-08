package SystemMain;

import DictionaryManager.DictionaryID;
import GameManager.GameManagement;
import utils.Input.Input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryPanel extends Initializer {
    private boolean app_state = true;
    private GameManagement game_manager;

    /**
     * Contructor.
     *
     * @param filePath The path to txt data file.
     */
    DictionaryPanel() {
        manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE, txtPath);
        System.out.printf("DictionaryPanel created.\n");
    }
    /**
     * delay time run code
    */
    private void delay() {
        try {
            long milliseconds = 1000;
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear console.
     */
    private void clear() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getInterface() {
        System.out.println("Welcome to My Application");
        System.out.println("[0] Exit");
        System.out.println("[1] Add");
        System.out.println("[2] Remove");
        System.out.println("[3] Update");
        System.out.println("[4] Display");
        System.out.println("[5] Lookup");
        System.out.println("[6] Search");
        System.out.println("[7] Game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");
        System.out.println("Your action: ");

        int input = Input.getInteger();
        clear();

        return input;
    }

    /**
     * Open the Commandline interface to use
     *
     * @throws IOException           _Avoid exception from file input
     * @throws FileNotFoundException _Avoid exception from file input
     */
    public void CommandLineManager() {

        //start the application
        while (app_state) {
            int inputOfCustomer = getInterface();
            boolean function_state = true;
            switch (inputOfCustomer) {
                case 0:
                    app_state = false;
                    break;
                case 1: // add word
                    while (function_state) {
                        System.out.println("Enter the number of words you want to add: ");
                        int num = Integer.parseInt(Input.getLine());

                        for (int i = 0; i < num; i++) {
                            manager.insertFromCommandLine(DictionaryID.ENGLISH_VIETNAMESE);
                        }
                        //override the current file
                        manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, txtPath);

                        clear();
                        System.out.println("Do you want to continue: Yes or No");
                        System.out.println("Enter your option: ");
                        String option = Input.getLine();
                        if (option.equals("Yes")) clear();
                        else {
                            clear();
                            break;
                        }
                    }
                    break;
                case 2: // delete word
                    while (function_state) {
                        System.out.println("Enter the word number that you want to remove: ");
                        int wordNum = Integer.parseInt(Input.getLine());
                        manager.removeFromDictionary(wordNum, DictionaryID.ENGLISH_VIETNAMESE);
                        manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, txtPath);
                        clear();
                        System.out.println("Do you want to continue: Yes or No");
                        System.out.println("Enter your option: ");
                        String option = Input.getLine();
                        if (option.equals("Yes")) clear();
                        else {
                            clear();
                            break;
                        }
                    }
                    break;
                    
                case 3: // update word
                    while (function_state) {
                        System.out.println("Enter the word number that you want to update: ");
                        int wordNum2 = Integer.parseInt(Input.getLine());
                        manager.updateDictionary(wordNum2, DictionaryID.ENGLISH_VIETNAMESE);
                        manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, txtPath);
                        clear();
                        System.out.println("Do you want to continue: Yes or No");
                        System.out.println("Enter your option: ");
                        String option = Input.getLine();
                        if (option.equals("Yes")) clear();
                        else {
                            clear();
                            break;
                        }
                    }
                    
                case 4: // show word
                    dictCom.ShowAllWords(DictionaryID.ENGLISH_VIETNAMESE);
                    delay();
                    clear();
                    break;
                case 5: // look up
                    while (function_state) {
                        System.out.println("Enter the word you want to look up: ");
                        String word = Input.getLine();
                        ArrayList<String> pList = manager.dictionarySearcher(word, DictionaryID.ENGLISH_VIETNAMESE);
                        if (pList.size() == 0) System.out.println("Cannot find!"); 
                        else {
                            for (String i : pList) {
                                System.out.println(i);
                            }
                        }
                        delay();
                        clear();
                        System.out.println("Do you want to continue: Yes or No");
                        System.out.println("Enter your option: ");
                        String option = Input.getLine();
                        if (option.equals("Yes")) clear();
                        else {
                            clear();
                            break;
                        }
                    }
                    break;
                case 7: // Game
                    while (function_state) {
                        game_manager = new GameManagement(DictionaryID.ENGLISH_VIETNAMESE, manager);
                        game_manager.StartGame();
                        delay();
                        clear();
                        System.out.println("Do you want to continue: Yes or No");
                        System.out.println("Enter your option: ");
                        String option = Input.getLine();
                        if (option.equals("Yes")) clear();
                        else {
                            clear();
                            break;
                        }
                    }
                    break;
                    
                case 8:
                    //Note: this is uncompleted
                    //Update: user should be able import from an .txt with different source
                    manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE, "");
                    break;
                case 9:
                    //Note: this is uncompleted
                    //Update: user should be able to export from different dictionary
                    manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, "");
                    break;
                default:
                    //input < 0 or > 9
                    System.out.println("Action not supported");
                    delay();
                    clear();
                    break;
            }
        }
    }
}
