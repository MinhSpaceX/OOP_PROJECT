package com.zeus.Managers.Fxml;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.managerfactory.Manager;

import java.util.Map;

public class FxmlManager extends Manager {
    private Map<String, Object> pathToFxml;

    @Override
    public void init() {
        pathToFxml = config.getProperties().getProperties();
    }

    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("FXML");
    }

    public <T> String getPath(Class<T> classType) {
        Object o = pathToFxml.get(classType.getSimpleName());
        if (o instanceof String) {
            return (String) o;
        }
        return null;
    }
}
