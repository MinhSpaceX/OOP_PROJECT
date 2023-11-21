package com.zeus.Managers.SystemApp;

import com.zeus.App.Controller.FavoriteController;
import com.zeus.App.Controller.History;
import com.zeus.utils.ConfigFactory.ConfigFactory;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemManager extends Manager {
    private final static String configPath = "/com/zeus/config/config.json";
    private static final List<Manager> managerList = new ArrayList<>();
    private static final Map<Class<? extends Manager>, Manager> managerMap = new HashMap<>();
    protected static ConfigFactory configFactory = null;

    public static <T extends Manager> T getManager(Class<T> tClass) {
        return tClass.cast(managerMap.get(tClass));
    }

    private static void addToMap(Manager manager) {
        managerMap.put(manager.getClass(), manager);
    }

    public static ConfigFactory getConfigFactory() {
        return configFactory;
    }

    public static List<Manager> getManagerList() {
        return managerList;
    }

    private static void addToList(Manager manager) {
        if (manager.getPriority() < 0) managerList.add(manager);
        else managerList.add(manager.getPriority(), manager);
    }

    public static void addToCollection(Manager manager) {
        addToMap(manager);
        addToList(manager);
    }

    public static void loadAllConfig() {
        try {
            SystemManager.configFactory = new ConfigFactory(FileManager.getPathFromFile(SystemManager.configPath));
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    @Override
    public void init() {
        FileManager.insertFromFile(config.getProperty("localHistory", String.class), History.historyList);
        FileManager.insertFromFile(config.getProperty("localFavorite", String.class), FavoriteController.FavoriteList);
        Logger.info("SystemManager initialized.");
    }

    @Override
    protected void setConfig() {
        config = configFactory.getConfig("Database");
    }

    public void terminate() {
        FileManager.dictionaryExportToFile(History.historyList, config.getProperty("localHistory", String.class));
        FileManager.dictionaryExportToFile(FavoriteController.FavoriteList, config.getProperty("localFavorite", String.class));
    }
}
