package SystemMain;

import DictionaryManager.DictionaryCommandline;
import DictionaryManager.DictionaryID;
import DictionaryManager.DictionaryManagement;
import Input.Input;

import java.util.Scanner;

public class Main {
    static Scanner sc = Input.getScanner();

    public static void main(String[] args) {

        //khai bao cac gia tri
        DictionaryManagement manager = new DictionaryManagement();
        DictionaryCommandline dictCom = new DictionaryCommandline(manager);

        // nhap so luong tu muon them
        System.out.println("Enter the number of words you want to add: ");
        int num = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < num; i++) {
            manager.insertFromCommandLine(sc, DictionaryID.ENGLISH_ENGLISH);
        }

        //in cac tu trong tu dien
        dictCom.ShowAllWords(DictionaryID.ENGLISH_ENGLISH);
        terminate();
    }

    /***
     * Call this to terminate all created utils.
     */
    static void terminate() {
        Input.getScanner().close();
    }
}