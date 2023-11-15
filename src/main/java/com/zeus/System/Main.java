package com.zeus.System;

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
                FileManager.dictionaryExportToFile(History.historyList);
                Logger.info("GOODBYE");
            }
        });
        FileManager.insertFromFile();
        ManagerFactory.createManager(SystemManager.class).init(null);
        App.run(args);
    }
}