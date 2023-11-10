package com.zeus.System;

import com.zeus.App.Window.App;
import com.zeus.utils.managerfactory.ManagerFactory;
import com.zeus.utils.managerfactory.SystemManager;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        // Shutdown function when the program end.
        /*Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

            }
        });*/
        ManagerFactory.createManager(SystemManager.class).init(null);
        App.run(args);
    }
}