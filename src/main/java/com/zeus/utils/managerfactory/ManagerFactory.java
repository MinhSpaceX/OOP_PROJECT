package com.zeus.utils.managerfactory;

import java.lang.reflect.InvocationTargetException;

public class ManagerFactory {
    public static <T extends Manager> T createManager(Class<T> tClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return tClass.getDeclaredConstructor().newInstance();
    }
}
