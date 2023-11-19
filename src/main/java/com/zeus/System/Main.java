package com.zeus.System;

import com.zeus.App.Controller.FavoriteController;
import com.zeus.App.Controller.History;
import com.zeus.App.Window.App;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.utils.managerfactory.SystemManager;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                FileManager.dictionaryExportToFile(History.historyList, "/com/zeus/data/history.txt");
                FileManager.dictionaryExportToFile(FavoriteController.FavoriteList, "/com/zeus/data/favorite.txt");
                Logger.info("GOODBYE");
            }
        });
        FileManager.insertFromFile("/com/zeus/data/history.txt", History.historyList);
        FileManager.insertFromFile("/com/zeus/data/favorite.txt", FavoriteController.FavoriteList);
        ManagerFactory.createManager(SystemManager.class).init(null);
        App.run(args);
    }
}