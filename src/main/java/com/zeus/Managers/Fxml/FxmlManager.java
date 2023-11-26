package com.zeus.Managers.Fxml;

import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.managerfactory.Manager;

import java.util.Map;

public class FxmlManager extends Manager {
    private Map<String, Object> pathToFxml;

    /**
     * Initializes the configuration properties for FXML.
     * Retrieves the properties from the "config" object and assigns them to the 'pathToFxml' variable.
     */
    @Override
    public void init() {
        pathToFxml = config.getProperties().getProperties();
    }

    /**
     * Sets the configuration for FXML by retrieving the corresponding configuration object from {@link SystemManager}.
     */
    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("FXML");
    }

    /**
     * Retrieves the file path associated with the provided class type from the pathToFxml map.
     *
     * @param classType The class type for which the file path is requested.
     * @param <T>       The generic type representing the class.
     * @return The file path corresponding to the class type, or null if not found.
     */
    public <T> String getPath(Class<T> classType) {
        Object o = pathToFxml.get(classType.getSimpleName());
        if (o instanceof String) {
            return (String) o;
        }
        return null;
    }
}
