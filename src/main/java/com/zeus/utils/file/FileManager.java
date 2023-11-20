package com.zeus.utils.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.DictionaryUtil.Word;
import com.zeus.utils.log.Logger;
import com.zeus.utils.stackset.StackSet;
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

    public static File getFileFromPath(String filePath) {
        try {
            return new File(getPathFromFile(filePath));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    public static List<Word> deserializeFromFile(String filePath) {
        List<Word> words = new ArrayList<>();
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(getFileFromPath(filePath))) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    Word w = new Word(jsonParser.currentName(), o.readValue(jsonParser, Word.Description.class));
                    words.add(w);
                    jsonParser.skipChildren();
                }
            }
            return words;
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
        return null;
    }

    public static void insertFromFile(String url, StackSet<String> target) {
        try (InputStream is = new FileInputStream(getPathFromFile(url));
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;

            while ((line = br.readLine()) != null) {
                target.addFromFile(line);
            }
            Logger.info("inserted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  void dictionaryExportToFile(StackSet<String> wordTarget, String url) {
        try (FileWriter fw = new FileWriter(FileManager.getPathFromFile(url));
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (String a : wordTarget) {
                bw.write(a.toString()+ "\n");
            }
        } catch (IOException e) {
            System.out.printf("%s", e.getMessage());
        }
    }
}
