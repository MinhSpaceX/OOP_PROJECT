package com.zeus.utils.managerfactory;

import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.Managers.ImageIcon.ImageIcon;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.System;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.log.Logger;

import java.util.function.Consumer;

/**
 * Class to handle managers.
 */
public class ManagerFactory {
    /**
     * Create manager.
     *
     * @param tClass Manager class.
     * @param <T>    type of manager.
     * @return Manager instance after created.
     * @throws NullPointerException Exception if cannot create new manager.
     */
    public static <T extends Manager> T createManager(Class<T> tClass) throws NullPointerException {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        throw new NullPointerException();
    }

    /**
     * Initialize necessary managers for the application.
     */
    public static void necessary() {
        try {
            SystemManager.loadAllConfig();
            ManagerFactory.createManager(SystemManager.class).init();
            ManagerFactory.createManager(System.class).init();
            ManagerFactory.createManager(FxmlManager.class).init();
            ManagerFactory.createManager(SQLite.class);
            //ManagerFactory.createManager(MongoManager.class);
            ManagerFactory.createManager(SearchManager.class);
            ManagerFactory.createManager(ImageIcon.class);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * Initialize all existing manager in {@link SystemManager#getManagerList()}.
     *
     * @param consumer Tasks to do with each manager in the list.
     */
    public static void initAllManager(Consumer<Manager> consumer) {
        try {
            for (Manager manager : SystemManager.getManagerList()) {
                Thread thread = new Thread(() -> {
                    consumer.accept(manager);
                    if (manager.isInitRunnable()) manager.getInitRunnable().run();
                    manager.init();
                });
                thread.start();
                thread.join();
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }
}
