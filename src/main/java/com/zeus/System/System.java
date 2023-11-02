package com.zeus.System;

import com.zeus.App.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class System {
    private final static String configPath = "/com/zeus/config/config.json";
    private static MongoPanel mongoPanel = null;
    private static DictionaryManagement dictionaryManagement = null;
    public System() {
        ConfigFactory configFactory = null;
        try {
            configFactory = new ConfigFactory(FileManager.getPathFromFile(configPath));
            dictionaryManagement = new DictionaryManagement(configFactory.getConfig("Database").getProperty("localDataPath", String.class));
        } catch (UnsupportedEncodingException | FileNotFoundException | MalformedURLException e) {
            Logger.error(e.getMessage());
        }
        if (configFactory != null) {
            App.setWindow(configFactory.getConfig("WindowConfig"));
        } else {
            Logger.warn("Config not exist. Check the name again.");
        }
        if (configFactory != null) {
            mongoPanel = new MongoPanel(configFactory.getConfig("Database").getProperty("mongodbPath", String.class));
        } else {
            Logger.warn("Config not exist. Check the name again.");
        }
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
