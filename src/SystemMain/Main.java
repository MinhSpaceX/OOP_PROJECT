package SystemMain;

import DictionaryManager.*;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        //khai bao cac gia tri
        Word newword = new Word();
        DictionaryManagement DicMan = new DictionaryManagement();
        DictionaryCommandline DicCom = new DictionaryCommandline();
        Dictionary dic = new Dictionary();
        Scanner sc = new Scanner(System.in);

        // nhap so luong tu muon them
        System.out.println("Enter the number of words you want to add: ");
        int num = sc.nextInt();

        for (int i = 0; i < num; i++) {
            newword = DicMan.insertFromCommandLine();
            dic.GetAllWords(newword);
        }

        //in cac tu trong tu dien
        DicCom.ShowAllWords(dic.ReturnWordSaver());
    }
}