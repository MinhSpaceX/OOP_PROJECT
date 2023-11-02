package com.zeus.System;

import com.zeus.App.App;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoPanel;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class System {
    private final static String configPath = "/com/zeus/config/config.json";
    private ConfigFactory configFactory = null;
    private static MongoPanel mongoPanel = null;
    public System() {
        try {
            configFactory = new ConfigFactory(FileManager.getPathFromFile(configPath));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            Logger.error(e.getMessage());
        }
        App.setWindow(configFactory.getConfig("WindowConfig"));
    }

    public void run(String[] args) {
        App.run(args);
    }
}
