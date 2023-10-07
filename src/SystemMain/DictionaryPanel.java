package SystemMain;

import DictionaryManager.DictionaryCommandline;
import DictionaryManager.DictionaryID;
import DictionaryManager.DictionaryManagement;
import GameManager.GameManagement;
import utils.Input.Input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryPanel {
    private final DictionaryManagement manager;
    private final DictionaryCommandline dictCom;
    private boolean app_state = true;
    private GameManagement game_manager;
    private final String filePath;

    /**
     * Contructor.
     *
     * @param filePath The path to txt data file.
     */
    DictionaryPanel(DictionaryManagement manager, DictionaryCommandline dictCom, String filePath) {
        this.manager = manager;
        this.dictCom = dictCom;
        this.filePath = filePath;
        manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE, filePath);
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

        if (input >= 0 && input <= 9) {
            return input;
        } else {
            System.out.println("Action not supported!");
            return input;
        }
    }

    /**
     * Open the Commandline interface to use
     *
     * @throws IOException           _Avoid exception from file input
     * @throws FileNotFoundException _Avoid exception from file input
     */
    public void CommandLineManager() {
        System.out.println("Welcome to My Application");

        //start the application
        while (app_state) {
            int inputOfCustomer = getInterface();
            switch (inputOfCustomer) {
                case 0:
                    app_state = false;
                    break;
                case 1: // add word
                    System.out.println("Enter the number of words you want to add: ");
                    int num = Integer.parseInt(Input.getLine());

                    for (int i = 0; i < num; i++) {
                        manager.insertFromCommandLine(DictionaryID.ENGLISH_VIETNAMESE);
                    }
                    //override the current file
                    manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, filePath);
                    break;
                case 2: // delete word
                    System.out.println("Enter the word number that you want to remove: ");
                    int wordNum = Integer.parseInt(Input.getLine());
                    manager.removeFromDictionary(wordNum, DictionaryID.ENGLISH_VIETNAMESE);
                    manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, filePath);
                    break;
                case 3: // update word
                    System.out.println("Enter the word number that you want to update: ");
                    int wordNum2 = Integer.parseInt(Input.getLine());
                    manager.updateDictionary(wordNum2, DictionaryID.ENGLISH_VIETNAMESE);
                    manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, filePath);
                    break;
                case 4: // show word
                    dictCom.ShowAllWords(DictionaryID.ENGLISH_VIETNAMESE);
                    break;
                case 5: // look up
                    System.out.println("Enter the word you want to look up: ");
                    String word = Input.getLine();
                    ArrayList<String> pList = manager.dictionarySearcher(word, DictionaryID.ENGLISH_VIETNAMESE);
                    for (String i : pList) {
                        System.out.println(i);
                    }
                    break;
                case 7:
                    game_manager = new GameManagement(DictionaryID.ENGLISH_VIETNAMESE, manager);
                    game_manager.StartGame();
                    break;
                case 8:
                    //Note: this is uncompleted
                    //Update: user should be able import from an .txt with different source
                    manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE, "");
                case 9:
                    //Note: this is uncompleted
                    //Update: user should be able to export from different dictionary
                    manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE, "");
            }

        }
        //in ra các từ bắt đầu bằng key.
        /*if (inputOfCustomer == 3) {
            String key = dictCom.cinKey();
            for (String a : manager.dictionarySearcher(key, DictionaryID.ENGLISH_ENGLISH)) {
                System.out.println(a);
            }
        }*/
    }
}
