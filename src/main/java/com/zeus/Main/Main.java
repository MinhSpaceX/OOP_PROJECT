package com.zeus.Main;

import com.zeus.App.Window.App;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.Managers.SystemApp.SystemManager;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SystemManager.getManager(SystemManager.class).terminate();
            Logger.info("GOODBYE");
        }));
        ManagerFactory.necessary();
        App.run(args);
    }
}