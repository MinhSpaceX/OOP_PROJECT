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

    public void ShowAllWords() {
        Dictionary dict = manager.getDictionary();
        int count = 0;
        for (Word a : dict.getDictionary()) {
            System.out.println(a.print());
        }
    }

}
