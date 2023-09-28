package DictionaryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryManagement {
    private Map<DictionaryID, Dictionary> dictionaries;

    /**
     * Constructor.
     */
    public DictionaryManagement() {
        dictionaries = new HashMap<DictionaryID, Dictionary>();
        createDictionary();
    }
    /**
     * Function to create all dictionary from the enum file.
     */
    private void createDictionary() {
        for (DictionaryID x : DictionaryID.values()) {
            Dictionary dict = new Dictionary(x);
            dictionaries.put(dict.getId(), dict);
        }
        
    }
    /**
     * Function to create new word.
     * @param word_target the word to create.
     * @param word_explain definition of the word.
     * @return the word created.
     */
    public Word createWord(String word_target, String word_explain) {
        Word word = new Word(word_target, word_explain);
        return word;
    }
    /**
     * Function to get a dictionary from the provided ID.
     * @param id the id of the dictionary.
     * @return the dictionary.
     */
    Dictionary getDictionary(DictionaryID id) {
        return dictionaries.get(id);
    }
    
    /**
     * Function to add word to a dictionary.
     * @param word the word to add. 
     * @param id the ID of the dictionary to add the word to.
     */
    public void addWordToDictionary(Word word, DictionaryID id) {
        dictionaries.get(id).addWord(word);
    }

    /***
     * Function to get input from terminal.
     * @return inserted word.
     */
    public void insertFromCommandLine(Scanner sc, DictionaryID id) {
        System.out.println("Enter your input with format: word_target <enter> word_explain");
        String word_target = sc.nextLine();
        String word_explain = sc.nextLine();

        Word word = new Word(word_target, word_explain);

        addWordToDictionary(word, id);
    }
}
