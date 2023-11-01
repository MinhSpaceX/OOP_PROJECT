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
        System.out.println("DictionaryManagement initialized");
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void addWordToDictionary(Word word) {
        dictionary.addWord(word);
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

