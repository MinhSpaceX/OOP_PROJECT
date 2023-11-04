package com.zeus.System;

import com.zeus.App.Window.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class System {
    private final static String configPath = "/com/zeus/config/config.json";
    private static MongoPanel mongoPanel = null;
    public System() {
        ConfigFactory configFactory = null;
        try {
            configFactory = new ConfigFactory(FileManager.getPathFromFile(configPath));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
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

    public void run(String[] args) {
        App.run(args);
    }
}
