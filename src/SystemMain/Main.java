package SystemMain;

import DictionaryManager.*;
import Input.Input;
import java.io.IOException;

import java.util.Scanner;

public class Main {
    static Scanner sc = Input.getScanner();

    public static void main(String[] args) throws IOException {

        //khai bao cac gia tri
        DictionaryManagement manager = new DictionaryManagement();
        DictionaryCommandline dictCom = new DictionaryCommandline(manager);

        manager.insertFromFile(DictionaryID.ENGLISH_ENGLISH);
        
        // nhap so luong tu muon them
        System.out.println("Enter the number of words you want to add: ");
        int num =Integer.parseInt(sc.nextLine());

        for (int i = 0; i < num; i++) {
            manager.insertFromCommandLine(sc, DictionaryID.ENGLISH_ENGLISH);
        }
        manager.dictionaryExportToFile(DictionaryID.ENGLISH_ENGLISH);
        
        //in cac tu trong tu dien.
        dictCom.ShowAllWords(DictionaryID.ENGLISH_ENGLISH);
        //in giao dien menu.
        int inputOfCustomer = dictCom.dictionaryAdvanced();
        //in ra các từ bắt đầu bằng key.
        if (inputOfCustomer == 3) {
            String key = dictCom.cinKey();
            for (String a : manager.dictionarySearcher(key, DictionaryID.ENGLISH_ENGLISH)) {
                System.out.println(a);
            }
        }
        terminate();
    }

    /***
     * Call this to terminate all created utils.
     */
    static void terminate() {
        Input.getScanner().close();
    }
}