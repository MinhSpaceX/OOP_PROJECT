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

    /**
     * Retrieves a manager of the specified type from the manager map.
     *
     * @param tClass The class type of the manager to be retrieved.
     * @param <T>    The type of the manager.
     * @return The manager of the specified type, or null if not found.
     */
    public static <T extends Manager> T getManager(Class<T> tClass) {
        return tClass.cast(managerMap.get(tClass));
    }

    /**
     * Adds a manager to the manager map, using its class as the key.
     *
     * @param manager The manager to be added to the map.
     */
    private static void addToMap(Manager manager) {
        managerMap.put(manager.getClass(), manager);
    }

    /**
     * Retrieves the global configuration factory instance.
     *
     * @return The global configuration factory instance.
     */
    public static ConfigFactory getConfigFactory() {
        return configFactory;
    }

    /**
     * Retrieves the list of all registered managers.
     *
     * @return The list of all registered managers.
     */
    public static List<Manager> getManagerList() {
        return managerList;
    }

    /**
     * Adds a manager to the managerList, taking its priority into account.
     *
     * @param manager The manager to be added to the managerList.
     */
    private static void addToList(Manager manager) {
        if (manager.getPriority() < 0) managerList.add(manager);
        else managerList.add(manager.getPriority(), manager);
    }

    /**
     * Adds a manager to both the managerMap and the managerList.
     *
     * @param manager The manager to be added to the collection.
     */
    public static void addToCollection(Manager manager) {
        addToMap(manager);
        addToList(manager);
    }

    /**
     * Loads all configurations using the ConfigFactory and sets it in the SystemManager.
     */
    public static void loadAllConfig() {
        try {
            SystemManager.configFactory = new ConfigFactory(FileManager.getPathFromFile(SystemManager.configPath));
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * Initializes the SystemManager by loading history and favorite data from files.
     * The loaded data is inserted into the corresponding lists.
     */
    @Override
    public void init() {
        FileManager.insertFromFile(config.getProperty("localHistory", String.class), History.historyList);
        FileManager.insertFromFile(config.getProperty("localFavorite", String.class), FavoriteController.FavoriteList);
        Logger.info("SystemManager initialized.");
    }

    /**
     * Sets the configuration for the Database by obtaining it from the ConfigFactory.
     * The 'Database' configuration is assigned to the 'config' variable.
     */
    @Override
    protected void setConfig() {
        config = configFactory.getConfig("Database");
    }

    /**
     * Terminates the system by exporting history and favorite lists to local files.
     */
    public void terminate() {
        FileManager.dictionaryExportToFile(History.historyList, config.getProperty("localHistory", String.class));
        FileManager.dictionaryExportToFile(FavoriteController.FavoriteList, config.getProperty("localFavorite", String.class));
    }
}
