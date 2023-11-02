package com.zeus.utils.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static String getPathFromFile(String file) throws UnsupportedEncodingException, FileNotFoundException {
        File dir = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        file = dir.getParent() + file;
        file = URLDecoder.decode(file, StandardCharsets.UTF_8);
        File f = new File(file);
        if (!f.exists()) {
            throw new FileNotFoundException("File: " + file + " did not exist.");
        }
        return file;
    }

    public static String readLineFromFile(String filePath, int numberReadline) {
        try (FileReader fr = new FileReader(getPathFromFile(filePath));
             BufferedReader br = new BufferedReader(fr)) {

            int countLine = 0;
            while (numberReadline >= countLine) {
                if (countLine > 1) throw new Exception("ERROR: File contains more than 1 line");
                String line = br.readLine();
                countLine++;
                if (line == null) break;
                return line;
            }

        } catch (Exception e) {
            System.out.printf("%s. File path: '%s'.\n", e.getMessage(), filePath);
        }
        return null;
    }

    /**
     *Load a Parent based on fxml path provided
     * @param fxml
     * @return an object name's parent
     * @throws IOException
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new File(getPathFromFile(fxml)).toURI().toURL());
        return fxmlLoader.load();
    }

    /**
     * return an image loaded from icon.png path
     * @param filePath File path.
     * @return an object of class Image.
     */
    public static Image loadImage(String filePath) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(getPathFromFile(filePath));
        if (file.exists()) {
            return new Image(file.toURI().toString());
        }
        Logger.warn("Cannot find file: " + file.toURI().toString());
        return null;
    }

    public static List<Word> loadWord(String jsonPath, int countWord) {
        List<Word> words = new ArrayList<>();
        URL url = FileManager.class.getResource(jsonPath);
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(url)) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null && countWord > 0) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    countWord--;
                    Word w = new Word(jsonParser.currentName(), o.readValue(jsonParser, Word.Description.class));
                    words.add(w);
                    jsonParser.skipChildren();
                }
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
        return words;
    }

    public static Trie loadTrie(String jsonPath) throws FileNotFoundException, UnsupportedEncodingException, MalformedURLException {
        Trie result = new Trie();
        List<Word> words = new ArrayList<>();
        URL url = new File(getPathFromFile(jsonPath)).toURI().toURL();
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(url)) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    //countWord--;
                    result.insert(jsonParser.currentName());
                    jsonParser.skipChildren();
                }
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
        return result;
    }
}
