package com.zeus.Main;

import com.zeus.App.Controller.FavoriteController;
import com.zeus.App.Controller.History;
import com.zeus.App.Window.App;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.Managers.SystemApp.SystemManager;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SystemManager.getManager(SystemManager.class).terminate();
            Logger.info("GOODBYE");
        }));
        ManagerFactory.neccessary();
        App.run(args);
    }
}