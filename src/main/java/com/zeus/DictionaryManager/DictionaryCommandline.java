package com.zeus.DictionaryManager;

public class DictionaryCommandline {
    private final DictionaryManagement manager;

    /**
     * Constructor.
     */
    public DictionaryCommandline(DictionaryManagement manager) {
        this.manager = manager;
        System.out.print("DictionaryCommandline created.\n");
    }

    public void ShowAllWords(DictionaryID id) {
        System.out.println("No | English | Vietnamese | WordType");
        int a = 1;
        Dictionary dict = manager.getDictionary(id);
        for (Word w : dict.getDictionary()) {
            System.out.format("%s | %s | %s | %s\n", a, w.GetWordTarget(), w.GetWordExplain(), w.GetWordType());
            a++;
        }
    }


}
