package DictionaryManager;

import Input.Input;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    /**
     * Function to search for words start with the key value.
     * @param key : value to pass in.
     * @return list string 
     */
    public String[] dictionarySearcher(String key, DictionaryID id) {
        String[] pList = new String[5];
        int i=0;
        for (Word a : dictionaries.get(id).getDictionary()) {
            if (a.GetWordTarget().startsWith(key)) {
                pList[i] = a.GetWordTarget();
                i++;
                //System.out.println(a.GetWordTarget());
            }
        }
        return pList;
    }

    /**
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
    
    /**
     * function Import dictionary data from a text file.
     * @param id the DictionaryID.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void insertFromFile(DictionaryID id) throws FileNotFoundException, IOException {
        File f = new File("dictionary.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String[] info = line.split("[|]");
            String word_target = info[0].trim();
            String word_explain = info[1].trim();
            addWordToDictionary(new Word(word_target,word_explain), id);
        }
        
        br.close();
        fr.close();
    }
    
    /**
     * Function export existing dictionary data to a text file.
     * @param id the DictionaryID.
     * @throws IOException 
     */
    public void dictionaryExportToFile(DictionaryID id) throws IOException {
        File f = new File("dictionary.txt");
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        
            for( Word a : dictionaries.get(id).getDictionary()) {
                bw.write(a.toString());
            } 
            
        bw.close();
        fw.close();
            
    }
}
