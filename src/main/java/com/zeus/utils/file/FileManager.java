package com.zeus.utils.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zeus.App.Config.Config;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.trie.Trie;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileManager {
    public static String readLineFromFile(String filePath, int numberReadline) {
        try (FileReader fr = new FileReader(new File(filePath));
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
        FXMLLoader fxmlLoader = new FXMLLoader(FileManager.class.getResource(fxml));
        return fxmlLoader.load();
    }

    /**
     * return an image loaded from icon.png path
     * @param filePath File path.
     * @return an object of class Image.
     */
    public static Image loadImage(String filePath) {
        return new Image(filePath);
    }

    /**
     * get configuration based on target and config.json file path provided.
     * @param target
     * @param jsonPath
     * @return an object of Config Class
     */
    public static Config getConfig(String target, String jsonPath) {
        ObjectMapper objm = new ObjectMapper();
        try {
            List<Config> configs = objm.readValue(FileManager.class.getResource(jsonPath), objm.getTypeFactory().constructCollectionType(List.class, Config.class));
            for (Config c : configs) {
                return c;
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
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

    public static Trie loadTrie(String jsonPath, int countWord) {
        Trie result = new Trie();
        List<Word> words = new ArrayList<>();
        URL url = FileManager.class.getResource(jsonPath);
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(url)) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null && countWord > 0) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    countWord--;
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
