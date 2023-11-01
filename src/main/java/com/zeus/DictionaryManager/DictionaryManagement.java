package com.zeus.DictionaryManager;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.input.Input;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DictionaryManagement {
    private final Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
        System.out.println("DictionaryManagement initialized\n");
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void addWordToDictionary(Word word) {
        dictionary.addWord(word);
    }

    public ArrayList<String> dictionarySearcher(String word) {
        ArrayList<String> pList = new ArrayList<>();

        //look up all the word with similar prefix
        for (Word a : dictionary.getDictionary()) {
            if (a.getWordTarget().startsWith(word)) {
                pList.add(a.getWordTarget());
                if (pList.size() > 10 ) break;
            }
        }
        return pList;
    }

    public Word createWord() {
        try {
            System.out.print("Enter word_target:");
            String word_target = Input.getLine();
            System.out.println("Enter pronoun: ");
            String pronoun = Input.getLine();

            List<Word.Description.Type> typesList = new ArrayList<>();

            System.out.println("Enter number of types: ");
            int numberTypes = Input.getInteger();

            for (int i = 0; i < numberTypes; i++) {
                System.out.print("Enter type: ");
                String typeName = Input.getLine();

                List<Word.Description.Type.Meaning> meaningsList = new ArrayList<>();

                System.out.println("Enter number of meanings for type '" + typeName + "': ");
                int numberMeanings = Input.getInteger();

                for (int j = 0; j < numberMeanings; j++) {
                    System.out.print("Enter meaning: ");
                    String meaningExplanation = Input.getLine();

                    List<Word.Description.Type.Meaning.Example> examplesList = new ArrayList<>();

                    System.out.println("Enter number of examples for meaning '" + meaningExplanation + "': ");
                    int numberExamples = Input.getInteger();

                    for (int k = 0; k < numberExamples; k++) {
                        System.out.print("Enter example in English: ");
                        String exampleEnglish = Input.getLine();
                        System.out.print("Enter example in Vietnamese: ");
                        String exampleVietnamese = Input.getLine();

                        Word.Description.Type.Meaning.Example example = new Word.Description.Type.Meaning.Example();
                        example.setExample(exampleEnglish, exampleVietnamese);
                        examplesList.add(example);
                    }

                    Word.Description.Type.Meaning meaning = new Word.Description.Type.Meaning();
                    meaning.setMeaning(meaningExplanation, examplesList);
                    meaningsList.add(meaning);
                }

                Word.Description.Type type = new Word.Description.Type();
                type.setType(typeName, meaningsList);
                typesList.add(type);
            }

            Word.Description description = new Word.Description();
            description.setDescription(pronoun, typesList);

            return new Word(word_target, description);
        } catch (Exception e) {
            System.out.printf("%s\n", e.getMessage());
            System.out.println("ReWrite your Input!");
            return createWord();
        }
    }

    public void insertFromCommandLine() {
        //System.out.println("Enter your input with format: word_target <enter> word_explain <enter> word_type");
        addWordToDictionary(createWord());
        System.out.println("Successfull!");
    }

    public void removeFromDictionary(int index) {
        //Search for the word
        dictionary.getDictionary().remove(index - 1);
    }

    public void updateDictionary(int index) {
        System.out.println("Input the change: word_target <enter> word_explain <enter> word_type");
        dictionary.getDictionary().set(index - 1, createWord());
    }

    public void insertFromFile(String file) {
        URL url = DictionaryManagement.class.getResource(file);
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        try (JsonParser jsonParser = jsonFactory.createParser(new BufferedReader(new InputStreamReader(new FileInputStream(FileManager.getPathFromFile(file)), StandardCharsets.UTF_8)))) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    System.out.println(jsonParser.currentName());
                    Word w = new Word(jsonParser.currentName(), o.readValue(jsonParser, Word.Description.class));
                    addWordToDictionary(w);
                    jsonParser.skipChildren();
                }
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
    }

    public void dictionaryExportToFile(String file) {
        try (RandomAccessFile raf = new RandomAccessFile(FileManager.getPathFromFile(file), "rw");) {
            raf.seek(1);
            int i= 0;
            for (Word word : dictionary.getDictionary()) {
                if (i == dictionary.getDictionary().size() -1) {
                    raf.write(word.toString().getBytes(StandardCharsets.UTF_8));
                } else {
                    raf.write(word.toString().getBytes(StandardCharsets.UTF_8));
                    raf.writeBytes(",");
                    i++;
                }
            }
            raf.writeBytes("\n}");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp JSON: " + e.getMessage());
        }
    }
}

