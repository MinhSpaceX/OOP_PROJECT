package SystemMain;

import DictionaryManager.*;
import Input.Input;

import java.util.Scanner;

public class Main {
    static Scanner sc = Input.getScanner();

    public static void main(String[] args) {

        //khai bao cac gia tri
        Word newWord = new Word();
        DictionaryCommandline dictCom = new DictionaryCommandline();

        // nhap so luong tu muon them
        System.out.println("Enter the number of words you want to add: ");
        int num = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < num; i++) {
            newWord = DictionaryManagement.insertFromCommandLine();
            Dictionary.addWord(newWord);
        }

        //in cac tu trong tu dien
        dictCom.ShowAllWords(Dictionary.getDictionary());
        terminate();
    }

    /***
     * Call this to terminate all created utils.
     */
    static void terminate() {
        Input.getScanner().close();
    }
}