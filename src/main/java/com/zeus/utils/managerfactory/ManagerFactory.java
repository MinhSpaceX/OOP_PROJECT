package com.zeus.utils.managerfactory;

import com.zeus.Managers.Database.SQLite;
import com.zeus.Managers.Fxml.FxmlManager;
import com.zeus.Managers.ImageIcon.ImageIcon;
import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.System;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.log.Logger;

import java.util.function.Consumer;

public class ManagerFactory {
    public static <T extends Manager> T createManager(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        return null;
    }

    public static void neccessary() {
        SystemManager.loadAllConfig();
        ManagerFactory.createManager(SystemManager.class).init();
        ManagerFactory.createManager(System.class).init();
        ManagerFactory.createManager(FxmlManager.class).init();
        ManagerFactory.createManager(SQLite.class);
        //ManagerFactory.createManager(MongoManager.class);
        ManagerFactory.createManager(SearchManager.class);
        ManagerFactory.createManager(ImageIcon.class);
    }

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
