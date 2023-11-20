package com.zeus.Managers.SystemApp;

import com.zeus.App.Controller.FavoriteController;
import com.zeus.App.Controller.History;
import com.zeus.Managers.Database.MongoManager;
import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.utils.ConfigFactory.ConfigFactory;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.managerfactory.ManagerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemManager extends Manager {
    private final static String configPath = "/com/zeus/config/config.json";
    protected static ConfigFactory configFactory = null;
    private static final List<Manager> managerList = new ArrayList<>();
    private static final Map<Class<? extends Manager>, Manager> managerMap = new HashMap<>();

    public static <T extends Manager> T getManager(Class<T> tClass) {
        return tClass.cast(managerMap.get(tClass));
    }

    private static void addToMap(Manager manager) {
        managerMap.put(manager.getClass(), manager);
    }

    public static ConfigFactory getConfigFactory() {
        return configFactory;
    }

    public static List<Manager> getManagerList() {return managerList;}

    private static void addToList(Manager manager) {
        if (manager.getPriority() < 0) managerList.add(manager);
        else managerList.add(manager.getPriority(), manager);
    }

    public static void addToCollection(Manager manager) {
        addToMap(manager);
        addToList(manager);
    }

    @Override
    public void init() {
        FileManager.insertFromFile(config.getProperty("localHistory", String.class), History.historyList);
        FileManager.insertFromFile(config.getProperty("localHistory", String.class), FavoriteController.FavoriteList);
        Logger.info("SystemManager initialized.");
    }

    public static void loadAllConfig() {
        try {
            SystemManager.configFactory = new ConfigFactory(FileManager.getPathFromFile(SystemManager.configPath));
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
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
