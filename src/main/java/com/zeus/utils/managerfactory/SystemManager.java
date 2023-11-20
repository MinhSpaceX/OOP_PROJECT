package com.zeus.utils.managerfactory;

import com.zeus.App.SearchManager;
import com.zeus.ConfigFactory.ConfigFactory;
import com.zeus.DatabaseManager.MongoManager;
import com.zeus.System.System;
import com.zeus.utils.config.Config;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SystemManager extends Manager {
    private final static String configPath = "/com/zeus/config/config.json";
    protected static ConfigFactory configFactory = null;
    private static final Map<Class<? extends Manager>, Manager> managerMap = new HashMap<>();

    public static <T extends Manager> T getManager(Class<T> tClass) {
        return tClass.cast(managerMap.get(tClass));
    }

    public static void addToMap(Manager manager) {
        managerMap.put(manager.getClass(), manager);
    }

    public static ConfigFactory getConfigFactory() {
        return configFactory;
    }

    @Override
    public void init(Config config) {
        try {
            configFactory = new ConfigFactory(FileManager.getPathFromFile(configPath));
            ManagerFactory.createManager(System.class).init(configFactory.getConfig("WindowConfig"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            Logger.error(e.getMessage());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Logger.info("SystemManager initialized.");
    }
}
