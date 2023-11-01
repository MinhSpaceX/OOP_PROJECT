package com.zeus.System;

import com.zeus.App.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class System {
    private final static String configPath = "/com/zeus/config/config.json";
    private ConfigFactory configFactory = null;
    private static MongoPanel mongoPanel = null;
    private static DictionaryManagement dictionaryManagement = null;
    public System() {
        try {
            configFactory = new ConfigFactory(FileManager.getPathFromFile(configPath));
        } catch (UnsupportedEncodingException e) {
            Logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        App.setWindow(configFactory.getConfig("WindowConfig"));
        mongoPanel = new MongoPanel(configFactory.getConfig("Database").getProperty("mongodbPath", String.class));
        dictionaryManagement = new DictionaryManagement(configFactory.getConfig("localDataPath").getProperty("localDataPath", String.class));
    }

    public static MongoPanel getMongoPanel() {
        return mongoPanel;
    }

    public static DictionaryManagement getDictionaryManagement() {
        return dictionaryManagement;
    }

    public void run(String[] args) {
        App.run(args);
    }
}
