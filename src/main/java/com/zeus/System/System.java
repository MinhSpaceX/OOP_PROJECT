package com.zeus.System;

import com.zeus.App.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class System {
    private final static String configPath = "/com/zeus/config/config.json";
    private ConfigFactory configFactory = null;
    private static MongoPanel mongoPanel = null;
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
    }

    public static MongoPanel getMongoPanel() {
        return mongoPanel;
    }

    public void run(String[] args) {
        App.run(args);
    }
}
