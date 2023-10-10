package com.zeus.DictionaryManager;

import com.zeus.utils.input.Input;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictionaryManagement {
    private final Map<DictionaryID, Dictionary> dictionaries;

    /**
     * Constructor.
     */
    public DictionaryManagement() {
        dictionaries = new HashMap<>();
        createDictionary();
        System.out.print("DictionaryManagement created.\n");
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
     *
     * @param word_target  the word to create.
     * @param word_explain definition of the word.
     * @return the word created.
     */
    public Word createWord(String word_target, String word_explain, String word_type) {
        return new Word(word_target, word_explain, word_type);
    }

    /**
     * Function to get a dictionary from the provided ID.
     *
     * @param id the id of the dictionary.
     * @return the dictionary.
     */
    public Dictionary getDictionary(DictionaryID id) {
        return dictionaries.get(id);
    }

    /**
     * Get the array with provided id.
     *
     * @param id the id of dictionary.
     * @return the list of all the words.
     */
    public ArrayList<Word> getListOfWords(DictionaryID id) {
        return getDictionary(id).getDictionary();
    }

    /**
     * Function to add word to a dictionary.
     *
     * @param word the word to add.
     * @param id   the ID of the dictionary to add the word to.
     */
    public void addWordToDictionary(Word word, DictionaryID id) {
        dictionaries.get(id).addWord(word);
    }

    public ArrayList<String> dictionarySearcher(String word, DictionaryID id) {
        ArrayList<String> pList = new ArrayList<>();

        //look up all the word with similar prefix
        for (Word a : dictionaries.get(id).getDictionary()) {
            if (a.GetWordTarget().startsWith(word)) {
                pList.add(a.GetWordTarget());
            }
        }
        return pList;
    }

    public void insertFromCommandLine(DictionaryID id) {
        System.out.println("Enter your input with format: word_target <enter> word_explain <enter> word_type");
        String word_target = Input.getLine();
        String word_explain = Input.getLine();
        String word_type = Input.getLine();

        Word word = new Word(word_target, word_explain, word_type);

        addWordToDictionary(word, id);
    }

    public void removeFromDictionary(int index, DictionaryID id) {

        ArrayList<Word> dictionary = dictionaries.get(id).getDictionary();
        //Search for the word
        dictionary.remove(index - 1);
    }

    /**
     * update new word to the desired position
     *
     * @param index The index.
     * @param id the id.
     */
    public void updateDictionary(int index, DictionaryID id) {
        ArrayList<Word> dictionary = dictionaries.get(id).getDictionary();
        System.out.println("Input the change: word_target <enter> word_explain <enter> word_type");
        String word_target = Input.getLine();
        String word_explain = Input.getLine();
        String word_type = Input.getLine();

        Word word = new Word(word_target, word_explain, word_type);

        dictionary.set(index - 1, word);

        System.out.println("Your word has been update!");
    }

    public void insertFromFile(DictionaryID id, String filePath) {
        System.out.println(filePath);
        try (FileReader fr = new FileReader(String.valueOf(this.getClass().getResource(filePath)));
             BufferedReader br = new BufferedReader(fr)) {

            while (true) {
                String line = br.readLine();
                if (line == null) break;
                String[] info = line.split("[|]");
                String word_target = info[0].trim();
                String word_explain = info[1].trim();
                String word_type = info[2].trim();
                addWordToDictionary(new Word(word_target, word_explain, word_type), id);
            }

        } catch (IOException e) {
            System.out.printf("%s", e.getMessage());
        }
    }

    public void dictionaryExportToFile(DictionaryID id, String filePath) {
        try (FileWriter fw = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Word a : dictionaries.get(id).getDictionary()) {
                bw.write(a.toString());
            }
        } catch (IOException e) {
            System.out.printf("%s", e.getMessage());
        }

    }
}
