package com.zeus.utils.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.config.Config;
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

}
