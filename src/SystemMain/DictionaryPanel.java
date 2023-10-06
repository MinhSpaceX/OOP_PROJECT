package SystemMain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import DictionaryManager.DictionaryCommandline;
import DictionaryManager.DictionaryID;
import DictionaryManager.DictionaryManagement;
import GameManager.GameManagement;
import Input.Input;

public class DictionaryPanel {
	
	private DictionaryManagement manager = new DictionaryManagement();
	private DictionaryCommandline dictCom = new DictionaryCommandline(manager);
	private boolean app_state = true;
	private Scanner sc = Input.getScanner();
	private GameManagement game_manager;
	
	/**
	 * Open the Commandline interface to use
	 * @throws IOException _Avoid exception from file input
	 * @throws FileNotFoundException _Avoid exception from file input
	 */
	public void CommandLineManager() throws FileNotFoundException, IOException {
		
		System.out.println("Welcome to My Application");
        manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE);
        
        //start the application
        while(app_state) {
	        int inputOfCustomer = dictCom.getInterface();
			clearScreen();
	        switch (inputOfCustomer) {
	        case 0:
	        	app_state = false;
	        	break;
	        case 1: // add word
	        	System.out.println("Enter the number of words you want to add: ");
	            int num = Integer.parseInt(sc.nextLine());
	
	            for (int i = 0; i < num; i++) {
	                manager.insertFromCommandLine(sc, DictionaryID.ENGLISH_VIETNAMESE);
	            }
	            //override the current file
	            manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE);
	            break;
	        case 2: // delete word
	        	System.out.println("Enter the word number that you want to remove: ");
	        	int wordNum = Integer.parseInt(sc.nextLine());
	        	manager.removeFromDictionary(wordNum, DictionaryID.ENGLISH_VIETNAMESE);
	        	manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE);
	        	break;
	        case 3: // update word
	        	System.out.println("Enter the word number that you want to update: ");
	        	int wordNum2 = Integer.parseInt(sc.nextLine());
	        	manager.updateDictionary(wordNum2, DictionaryID.ENGLISH_VIETNAMESE, sc);
	        	manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE);
	        	break;
	        case 4: // show word
	        	dictCom.ShowAllWords(DictionaryID.ENGLISH_VIETNAMESE);
	        	break;
	        case 5: // look up
	        	System.out.println("Enter the word you want to look up: ");
	        	String word = sc.nextLine();
	        	ArrayList<String> pList = manager.dictionarySearcher(word, DictionaryID.ENGLISH_VIETNAMESE);
	        	for(String i : pList) {
	        		System.out.println(i);
	        	}
	        	break;	  
	        case 7:
	        	game_manager = new GameManagement(manager.getDictionary(DictionaryID.ENGLISH_VIETNAMESE), sc);
	        	game_manager.StartGame();
	        	break;
	        case 8:
	        	//Note: this is uncompleted
	        	//Update: user should be able import from an .txt with different source
	        	manager.insertFromFile(DictionaryID.ENGLISH_VIETNAMESE);
	        case 9:
	        	//Note: this is uncompleted
	        	//Update: user should be able to export from different dictionary
	        	manager.dictionaryExportToFile(DictionaryID.ENGLISH_VIETNAMESE);
	        }
	        
        }
        //in ra các từ bắt đầu bằng key.
        /*if (inputOfCustomer == 3) {
            String key = dictCom.cinKey();
            for (String a : manager.dictionarySearcher(key, DictionaryID.ENGLISH_ENGLISH)) {
                System.out.println(a);
            }
        }*/
        terminate();
	}
	
	public void terminate() {
        Input.getScanner().close();
    }
	
	private void clearScreen() {
		dictCom.clear();
	}
}
